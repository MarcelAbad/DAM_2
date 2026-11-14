# Explicación clase por clase (mi parte de CampusHub)

Para cada clase: **qué es**, **qué hace**, **explicación de las partes importantes** y **cómo decirlo en la defensa**.

## Mapa general: qué clase hace qué

Piensa en un restaurante:

| Clase | Papel en el restaurante | Tipo |
|---|---|---|
| `Actividad`, `Inscripcion`, `Etiqueta` | Los **platos y fichas** (los datos que existen) | Entidad (tabla) |
| `ActividadRepository`, `InscripcionRepository`, `EtiquetaRepository` | El **almacén** (guarda y busca datos) | Repositorio |
| `ActividadService` | La **carta de lo que sabemos hacer** (lista de operaciones) | Interfaz |
| `ActividadServiceImpl` | El **cocinero**: decide, comprueba las reglas y ejecuta | Servicio |
| `ActividadMapper` | El que **emplata**: convierte el dato al formato que se entrega al cliente | Mapper |
| Controllers (ya venían hechos) | El **camarero**: recibe la petición y entrega la respuesta | Controller |

El camino de una petición, por ejemplo "inscribir a un estudiante":

```
Cliente (Swagger/Postman)
   │  POST /api/actividades/3/inscripciones   {"estudianteId": 7}
   ▼
ActividadController ──► CampusHubService ──► ActividadServiceImpl.inscribir()
                                                   │  usa los repositorios
                                                   ▼
                                 ActividadRepository / InscripcionRepository / EstudianteRepository
                                                   │  hablan con PostgreSQL
                                                   ▼
                                  se guarda una fila en la tabla "inscripciones"
                                                   │
                         ActividadMapper convierte la fila en un JSON de respuesta
```

---

# PARTE 1 — Las entidades (las tablas)

Una **entidad** es una clase Java que representa una **tabla** de la base de datos. Cada objeto de la clase es una **fila**; cada atributo es una **columna**.

### Anotaciones que se repiten en las tres entidades

| Anotación | Qué significa, en fácil |
|---|---|
| `@Entity` | "Esta clase es una tabla" |
| `@Table(name = "...")` | El nombre de la tabla en PostgreSQL (y aquí también se declaran restricciones e índices) |
| `@Id` | "Este atributo es la clave primaria" (el número que identifica cada fila) |
| `@GeneratedValue(strategy = IDENTITY)` | "El número lo genera la base de datos sola": 1, 2, 3… (autoincremental) |
| `@Column(...)` | Ajustes de la columna: `nullable = false` (obligatoria), `length = 200` (máximo 200 caracteres), `unique = true` (no repetir) |
| `@Getter` `@Setter` | De **Lombok**: crea automáticamente los métodos `getTitulo()`, `setTitulo(...)`, etc., para no escribirlos a mano |
| `@NoArgsConstructor` | De Lombok: crea un constructor vacío, que JPA necesita para poder crear objetos al leer de la base de datos |

---

## 1. `Etiqueta` (la más sencilla)

**Qué es:** una palabra clave que se pone a las actividades (programación, empresa, seguridad…).

```java
@Entity
@Table(name = "etiquetas")
public class Etiqueta {
  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 80)
  private String nombre;

  @ManyToMany(mappedBy = "etiquetas")
  private Set<Actividad> actividades = new HashSet<>();
}
```

**Parte por parte:**

- `id`: identificador numérico que genera la base de datos.
- `nombre`: obligatorio (`nullable = false`), **no se puede repetir** (`unique = true`) y como máximo 80 caracteres. Esto es lo que impide dos etiquetas con el mismo nombre, **aunque alguien lo intente con `psql`**.
- `actividades`: la lista de actividades que tienen esta etiqueta.
  - `@ManyToMany` = "muchos a muchos": una etiqueta está en muchas actividades y una actividad tiene muchas etiquetas.
  - `mappedBy = "etiquetas"` = "yo **no mando** en esta relación; quien manda es el atributo `etiquetas` de la clase `Actividad`". Es el **lado inverso**: solo sirve para poder preguntar "¿qué actividades tienen esta etiqueta?".
  - Es un `Set` (conjunto) y no una lista porque **un conjunto no admite repetidos**.

