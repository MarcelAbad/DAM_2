# Defensa de mi parte de CampusHub (Persona B: Marcel)

Documento para estudiar y defender mi parte. Está escrito en lenguaje sencillo, con comparaciones y con ejemplos. Todo lo que aparece como "comprobado" lo he probado de verdad (con los tests y con `psql`).

---

## 1. Qué parte es la mía (en 30 segundos)

CampusHub gestiona actividades de un centro educativo (talleres, charlas, visitas…). Mi parte es:

| Lo mío | Para qué sirve |
|---|---|
| **Actividad** | Una actividad con título, descripción, tipo, fecha, plazas máximas y docente responsable |
| **Inscripción** | Apuntar a un estudiante a una actividad (con fecha de inscripción) |
| **Etiqueta** | Una "palabra clave" (programación, empresa, seguridad…) que se pone a las actividades |

Y la lógica que hace que funcionen: **crear y consultar actividades, inscribir sin duplicados y sin pasarse del aforo, ver los participantes de una actividad, ver el historial de un estudiante, y crear/asociar etiquetas**.

Lo que **no** es mío: grupos, estudiantes, docentes, departamentos y contacto (Álvaro) y los seguimientos en MongoDB (Óscar). Yo dependo de las clases `Docente` y `Estudiante` de Álvaro.

**La idea central de todo mi diseño:** *las reglas importantes las protege la base de datos, no solo el código.* Si alguien se salta la API y mete datos a mano con `psql`, la base de datos los rechaza igualmente.

---

## 2. El modelo de datos

```
 departamento ──< docente >── (responsable de) ──< ACTIVIDADES >──< ACTIVIDAD_ETIQUETA >── ETIQUETAS
                                                       │
                                                       │ 1
                                                       │
                                                       N
 grupo ──< estudiante ──────────────────────────< INSCRIPCIONES
                                  (un estudiante en muchas actividades,
                                   una actividad con muchos estudiantes)
```

Las tablas en mayúsculas son las mías. Resumen:

| Tabla | Columnas importantes | Restricciones |
|---|---|---|
| `actividades` | id, titulo (200), descripcion (text), tipo (50), fecha_prevista, max_participantes, docente_id | `CHECK (max_participantes > 0)`, FK a `docente`, todo NOT NULL, índice por `docente_id` |
| `inscripciones` | id, actividad_id, estudiante_id, fecha_inscripcion | `UNIQUE (actividad_id, estudiante_id)`, 2 FK, todo NOT NULL, índice por `estudiante_id` |
| `etiquetas` | id, nombre (80) | `UNIQUE (nombre)`, NOT NULL |
| `actividad_etiqueta` | actividad_id, etiqueta_id | `UNIQUE (actividad_id, etiqueta_id)`, 2 FK |

### ¿Por qué todo esto en PostgreSQL y no en MongoDB?

Una forma fácil de entenderlo: **PostgreSQL es como un archivador con fichas muy ordenadas y reglas estrictas; MongoDB es como una carpeta donde cada documento puede tener un formato distinto.**

| Pregunta | Mis datos (actividades, inscripciones, etiquetas) | Qué motor encaja |
|---|---|---|
| ¿Tienen estructura fija y conocida? | Sí: siempre título, fecha, plazas… | PostgreSQL |
| ¿Se relacionan entre sí? | Sí: inscripción une estudiante con actividad | PostgreSQL (claves foráneas) |
| ¿Necesito evitar duplicados? | Sí: no repetir inscripción ni etiqueta | PostgreSQL (`UNIQUE`) |
| ¿Necesito controlar un aforo con peticiones simultáneas? | Sí | PostgreSQL (transacciones y bloqueos) |
| ¿La estructura cambia con el tiempo? | No | (si cambiase, sería MongoDB) |

**Los seguimientos (Óscar) sí van a MongoDB** porque cada tipo de actividad guarda datos distintos (un taller guarda lenguaje y ejercicios; una visita, la empresa y las incidencias) y pueden aparecer tipos nuevos. Ahí la flexibilidad compensa.

