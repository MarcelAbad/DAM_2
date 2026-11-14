# Decisiones técnicas — CampusHub (parte de Marcel: actividades, inscripciones y etiquetas)

## 1. Qué almaceno en PostgreSQL y qué en MongoDB

**PostgreSQL (mi parte):** `actividades`, `inscripciones`, `etiquetas` y la tabla intermedia `actividad_etiqueta`.

**MongoDB:** nada de mi parte. Los seguimientos de las actividades (parte de Óscar) van en MongoDB y se enlazan con mis actividades por su identificador.

## 2. Por qué estas decisiones

Mis datos tienen estructura fija, se relacionan entre sí y necesitan reglas estrictas, que es lo que mejor hace una base relacional. Los seguimientos cambian de estructura según el tipo de actividad, y ahí compensa la flexibilidad de MongoDB.

| Necesidad | Mis datos | Motor |
|---|---|---|
| Estructura fija | Siempre título, fecha, plazas, docente | PostgreSQL |
| Relaciones entre datos | Inscripción une estudiante y actividad | PostgreSQL (claves foráneas) |
| Evitar duplicados | Inscripción y etiqueta únicas | PostgreSQL (`UNIQUE`) |
| Control de aforo concurrente | Última plaza | PostgreSQL (transacción y bloqueo) |
| Estructura variable | Seguimientos | MongoDB |

**Reglas protegidas en la propia base de datos** (comprobadas con `psql`, no solo en la API):

| Regla | Mecanismo |
|---|---|
| Plazas máximas > 0 | `CHECK (max_participantes > 0)` |
| Sin inscripción duplicada | `UNIQUE (actividad_id, estudiante_id)` |
| Sin etiqueta repetida en una actividad | `UNIQUE (actividad_id, etiqueta_id)` |
| Nombre de etiqueta único | `UNIQUE (nombre)` |
| Docente y estudiante existentes | Claves foráneas `NOT NULL` |
| Longitudes y obligatoriedad | `varchar(200/50/80)`, `text` para la descripción, `NOT NULL` |

**Índices:** `(actividad_id, estudiante_id)` (único) para duplicados, plazas ocupadas y participantes; `estudiante_id` para el historial; `docente_id` en actividades.

**Mecanismo elegido:** anotaciones JPA/Hibernate en las entidades, porque mantienen la regla junto al código que la usa. Se verificó en la base real.

**Aforo con concurrencia:** `inscribir` bloquea la fila de la actividad (`SELECT … FOR UPDATE`) dentro de una transacción, comprueba duplicado y plazas, y guarda. Dos inscripciones simultáneas se ejecutan una tras otra. Si algo se escapa, el `UNIQUE` lo detiene y la API responde 409 sin mostrar SQL.

**Modelado:** `Inscripcion` es una entidad propia porque guarda la fecha; las etiquetas son un `@ManyToMany` con `Set` (no admite repetidos), porque la relación no tiene datos propios.

## 3. Alternativas consideradas

| Decisión | Alternativa | Por qué no |
|---|---|---|
| Bloqueo pesimista del aforo | Bloqueo optimista (`@Version`) | Exige gestionar reintentos y errores confusos |
| | Contar y guardar sin bloqueo | Falla con peticiones simultáneas |
| | Contador `plazas_ocupadas` | Dato duplicado que puede desincronizarse |
| Etiquetas en tabla propia | Texto, array o `jsonb` en la actividad | Pierden unicidad real y claves foráneas |
| Etiquetas en MongoDB | | Son datos relacionales y estables; no hay ventaja |
| `Inscripcion` como entidad | `@ManyToMany` Estudiante–Actividad | No puede guardar la fecha de inscripción |
| Seguimientos en MongoDB | `jsonb` en PostgreSQL | Habría un solo motor, pero se pierde el modelo documental natural |

## 4. Riesgos de usar dos motores

Mis actividades están en PostgreSQL y sus seguimientos en MongoDB, que **no tiene claves foráneas** hacia PostgreSQL. Pueden aparecer dos inconsistencias:

- **Seguimiento huérfano:** apunta a una actividad que no existe.
- **Escritura a medias:** se guarda en un motor y falla en el otro.

**Cómo reducir el riesgo** (sin transacciones distribuidas): comprobar en PostgreSQL que la actividad existe antes de escribir en MongoDB; escribir primero en PostgreSQL; no ofrecer borrado de actividades; indexar `actividadId` en MongoDB; y revisar periódicamente seguimientos sin actividad.

## 5. Limitaciones

- Las tablas las crea Hibernate (`ddl-auto: update`); en producción se usarían migraciones (Flyway).
- `tipo` es texto libre (máx. 50), no una lista cerrada.
- El aforo concurrente está diseñado y razonado, pero no tiene test automático; los tests públicos lo prueban en secuencia.
- `fechaInscripcion` usa el reloj de la aplicación, no el de la base de datos.
- No hay borrado en cascada: las claves foráneas impiden borrar datos con relaciones, para conservar el historial.
- Falta conectar `CampusHubServiceImpl` a los tres servicios y el detalle de la actividad (RF06), que necesita los seguimientos.
- Los tests de integración del proyecto base no pueden lanzarse todos juntos (contenedores estáticos): se ejecutan por clase, p. ej. `mvn verify -Dit.test=RelacionesApiIT`.

## 6. Qué cambió respecto al diseño inicial

- La primera versión de `Actividad` y `Etiqueta` no tenía `CHECK` de plazas, índices ni lista de inscripciones; los añadí al incorporar `Inscripcion`.
- Pasé de validar solo en el servicio a **validar también en la base de datos**, para que `psql` no pueda saltarse las reglas.
- Añadí el bloqueo de fila al ver que "contar y guardar" no es seguro con peticiones simultáneas.
- Mantuve `tipo` libre para no romper posibles pruebas con otros valores.