**Para la defensa:** *"Etiqueta tiene el nombre único en la base de datos y es el lado inverso de la relación muchos a muchos con Actividad."*

---

## 2. `Actividad`

**Qué es:** una actividad del centro (taller, charla, visita…).

```java
@Entity
@Table(name = "actividades",
       indexes = @Index(name = "idx_actividad_docente", columnList = "docente_id"))
@Check(name = "ck_actividad_max_participantes", constraints = "max_participantes > 0")
public class Actividad {
  @Id @GeneratedValue(strategy = IDENTITY)   private Long id;
  @Column(nullable = false, length = 200)    private String titulo;
  @Column(nullable = false, columnDefinition = "text") private String descripcion;
  @Column(nullable = false, length = 50)     private String tipo;
  @Column(name = "fecha_prevista", nullable = false) private LocalDate fechaPrevista;
  @Column(name = "max_participantes", nullable = false) private Integer maxParticipantes;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "docente_id", nullable = false)
  private Docente docente;

  @ManyToMany
  @JoinTable(name = "actividad_etiqueta", ... )
  private Set<Etiqueta> etiquetas = new HashSet<>();

  @OneToMany(mappedBy = "actividad")
  private List<Inscripcion> inscripciones = new ArrayList<>();
}
```

**Parte por parte:**

- **`@Table(... indexes = @Index(...))`**: crea un **índice** sobre `docente_id`. Un índice es como el índice de un libro: permite encontrar filas rápido sin leer toda la tabla.
- **`@Check(... "max_participantes > 0")`**: crea una regla en la base de datos: **no se puede guardar una actividad con 0 plazas o menos**. Lo probé en `psql` y la BD lo rechaza.
- **`titulo`**: máximo 200 caracteres y obligatorio.
- **`descripcion`** con `columnDefinition = "text"`: tipo `text` de PostgreSQL, que admite textos largos (el enunciado pide un tipo adecuado para textos largos).
- **`fechaPrevista`** (`LocalDate`): una fecha sin hora. `@Column(name = "fecha_prevista")` hace que en la base de datos se llame `fecha_prevista` (con guion bajo, estilo SQL) aunque en Java sea `fechaPrevista`.
- **`docente`** — relación **muchos a uno**:
  - `@ManyToOne`: muchas actividades pueden tener el mismo docente; cada actividad tiene **un** docente.
  - `optional = false` + `nullable = false`: **no puede haber actividad sin docente**.
  - `@JoinColumn(name = "docente_id")`: en la tabla `actividades` hay una columna `docente_id` que es una **clave foránea** (apunta a la tabla `docente`). Si pones un docente que no existe, la base de datos lo rechaza.
  - `fetch = LAZY` ("perezoso"): al leer una actividad **no se carga el docente hasta que alguien lo pide**. Es más rápido, pero obliga a estar dentro de una transacción para leerlo.
  - Esta clase es el **lado propietario** (la tabla que contiene la clave foránea).
- **`etiquetas`** — relación **muchos a muchos**:
  - `@JoinTable(name = "actividad_etiqueta", ...)` define la **tabla intermedia** con dos columnas (`actividad_id`, `etiqueta_id`).
  - `uniqueConstraints`: la pareja `(actividad_id, etiqueta_id)` **no se puede repetir**. Así no se duplica la relación.
  - Aquí es el **lado propietario** (es la que manda sobre la tabla intermedia).
- **`inscripciones`**: lista de inscripciones de la actividad. `@OneToMany(mappedBy = "actividad")` = "una actividad tiene muchas inscripciones; la clave foránea está en `Inscripcion.actividad`, no aquí". Es el lado inverso: **no crea ninguna columna**, solo permite navegar de actividad a sus inscripciones.

**Para la defensa:** *"Actividad guarda sus datos con restricciones en la base de datos: título y descripción obligatorios, plazas mayores que cero con un CHECK, docente obligatorio con clave foránea, y una tabla intermedia con clave única para las etiquetas."*