---

## 3. Cada regla del enunciado y cómo la protejo

Esta es la tabla más importante para la defensa. La columna "Comprobado" la ejecuté yo en `psql` contra la base de datos real.

| Regla | Cómo se protege | Comprobado en psql |
|---|---|---|
| Plazas máximas mayores que cero | `CHECK (max_participantes > 0)` en la tabla | ✅ Insertar con 0 → error de `ck_actividad_max_participantes` |
| Una actividad tiene docente que existe | FK `docente_id` NOT NULL | ✅ Docente 999 → error de clave foránea |
| No inscribir dos veces al mismo estudiante | `UNIQUE (actividad_id, estudiante_id)` | ✅ Segunda inscripción → error `uk_inscripcion_actividad_estudiante` |
| No inscribir a un estudiante que no existe | FK `estudiante_id` | ✅ Estudiante 999 → error de clave foránea |
| Nombre de etiqueta único | `UNIQUE (nombre)` | ✅ Segunda "seguridad" → error de duplicado |
| No repetir la relación actividad–etiqueta | `UNIQUE (actividad_id, etiqueta_id)` en la tabla intermedia | ✅ Segundo insert → error `uk_actividad_etiqueta` |
| Longitud máxima del título | `varchar(200)` | ✅ Título de 300 caracteres → "value too long" |
| Descripción larga | tipo `text` (sin límite práctico) | — |
| Campos obligatorios | `NOT NULL` en todas las columnas | — |
| No superar el aforo | Lógica en el servicio + bloqueo (ver sección 4) | ✅ Test `inscripcionUnicaYAforo` (primera 201, repetida 409, sin plazas 409) |

### ¿Por qué `CHECK` y `UNIQUE` en la base de datos y no solo en Java?

Comparación con un ejemplo de la vida real:

- **Validar solo en Java** = poner un cartel en la puerta que dice "prohibido pasar sin entrada". Quien entre por otra puerta (otro programa, `psql`, un script) se lo salta.
- **Validar también en la base de datos** = además, tener un guardia dentro. Da igual por dónde entres: el guardia te para.

El enunciado lo pide expresamente ("las reglas importantes no deben depender únicamente de la validación de la API") y avisa de que el profesor puede insertar datos inválidos a mano.

Yo hago **las dos cosas**: el servicio valida primero (para dar un mensaje claro: 400, 404 o 409) y la base de datos actúa como red de seguridad final.

### Índices: ¿para qué sirven?

Un índice es como el índice de un libro: sin él, la base de datos lee todas las filas para encontrar algo; con él, va directo.

| Índice | Consulta a la que ayuda |
|---|---|
| `uk_inscripcion_actividad_estudiante` (actividad, estudiante) | "¿Está este estudiante ya inscrito?", "plazas ocupadas de esta actividad", "participantes de esta actividad" |
| `idx_inscripcion_estudiante` | **Historial de un estudiante**: "todas las actividades de este estudiante" |
| `idx_actividad_docente` | Buscar actividades de un docente |
| `uk_actividad_etiqueta` (actividad, etiqueta) | Etiquetas de una actividad |
| `UNIQUE (nombre)` de etiquetas | Comprobar si una etiqueta ya existe |

Detalle que puede preguntar el profesor: el índice único `(actividad_id, estudiante_id)` sirve también para buscar solo por `actividad_id` (porque es la primera columna), por eso no hice otro índice aparte para eso. En cambio, para buscar por `estudiante_id` solo, sí hizo falta uno propio.

---

## 4. El aforo y las peticiones simultáneas (la parte más técnica)

### El problema

Imagina una actividad con **1 plaza libre** y dos estudiantes que pulsan "inscribirme" **a la vez**:

```
Estudiante A: ¿Quedan plazas? → cuenta 0 inscritos de 1 → "sí, queda"
Estudiante B: ¿Quedan plazas? → cuenta 0 inscritos de 1 → "sí, queda"   ← ambos miran antes de que el otro guarde
A guarda su inscripción.
B guarda su inscripción.
Resultado: 2 inscritos en una actividad de 1 plaza. ❌
```

Es como dos personas comprando la última entrada de un concierto: las dos ven "queda 1", las dos pagan.

### Mi solución: bloquear la fila de la actividad

En `ActividadRepository.findByIdParaActualizar` uso un bloqueo (`PESSIMISTIC_WRITE`), que en PostgreSQL es `SELECT ... FOR UPDATE`. Es como poner **un solo mostrador con una cola**: mientras A está siendo atendido, B espera. Cuando A termina, B ya ve la plaza ocupada.

El método `inscribir` hace estos pasos, **todos dentro de una misma transacción**:

1. **Bloquear** la actividad (si no existe → 404).
2. Comprobar que el estudiante existe (si no → 404).
3. Comprobar que no está ya inscrito (si lo está → 409).
4. Contar las inscripciones: si `ocupadas >= maxParticipantes` → 409 "sin plazas".
5. Guardar la inscripción con la fecha de ahora.

### Alternativas que consideré (y por qué elegí esta)

| Alternativa | Cómo funciona | Por qué no (o por qué sí) |
|---|---|---|
| **Solo contar y guardar, sin bloqueo** | Lo del ejemplo de arriba | ❌ Falla con peticiones simultáneas |
| **Bloqueo pesimista** (la que usé) | Bloquear la fila y que los demás esperen | ✅ Simple, correcto, fácil de explicar. Coste: las inscripciones a una *misma* actividad se hacen de una en una (aceptable: es una operación breve) |
| **Bloqueo optimista** (`@Version`) | Dejar que ambos intenten y, si chocan, uno falla y reintenta | Más complejo: habría que gestionar reintentos y devolver errores raros al usuario |
| **Un `CHECK` o *trigger* en la base de datos que cuente** | La propia BD rechaza si se supera | Muy robusto, pero un `CHECK` normal no puede mirar otras filas; haría falta un trigger en SQL, fuera de JPA |
| **Contador `plazas_ocupadas` en la actividad** | Guardar el número y sumarle 1 | Dos sitios con la misma información (riesgo de que no coincidan) |

### Y si se escapa algo: la red de seguridad

Aunque el bloqueo falle por algún motivo, `UNIQUE(actividad, estudiante)` impide duplicar. Si salta esa restricción, el `ApiExceptionHandler` (ya hecho en el proyecto base) la convierte en un **409 Conflict** sin enseñar SQL.

> **Honestidad para la defensa:** el aforo con peticiones simultáneas está *razonado y diseñado así*, pero **no tengo un test automático que lance dos inscripciones a la vez**. Los tests públicos comprueban el aforo en secuencia (una detrás de otra). Lo digo yo antes de que lo pregunten.

---

## 5. Etiquetas: relación "muchos a muchos"

Una actividad puede tener varias etiquetas y una etiqueta puede estar en muchas actividades. Eso es **N:M** y en bases de datos relacionales **siempre necesita una tabla intermedia** (`actividad_etiqueta`).

Analogía: los alumnos y las asignaturas. Un alumno cursa varias asignaturas y una asignatura tiene varios alumnos. Para saber quién cursa qué necesitas una "lista de matrículas" en medio.

### Cómo lo programé

- `Actividad` tiene `Set<Etiqueta> etiquetas` con `@ManyToMany`. Es el **lado propietario** (el que manda sobre la tabla intermedia).
- `Etiqueta` tiene `Set<Actividad> actividades` con `@ManyToMany(mappedBy = "etiquetas")`. Es el **lado inverso** (solo "mira" la relación).

### ¿Por qué un `Set` y no una `List`?