---

## 3. `Inscripcion`

**Qué es:** el "apunte" de un estudiante en una actividad. Une dos cosas y además guarda la fecha.

```java
@Entity
@Table(name = "inscripciones",
  uniqueConstraints = @UniqueConstraint(name = "uk_inscripcion_actividad_estudiante",
                                        columnNames = {"actividad_id", "estudiante_id"}),
  indexes = @Index(name = "idx_inscripcion_estudiante", columnList = "estudiante_id"))
public class Inscripcion {
  @Id @GeneratedValue(strategy = IDENTITY) private Long id;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "actividad_id", nullable = false)
  private Actividad actividad;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "estudiante_id", nullable = false)
  private Estudiante estudiante;

  @Column(name = "fecha_inscripcion", nullable = false)
  private LocalDateTime fechaInscripcion;
}
```

**Parte por parte:**

- **`@UniqueConstraint(columnNames = {"actividad_id","estudiante_id"})`**: la **pareja** actividad+estudiante no puede repetirse. Es lo que impide inscribirse dos veces en la misma actividad. Ojo: no es que cada columna sea única por separado (un estudiante puede estar en muchas actividades), sino **la combinación**.
- **`@Index` por `estudiante_id`**: acelera el historial de un estudiante ("todas sus actividades").
- **Dos `@ManyToOne`** (a `Actividad` y a `Estudiante`): esta clase tiene las **dos claves foráneas**, así que es el lado propietario de ambas relaciones. Las dos son obligatorias: no puede existir una inscripción de un estudiante o actividad inexistentes.
- **`fechaInscripcion`** (`LocalDateTime`, fecha y hora): cuándo se hizo la inscripción.

**¿Por qué es una clase propia y no un simple muchos a muchos?** Porque guarda un dato propio (la fecha). Un `@ManyToMany` simple no puede guardar datos extra.

**Para la defensa:** *"Inscripcion es una entidad propia con dos claves foráneas y una restricción de unicidad sobre la pareja actividad-estudiante, para que la base de datos impida duplicados."*

---

# PARTE 2 — Los repositorios (el almacén)

Un **repositorio** es una interfaz que se encarga de hablar con la base de datos. Lo mágico: al escribir `extends JpaRepository<Entidad, Long>`, Spring **crea solo el código** de las operaciones habituales, sin escribir una línea:

| Método gratis | Qué hace |
|---|---|
| `save(x)` / `saveAndFlush(x)` | Guarda (`saveAndFlush` lo envía a la base de datos al momento) |
| `findById(id)` | Busca por id (devuelve un `Optional`: puede estar o no) |
| `existsById(id)` | ¿Existe? Sí/no |
| `findAll()`, `count()`, `deleteById(id)` | Listar todo, contar, borrar |

`JpaRepository<Actividad, Long>` = "repositorio de `Actividad`, cuyo id es de tipo `Long`".

Además, Spring entiende **el nombre de un método** y crea la consulta. Se llaman *consultas derivadas*:

`existsByActividadIdAndEstudianteId` se lee como: *existe* (`exists`) *por* (`By`) *actividad.id* **Y** (`And`) *estudiante.id*.

---

## 4. `EtiquetaRepository`

```java
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
  boolean existsByNombreIgnoreCase(String nombre);
}
```

- `existsByNombreIgnoreCase`: "¿existe una etiqueta con este nombre, **sin importar mayúsculas**?" (`IgnoreCase`). Así "Seguridad" y "seguridad" cuentan como la misma. Lo uso al crear etiquetas para devolver un 409 claro.

## 5. `ActividadRepository`

```java
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select a from Actividad a where a.id = :id")
  Optional<Actividad> findByIdParaActualizar(@Param("id") Long id);

  List<Actividad> findByEtiquetasIdOrderById(Long etiquetaId);
}
```