Un `Set` (conjunto) **no admite repetidos**. Si asocio la misma etiqueta dos veces, el conjunto la ignora: la operación es *idempotente* (hacerla una o diez veces da el mismo resultado). Es justo lo que pide el enunciado ("asociar dos veces la misma etiqueta no debe duplicar la relación"). Y si algo fallase, `UNIQUE (actividad_id, etiqueta_id)` lo detiene en la base de datos.

### Alternativas para las etiquetas

| Alternativa | Idea | Por qué no |
|---|---|---|
| **Tabla propia de etiquetas + tabla intermedia** (la que usé) | Normalizado | ✅ No se repite el nombre, se puede cambiar en un solo sitio, se pueden buscar actividades por etiqueta con un índice |
| **Guardar las etiquetas como texto dentro de la actividad** ("programación,empresa") | Todo en una columna | ❌ Errores de escritura ("programacion" vs "programación"), no se pueden poner restricciones ni buscar eficazmente |
| **Array o `jsonb` de PostgreSQL** | Lista dentro de la fila | Mejor que texto plano, pero pierde claves foráneas y unicidad real del nombre, y las consultas "dame las actividades con esta etiqueta" son menos claras |
| **Guardarlas en MongoDB** | Documento con lista de etiquetas | Las etiquetas son datos relacionales y estables; el enunciado quiere que el detalle las combine con datos de PostgreSQL. No hay ventaja |

---

## 6. ¿Por qué `Inscripcion` es una entidad propia y no un simple `@ManyToMany`?

Pregunta típica. Hubiera sido posible poner `@ManyToMany` entre `Estudiante` y `Actividad`, como con las etiquetas. Pero una inscripción **guarda datos propios**: la **fecha de inscripción**. Una relación `@ManyToMany` simple no puede guardar datos extra en la tabla intermedia.

Regla fácil de recordar:

| ¿La relación N:M tiene datos propios? | Qué usar |
|---|---|
| **No** (etiquetas: solo une dos cosas) | `@ManyToMany` con tabla intermedia |
| **Sí** (inscripción: tiene fecha) | Entidad propia con dos `@ManyToOne` |

`Inscripcion` tiene entonces: `@ManyToOne actividad`, `@ManyToOne estudiante` y `fechaInscripcion`. Es el **lado propietario** (tiene la clave foránea). `Actividad.inscripciones` y `Estudiante.inscripciones` son el lado inverso (`@OneToMany(mappedBy = ...)`), tal como fija el acuerdo de entidades del equipo.

### `LAZY` y las transacciones (dato que se suele preguntar)

Las relaciones están en `FetchType.LAZY`: la base de datos **no carga el docente de una actividad hasta que alguien lo pide**. Es como no sacar toda la documentación de un cliente si solo quieres su nombre. Ventaja: más rápido.

Consecuencia: para leer esos datos hay que estar **dentro de una transacción**. Por eso mis servicios llevan `@Transactional` (y `readOnly = true` en las consultas). En el proyecto, además, `open-in-view` está en `false`, así que fuera de la transacción no se podría acceder a datos "perezosos".

También por eso las consultas del historial y de participantes usan `join fetch`: traen la actividad, el docente o el estudiante **en una sola consulta** en vez de hacer una por cada fila (el famoso problema "N+1", que haría 1 consulta + N consultas extra).

---

## 7. Cómo está organizado el código (capas)

```
Petición HTTP
   │
   ▼
Controller          (ya venía hecho: recibe la petición y devuelve la respuesta)
   │
   ▼
Service             (MI LÓGICA: validar, comprobar reglas, decidir 404/409/400)
   │
   ▼
Repository          (habla con la base de datos: guardar y buscar)
   │
   ▼
Entity / BD         (las tablas)

Mapper: convierte Entity ⇄ DTO (lo que se envía en el JSON)
```

Comparación con un restaurante: el **camarero** (controller) toma el pedido, el **cocinero** (service) decide cómo prepararlo y comprueba que se puede hacer, el **almacén** (repository) guarda y entrega los ingredientes, y el **mapper** es quien emplata el resultado en el formato que espera el cliente.

Mis ficheros:

| Fichero | Función |
|---|---|
| `entity/Inscripcion.java` | Entidad nueva |
| `entity/Actividad.java`, `Etiqueta.java` | Entidades (las completé: CHECK, índices, lista de inscripciones) |
| `repository/InscripcionRepository.java` | Consultas: duplicados, contar plazas, participantes, historial |
| `repository/ActividadRepository.java` | Bloqueo para el aforo y búsqueda por etiqueta |
| `mapper/ActividadMapper.java` | Convierte entidades a respuestas JSON |
| `service/ActividadService(.java / Impl.java)` | Toda la lógica |

**¿Por qué un servicio propio (`ActividadService`) y no meterlo todo en `CampusHubServiceImpl`?** Porque así cada integrante trabaja en su fichero sin pisar los de los demás (Álvaro hizo `OrganizacionService`, Óscar `SeguimientoService`). Luego `CampusHubServiceImpl` solo tiene que "repartir" cada operación al servicio correspondiente.

### Entidades con ids, no el DTO

Los DTO traen `docenteId` y `estudianteId` (números), pero en las entidades guardo el **objeto completo** (`docente`, `estudiante`), como dice el acuerdo del equipo. El servicio busca el docente por su id y, si no existe, devuelve 404. Esto garantiza que nunca se guarda una actividad con un docente inventado.

---

## 8. Códigos de error: ¿cuándo devuelvo cada uno?

| Código | Cuándo | Ejemplo |
|---|---|---|
| **201** | Algo se creó | Crear actividad o inscripción |
| **204** | Hecho, sin cuerpo de respuesta | Asociar una etiqueta |
| **400** | El dato enviado es inválido | Título vacío, fecha mal escrita, `maxParticipantes` ≤ 0 |
| **404** | Lo que se pide o referencia no existe | Actividad, estudiante, docente o etiqueta inexistente |
| **409** | Choca con algo que ya existe | Inscripción duplicada, etiqueta repetida, **sin plazas** |

Los mensajes de error **no muestran SQL ni detalles internos** (lo exige el enunciado).

Diferencia fácil entre 400 y 409: **400 = "lo que me envías está mal"**; **409 = "lo que me envías está bien, pero choca con el estado actual"** (ya existe, o no quedan plazas).

---

## 9. El problema de usar dos bases de datos (RF07)

El enunciado pide explicar cómo puede aparecer una inconsistencia entre PostgreSQL y MongoDB. Aunque los seguimientos son de Óscar, afecta a mi parte porque **mis actividades están en PostgreSQL y sus seguimientos en MongoDB**.

### Cómo puede romperse

- **Seguimiento huérfano:** se guarda un seguimiento en MongoDB con `actividadId = 5`, pero esa actividad no existe en PostgreSQL (o se borra después). MongoDB **no tiene claves foráneas**: no sabe que la actividad 5 está en otra base de datos.
- **Fallo a medias:** una operación escribe en PostgreSQL y luego falla al escribir en MongoDB (o al revés). Queda guardado solo un lado.

Analogía: tienes la agenda de contactos en el móvil y las fotos en la nube. Borras un contacto en el móvil y las fotos de esa persona siguen en la nube, sin nadie a quien enlazarlas. Cada sitio es correcto por separado, pero no se hablan.

### Cómo se reduce el riesgo (sin transacciones distribuidas)

| Medida | Efecto |
|---|---|
| **Comprobar en PostgreSQL que la actividad existe *antes* de escribir en MongoDB** | Evita seguimientos de actividades inventadas |
| **No borrar actividades** (la API no tiene endpoint de borrado) | Evita que el seguimiento se quede huérfano |
| **Escribir primero en la base "principal" (PostgreSQL) y después en MongoDB** | Si falla Mongo, lo importante ya está bien guardado |
| **Índice en MongoDB por `actividadId`** | Recuperar seguimientos rápido |
| **Revisión periódica** (consulta que busque seguimientos cuyo `actividadId` no exista) | Detectar y limpiar inconsistencias |