- **`findByIdParaActualizar`**: busca la actividad **y bloquea su fila**.
  - `@Query("select a from Actividad a where a.id = :id")` es una consulta en **JPQL** (parecido a SQL pero habla de clases Java, no de tablas). `:id` es un parámetro.
  - `@Lock(PESSIMISTIC_WRITE)` hace que PostgreSQL ejecute `SELECT ... FOR UPDATE`: **nadie más puede modificar esa fila hasta que termine mi transacción**. Se usa para que dos personas no se coman la última plaza a la vez.
  - Se llama así a propósito (no `findById`) para que quede claro que **bloquea**.
- **`findByEtiquetasIdOrderById`**: "busca actividades cuyas etiquetas tengan este id, ordenadas por id". Spring lo traduce solo. Sirve para "actividades de una etiqueta".

## 6. `InscripcionRepository`

```java
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
  boolean existsByActividadIdAndEstudianteId(Long actividadId, Long estudianteId);
  long countByActividadId(Long actividadId);

  @Query("select i from Inscripcion i join fetch i.estudiante where i.actividad.id = :actividadId order by i.id")
  List<Inscripcion> findByActividadIdConEstudiante(Long actividadId);

  @Query("select i from Inscripcion i join fetch i.actividad a join fetch a.docente where i.estudiante.id = :estudianteId order by a.fechaPrevista, i.id")
  List<Inscripcion> findHistorialByEstudianteId(Long estudianteId);
}
```

| Método | Pregunta que responde |
|---|---|
| `existsByActividadIdAndEstudianteId` | ¿Este estudiante ya está inscrito en esta actividad? |
| `countByActividadId` | ¿Cuántas plazas hay ocupadas en esta actividad? |
| `findByActividadIdConEstudiante` | Dame las inscripciones de esta actividad **con el estudiante ya cargado** |
| `findHistorialByEstudianteId` | Dame las inscripciones de este estudiante **con la actividad y su docente ya cargados**, ordenadas por fecha de la actividad |

**¿Qué es `join fetch`?** Como las relaciones son `LAZY`, normalmente al recorrer 20 inscripciones para mostrar el estudiante de cada una, Java haría **1 consulta + 20 consultas más** (una por estudiante). Eso es el **problema N+1**. Con `join fetch` se trae todo **en una sola consulta**. Es como pedir en el almacén toda la lista de una vez en vez de ir 20 veces.

---

# PARTE 3 — El servicio (el cerebro)

## 7. `ActividadService` (la interfaz)

Es solo la **lista de operaciones** que sabe hacer mi servicio, sin código dentro:

```java
public interface ActividadService {
  ActividadResponse crearActividad(CreateActividadRequest request);
  ActividadResponse obtenerActividad(Long id);
  InscripcionResponse inscribir(Long actividadId, CreateInscripcionRequest request);
  List<EstudianteResponse> participantes(Long actividadId);
  List<ActividadHistorialResponse> historial(Long estudianteId);
  EtiquetaResponse crearEtiqueta(CreateEtiquetaRequest request);
  void asociarEtiqueta(Long actividadId, Long etiquetaId);
  List<ActividadResponse> actividadesPorEtiqueta(Long etiquetaId);
}
```

Es como el **menú** de un restaurante: dice qué se puede pedir, no cómo se cocina. Tiene las mismas firmas que `CampusHubService`, para que luego sea fácil repartir cada operación a su servicio.

Los tipos `...Request` son lo que **entra** (el JSON que envía el cliente) y los `...Response`, lo que **sale** (el JSON de respuesta). Son los DTO que ya venían hechos y no se tocan.

---

## 8. `ActividadServiceImpl` (donde está la lógica)

Es la clase más importante. `Impl` = "implementación": el código real de cada operación.

### Cabecera y constructor

```java
@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {
  private final ActividadRepository actividades;
  private final EtiquetaRepository etiquetas;
  private final DocenteRepository docentes;
  private final EstudianteRepository estudiantes;
  private final InscripcionRepository inscripciones;
  private final ActividadMapper mapper;
  public ActividadServiceImpl(...) { ... }
```