---

## 10. Limitaciones y cosas mejorables (dilas tú antes de que las pregunten)

Reconocer las limitaciones con claridad suele puntuar mejor que ocultarlas.

1. **`ddl-auto: update`.** Las tablas las crea Hibernate al arrancar. Es cómodo para la práctica, pero en un proyecto real se usarían **migraciones** (Flyway o Liquibase) para controlar los cambios del esquema. Ventaja de lo que hice: las restricciones (`CHECK`, `UNIQUE`, índices) están declaradas con anotaciones en las entidades y **se verificaron en la base real**.
2. **El campo `tipo` es texto libre** (máximo 50). El enunciado menciona cuatro tipos y los tests usan `TALLER`, `CHARLA` y `VISITA`. No lo limité a una lista cerrada para no romper posibles pruebas privadas con otros valores. Mejora posible: un `CHECK` o un `enum`.
3. **El aforo con peticiones simultáneas no tiene test automático** (explicado en la sección 4).
4. **`fechaInscripcion` se calcula con el reloj de la aplicación** (`LocalDateTime.now()`), no con el de la base de datos. Es suficiente aquí; con varios servidores convendría usar la hora de la base de datos.
5. **Borrado:** no definí borrado en cascada. Las claves foráneas usan el comportamiento por defecto (**no permitir borrar** algo que tiene datos relacionados). Lo razoné así porque el historial de inscripciones es información valiosa que no debe desaparecer por borrar una actividad, y la API no ofrece borrado.
6. **Falta por conectar:** `CampusHubServiceImpl` (que usan los controllers) sigue devolviendo 501 hasta que el equipo lo conecte a los tres servicios. Y el **detalle de la actividad (RF06)** depende de los seguimientos de Óscar.
7. **Fallo del proyecto base en los tests:** los contenedores de prueba son estáticos en la clase padre y no se pueden lanzar todas las clases juntas (la segunda ya no encuentra la base de datos). Se lanzan por clase: `mvn verify -Dit.test=RelacionesApiIT`. No lo causa nuestro código.

---

## 11. Cómo demostrarlo en la defensa

### Entrar en PostgreSQL

```bash
docker compose exec postgres psql -U campushub -d campushub
```

> Esta forma entra **dentro del contenedor**, así que funciona aunque tengas otro PostgreSQL instalado en Windows en el puerto 5432 (en mi comprobación había uno y la aplicación se conectaba a ese por error).

Comandos útiles dentro de `psql`:

```sql
\d actividades        -- estructura, CHECK, índices y claves foráneas
\d inscripciones      -- UNIQUE(actividad, estudiante) e índices
\d actividad_etiqueta
select * from inscripciones;
```

### Pruebas de datos inválidos (las que probé yo)

```sql
-- Debe fallar por el CHECK:
insert into actividades(titulo,descripcion,tipo,fecha_prevista,max_participantes,docente_id)
values ('T','d','TALLER','2027-01-01',0,1);

-- Debe fallar por el UNIQUE (ejecutar dos veces con ids reales):
insert into inscripciones(actividad_id,estudiante_id,fecha_inscripcion) values (1,1,now());

-- Debe fallar por clave foránea:
insert into inscripciones(actividad_id,estudiante_id,fecha_inscripcion) values (1,999,now());
```

### Ejecutar los tests de mi parte

```bash
mvn verify -Dit.test=RelacionesApiIT
```

Resultado esperado hoy: pasan las etiquetas y los 201/409/409 de la inscripción; el test se detiene después en el **detalle**, que aún no está conectado.

---

## 12. Preguntas que puede hacer el profesor (y respuestas cortas)