- **`@Service`**: le dice a Spring "esta clase es un servicio; créala tú".
- **`@Transactional`**: **cada método se ejecuta dentro de una transacción**. Una transacción es "todo o nada": si algo falla a mitad, se deshace todo y no queda nada a medias. También mantiene abierta la conexión para poder leer datos `LAZY`.
- **El constructor con los repositorios**: Spring me "inyecta" los repositorios y el mapper ya creados. No tengo que crearlos yo. Se llama *inyección de dependencias*.
- `docentes` y `estudiantes` son repositorios **de mis compañeros** (Álvaro); los uso para comprobar que existen.

### Operación: `crearActividad`

Pasos:
1. `requireBody`: si no llega cuerpo → 400.
2. Valida cada campo: `texto(titulo, ..., 200)` (obligatorio y máx. 200), la fecha (formato `YYYY-MM-DD`), y que `maxParticipantes` sea mayor que 0 → si no, **400**.
3. Busca el docente por su id. Si no existe → **404**.
4. Crea el objeto `Actividad`, le pone los datos y lo guarda con `saveAndFlush`.
5. Devuelve la respuesta convertida con el mapper.

### Operación: `obtenerActividad`

Busca por id (si no existe → **404**) y la convierte a respuesta. Lleva `@Transactional(readOnly = true)` = "solo leo, no escribo", más eficiente.

### Operación: `inscribir` (la más importante)

```java
Actividad actividad = actividades.findByIdParaActualizar(actividadId)...   // 1. BLOQUEA la actividad (404 si no existe)
Estudiante estudiante = estudiantes.findById(request.estudianteId())...    // 2. ¿existe el estudiante? (404)
if (inscripciones.existsByActividadIdAndEstudianteId(...))  → 409           // 3. ¿ya inscrito?
if (inscripciones.countByActividadId(...) >= actividad.getMaxParticipantes()) → 409  // 4. ¿sin plazas?
// 5. crear la Inscripcion con la fecha de ahora y guardarla
```

La clave es el paso 1: al **bloquear** la fila de la actividad, si dos inscripciones llegan a la vez, la segunda espera a que termine la primera. Así el paso 4 (contar) siempre ve el dato real.

### Operaciones: `participantes` e `historial`

- `participantes(actividadId)`: comprueba que la actividad existe (404) y devuelve los estudiantes de sus inscripciones.
- `historial(estudianteId)`: comprueba que el estudiante existe (404) y devuelve sus actividades con la fecha de inscripción y el docente responsable.

### Operaciones de etiquetas

- **`crearEtiqueta`**: valida el nombre (obligatorio, máx. 80). Si ya existe → **409**. Si no, la guarda.
- **`asociarEtiqueta`**: busca la actividad y la etiqueta (404 si falta alguna) y hace `actividad.getEtiquetas().add(etiqueta)`. Como es un `Set`, **añadir una etiqueta que ya está no hace nada**: repetir la operación no da error ni duplica. Responde 204.
- **`actividadesPorEtiqueta`**: comprueba que la etiqueta existe (404) y devuelve sus actividades.

### Métodos de ayuda (al final de la clase)

| Método | Qué hace |
|---|---|
| `actividad(id)` | Busca una actividad o lanza 404 (para no repetir el mismo código) |
| `requireBody(x)` | Si no hay cuerpo en la petición → 400 |
| `texto(valor, campo, max)` | Comprueba que no esté vacío ni pase del máximo; devuelve el texto sin espacios sobrantes |
| `fecha(valor, campo)` | Convierte el texto `"2027-07-01"` en fecha; si el formato es malo → 400 |

### Las excepciones: cómo se convierten en códigos HTTP

Cuando algo está mal, el servicio **lanza una excepción** y la clase `ApiExceptionHandler` (ya venía hecha) la convierte en el código correcto:

| Excepción | Código HTTP | Se usa cuando… |
|---|---|---|
| `InvalidInputException` | 400 | El dato enviado es inválido |
| `ResourceNotFoundException` | 404 | Algo que se pide o referencia no existe |
| `BusinessConflictException` | 409 | Ya existe, ya inscrito o sin plazas |
| `DataIntegrityViolationException` (de Spring) | 409 | La base de datos rechazó algo (red de seguridad), sin mostrar SQL |

**Para la defensa:** *"El servicio valida, comprueba las reglas de negocio y lanza excepciones con el significado correcto. Además, la base de datos protege las mismas reglas como red de seguridad."*

---

# PARTE 4 — El mapper (el que emplata)

## 9. `ActividadMapper`

**Qué es:** convierte las **entidades** (objetos de la base de datos) en las **respuestas JSON** (DTO) que se envían al cliente.

¿Por qué no enviar la entidad directamente? Porque (1) el contrato de la API fija exactamente qué campos devolver, (2) la entidad tiene relaciones `LAZY` que no se pueden serializar sin problemas y (3) así no exponemos detalles internos.

```java
@Component
public class ActividadMapper {
  public ActividadResponse toResponse(Actividad a)  → id, titulo, descripcion, tipo, fechaPrevista (texto), maxParticipantes, docenteId
  public EtiquetaResponse toResponse(Etiqueta e)    → id, nombre
  public InscripcionResponse toResponse(Inscripcion i) → id, actividadId, estudianteId, fechaInscripcion
  public ActividadHistorialResponse toHistorial(Inscripcion i) → actividad + fecha de inscripción + docente responsable
  public EstudianteResponse toEstudiante(Inscripcion i) → el estudiante de esa inscripción
}
```

- **`@Component`**: Spring crea una instancia y la ofrece al servicio.
- Convierte las fechas a texto (`getFechaPrevista().toString()` → `"2027-07-01"`), como exige el contrato.
- **`toResponse` está repetido con distintos tipos**: se llama igual pero Java elige el correcto según lo que le pases. Se llama *sobrecarga*.
- Para el docente y el estudiante **reutiliza el `OrganizacionMapper` de Álvaro** (por eso recibe uno en el constructor), en vez de copiar su código.
- Debe usarse **dentro de una transacción**, porque lee relaciones `LAZY` (como `a.getDocente().getId()`).

---

# PARTE 5 — Clases de mis compañeros que mi código usa

No las escribí yo, pero mi parte depende de ellas:

| Clase | De quién | Para qué la uso |
|---|---|---|
| `Docente` | Álvaro | Es el responsable de una actividad (`Actividad.docente`) |
| `Estudiante` | Álvaro | Es quien se inscribe (`Inscripcion.estudiante`). Activé su lista `inscripciones` (lado inverso) |
| `DocenteRepository`, `EstudianteRepository` | Álvaro | Para comprobar que existen antes de guardar |
| `OrganizacionMapper` | Álvaro | Convierte docente y estudiante a JSON |
| DTO (`ActividadResponse`, `CreateActividadRequest`, etc.) | Profesor | El formato exacto de entrada y salida; **no se tocan** |
| `ApiExceptionHandler` y las excepciones | Profesor | Convierten excepciones en códigos 400/404/409 |
| `CampusHubService` / `CampusHubServiceImpl` | Profesor (se completará entre los tres) | Los controllers llaman aquí; cada operación debe repartirse al servicio de su dueño |

---

# Resumen para repasar antes de la defensa

| Clase | Una frase |
|---|---|
| `Etiqueta` | Palabra clave con nombre único; lado inverso del muchos a muchos. |
| `Actividad` | Datos de la actividad + CHECK de plazas + docente obligatorio + etiquetas (lado propietario). |
| `Inscripcion` | Une estudiante y actividad, guarda la fecha y es única por pareja. |
| `EtiquetaRepository` | Comprueba si un nombre de etiqueta ya existe. |
| `ActividadRepository` | Busca bloqueando la fila (aforo) y busca actividades por etiqueta. |
| `InscripcionRepository` | Duplicados, plazas ocupadas, participantes e historial. |
| `ActividadService` | La lista de operaciones. |
| `ActividadServiceImpl` | Valida, aplica las reglas y decide 400/404/409. |
| `ActividadMapper` | Convierte entidades en las respuestas JSON del contrato. |