**¿Qué pasa si dos personas se inscriben a la vez en la última plaza?**
Bloqueo la fila de la actividad con `SELECT ... FOR UPDATE`. La segunda inscripción espera a que termine la primera y entonces ya ve la plaza ocupada, así que recibe un 409.

**¿Por qué el `UNIQUE` si ya compruebas el duplicado en Java?**
Porque comprobar en Java no protege frente a accesos directos a la base de datos ni a fallos de concurrencia. La base de datos es la última línea de defensa.

**¿Por qué `Inscripcion` es una entidad y no un `@ManyToMany`?**
Porque guarda la fecha de inscripción. Un `@ManyToMany` simple no puede guardar datos propios de la relación.

**¿Por qué `Set` en las etiquetas?**
Porque un conjunto no admite repetidos, así que asociar la misma etiqueta dos veces no la duplica. Además hay un `UNIQUE` en la tabla intermedia.

**¿Qué es el lado propietario y el inverso?**
El propietario es el que tiene la clave foránea y manda sobre la relación (`Inscripcion.actividad`, `Actividad.etiquetas`). El inverso (`mappedBy`) solo la refleja y se usa para navegar desde el otro lado.

**¿Qué es el problema N+1?**
Pedir una lista de N elementos y hacer luego una consulta más por cada uno. Lo evito con `join fetch`, que trae todo en una sola consulta.

**¿Por qué `LAZY`?**
Para no cargar datos relacionados que nadie ha pedido. Por eso las operaciones van en una transacción.

**¿Por qué no pusiste las etiquetas o las inscripciones en MongoDB?**
Son datos con estructura fija que necesitan relaciones, claves foráneas y unicidad, que es lo que mejor hace PostgreSQL. MongoDB lo reservamos para los seguimientos, que sí cambian de estructura.

**¿Se podría hacer con `jsonb` de PostgreSQL?**
Sí para algunos casos (por ejemplo los seguimientos), y evitaría usar dos motores. Pero se pierde la flexibilidad natural de documentos y, para mis datos, no aporta nada: lo que necesito son relaciones y restricciones, no estructura variable.

**¿Qué mejorarías?**
Migraciones con Flyway en vez de `ddl-auto: update`, un test de concurrencia para el aforo, limitar `tipo` a una lista de valores válidos y tests unitarios propios del servicio.

---

## 13. Mi historial de commits (por si el profesor los revisa)

Hice commits pequeños, cada uno con un paso lógico:

| # | Commit | Qué hace |
|---|---|---|
| 1 | Aplica formato Spotless | Dejar mis ficheros con el estilo del proyecto |
| 2 | Añade entidad `Inscripcion` | Nueva tabla con `UNIQUE` y claves foráneas |
| 3 | Protege `maxParticipantes > 0` con CHECK | La regla de plazas en la base de datos |
| 4 | Conecta `Actividad` y `Estudiante` con `Inscripcion` | Lados inversos de la relación |
| 5 | `InscripcionRepository` | Consultas de duplicados, plazas, participantes, historial |
| 6 | `ActividadRepository` | Bloqueo para el aforo y búsqueda por etiqueta |
| 7 | `ActividadMapper` | Conversión de entidades a respuestas JSON |
| 8 | Servicio de etiquetas | Crear, asociar y listar por etiqueta |
| 9 | Servicio de actividades | Crear y consultar con validaciones |
| 10 | Servicio de inscripciones | Sin duplicados y control de aforo |
| 11 | Participantes e historial | Consultas de lectura |

Cada commit se puede explicar de forma independiente, que es lo que querrá comprobar el profesor.

---

## 14. Resumen de una frase para cerrar

> *"He modelado actividades, inscripciones y etiquetas en PostgreSQL porque son datos estructurados y relacionados; he protegido las reglas importantes (unicidad, plazas, referencias válidas) en la propia base de datos y las he comprobado con `psql`; y he evitado que se supere el aforo con peticiones simultáneas bloqueando la fila de la actividad."*
