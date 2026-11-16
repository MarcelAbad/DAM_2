// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// ─────────────────────────────────────────────────────────────────────────────
// QUÉ ES ESTE ARCHIVO (léelo entero, es corto)
//
// Checker es el "comprobador" de los 16 ejercicios evaluables (E01 a E16).
//
// ¿Para qué sirve? Para que un alumno pueda pulsar un botón dentro de la app
// (en la pantalla Academia) y ver inmediatamente si su ejercicio está:
//   - PENDIENTE: todavía tiene el TODO sin resolver (no pasa nada, es el estado inicial).
//   - CORRECTO: la función devuelve exactamente lo que pide el enunciado.
//   - INCORRECTO: la función compila pero devuelve algo equivocado.
//
// ¿Cómo lo hace? Ejecutando la función REAL del alumno con casos concretos:
// casos normales, casos límite y casos que DEBEN ser rechazados (errores).
// Es exactamente la misma lógica que usan los tests E01Test...E16Test,
// pero escrita sin JUnit para poder usarse dentro de la aplicación.
//
// Este archivo es Kotlin puro: NO usa nada de Android, así que funciona igual
// en la app, en los tests del PC y en la máquina de Java del profesor.
// ─────────────────────────────────────────────────────────────────────────────

// Los tres estados posibles de un ejercicio. Un enum es como una lista cerrada
// de opciones: solo puede valer una de estas tres, nunca otra cosa.
enum class CheckState {
    // El alumno aún no ha implementado el ejercicio: la función lanza TODO().
    PENDIENTE,
    // Todos los casos de prueba han pasado: el ejercicio cumple su contrato.
    CORRECTO,
    // La función está implementada pero al menos un caso falla.
    INCORRECTO
}

// Resultado de UN caso de prueba concreto (por ejemplo: "lote vacío se rechaza").
data class CaseOutcome(
    // Nombre legible del caso, se muestra al alumno: describe QUÉ se comprueba.
    val name: String,
    // true si el caso no se pudo evaluar porque el ejercicio sigue en TODO.
    val pending: Boolean = false,
    // true si el caso se superó (el resultado fue el esperado).
    val ok: Boolean = false,
    // Explicación del resultado: "Correcto", el fallo concreto, o el aviso de pendiente.
    val detail: String = ""
)

// Resultado completo de comprobar UN ejercicio: su número, su estado y todos sus casos.
data class CheckResult(
    // Número del ejercicio, del 1 al 16 (coincide con E01...E16).
    val exercise: Int,
    // Título oficial del ejercicio (el mismo que se ve en Academia).
    val title: String,
    // Estado global: pendiente, correcto o incorrecto.
    val state: CheckState,
    // La lista de casos individuales con su resultado, uno por uno.
    val cases: List<CaseOutcome>,
    // Qué cosas NO puede comprobar este archivo y hay que probar en un móvil real.
    // Es null cuando todo se puede comprobar automáticamente.
    val deviceNote: String? = null
)

// Objeto que centraliza toda la lógica de comprobación.
// Un `object` en Kotlin es una clase de la que solo existe UNA copia (un singleton).
object Checker {

    // ─────────────────────────────────────────────────────────────────────────
    // HERRAMIENTAS INTERNAS (las usan las 16 comprobaciones de abajo)
    // ─────────────────────────────────────────────────────────────────────────

    // Ejecuta un caso de prueba y clasifica lo que ha pasado.
    // `action` es el código del caso: dentro se llama a la función del alumno
    // y se comprueba el resultado con `expect` o `rejects`.
    fun run(name: String, action: () -> Unit): CaseOutcome = try {
        // Ejecutamos el caso. Si llega hasta aquí sin lanzar nada, el caso pasa.
        action()
        // El caso se superó: lo marcamos como correcto.
        CaseOutcome(name = name, ok = true, detail = "Correcto")
    } catch (e: NotImplementedError) {
        // El alumno dejó el TODO: no es un error del proyecto, es el estado inicial.
        // Guardamos el mensaje del TODO para que se vea qué ejercicio falta.
        CaseOutcome(name = name, pending = true, detail = "Pendiente: ${e.message}")
    } catch (e: AssertionError) {
        // Una comprobación de expect/rejects ha fallado: el resultado no era el esperado.
        // Se guarda el mensaje para que el alumno sepa QUÉ ha fallado exactamente.
        CaseOutcome(name = name, ok = false, detail = "Fallo: ${e.message}")
    } catch (e: Exception) {
        // La función está implementada pero lanzó otra excepción (validación, etc.).
        // También se clasifica como fallo del caso, nunca cierra la aplicación.
        CaseOutcome(name = name, ok = false, detail = "Fallo: ${e.message}")
    }

    // Comprueba que una condición sea verdadera. Si no lo es, lanza un error
    // con un mensaje claro. Es el "assertEquals" casero, sin JUnit.
    private fun expect(condition: Boolean, message: String) {
        // Si la condición es falsa, lanzamos una excepción con la explicación.
        // `run` la capturará y la mostrará como fallo del caso.
        if (!condition) throw AssertionError(message)
    }

    // Comprueba que un bloque de código rechace la entrada con un error controlado.
    // Se usa para los casos en los que el enunciado dice "esto debe rechazarse".
    // Ejemplo: añadir un lote vacío de jugadores NO es válido y debe fallar.
    // Admite dos familias de rechazo: validaciones (require) y JSON corrupto.
    private fun rejects(action: () -> Unit) {
        // Ejecutamos el bloque esperando que lance una excepción de rechazo.
        val thrown: Exception? = try {
            action()
            // Si llega aquí, NO lanzó ninguna excepción: eso es un fallo.
            null
        } catch (e: IllegalArgumentException) {
            // Lanzó una validación require: exactamente lo esperado.
            e
        } catch (e: org.json.JSONException) {
            // El texto no era un JSON válido: también es un rechazo controlado.
            e
        }
        // Si no se lanzó ninguna excepción esperada, el caso falla con este mensaje.
        if (thrown == null) throw AssertionError("Se esperaba un rechazo (validación) y no ocurrió")
    }

    // Decide el estado global de un ejercicio a partir de sus casos individuales.
    // Es una función pura: no toca nada de Android ni de la interfaz.
    // Esta separación permite probar al propio comprobador en CheckerTest.
    fun verdict(cases: List<CaseOutcome>): CheckState = when {
        // Si algún caso está pendiente (TODO sin resolver), el ejercicio está pendiente.
        cases.any { it.pending } -> CheckState.PENDIENTE
        // Si algún caso falló, el ejercicio es incorrecto (aunque compile).
        cases.any { !it.ok } -> CheckState.INCORRECTO
        // Todos los casos pasaron: el ejercicio es correcto.
        else -> CheckState.CORRECTO
    }

    // Punto de entrada principal: comprueba el ejercicio número `n` (del 1 al 16).
    fun check(n: Int): CheckResult = when (n) {
        1 -> check01(); 2 -> check02(); 3 -> check03(); 4 -> check04()
        5 -> check05(); 6 -> check06(); 7 -> check07(); 8 -> check08()
        9 -> check09(); 10 -> check10(); 11 -> check11(); 12 -> check12()
        13 -> check13(); 14 -> check14(); 15 -> check15(); 16 -> check16()
        // Cualquier otro número es un error del programador, no del alumno.
        else -> error("Ejercicio desconocido: $n")
    }

    // Comprueba los 16 ejercicios seguidos. Lo usa el botón "comprobar todo".
    fun all(): List<CheckResult> = (1..16).map { check(it) }

    // ─────────────────────────────────────────────────────────────────────────
    // LAS 16 COMPROBACIONES (una por ejercicio evaluable)
    //
    // Cada función: llama a la función REAL del alumno con casos concretos,
    // los clasifica con `run` y devuelve un CheckResult completo.
    // Los casos son los mismos contratos que los tests E01Test...E16Test.
    // ─────────────────────────────────────────────────────────────────────────

    // E01: añadir equipos. El contrato: sumar el lote, rechazar lotes vacíos,
    // IDs repetidos, dos clubes mezclados y jugadores duplicados.
    private fun check01(): CheckResult {
        // Lista de casos: cada `run` ejecuta y clasifica un caso.
        val cases = listOf(
            // Caso normal: 16 jugadores + lote del Athletic (5) = 21 en total.
            run("Añadir el lote del Athletic deja 21 jugadores") {
                expect(E01Catalogo.addTeam(Seed.players, Seed.extra).size == 21, "Se esperaban 21 jugadores tras añadir el lote")
            },
            // Caso límite: un lote vacío no tiene sentido y debe rechazarse.
            run("Un lote vacío se rechaza") {
                rejects { E01Catalogo.addTeam(Seed.players, emptyList()) }
            },
            // Caso de error: reutilizar un ID existente debe rechazarse.
            run("Un ID repetido se rechaza") {
                rejects { E01Catalogo.addTeam(Seed.players, listOf(Seed.players.first())) }
            },
            // Caso de error: el enunciado exige UN solo club por lote.
            run("Mezclar dos clubes en un lote se rechaza") {
                rejects { E01Catalogo.addTeam(Seed.players, listOf(Seed.extra.first(), Seed.atleti.first())) }
            },
            // Caso de error: mismo jugador con otro ID también es un duplicado.
            run("Un jugador duplicado con otro ID se rechaza") {
                rejects { E01Catalogo.addTeam(Seed.players, listOf(Seed.players[0].copy(id = 99))) }
            }
        )
        // Devuelve el resultado con el número, el título oficial y el veredicto.
        return CheckResult(1, Academy.titles[0], verdict(cases), cases)
    }

    // E02: buscar, filtrar y ordenar. El contrato: texto sin acentos, filtros
    // combinados, solo favoritos y orden ascendente/descendente por nombre.
    private fun check02(): CheckResult {
        // Los casos usan el catálogo inicial (16 jugadores) como datos de prueba.
        val cases = listOf(
            // "MBAPPE" (mayúsculas y sin acento) debe encontrar a Kylian Mbappé (id 7).
            run("Buscar MBAPPE encuentra a Mbappé") {
                expect(E02Filtros.filter(Seed.players, "MBAPPE", "", "", emptySet(), false, false).single().id == 7, "Se esperaba encontrar al jugador con id 7")
            },
            // Un texto que no existe nadie debe dejar la lista completamente vacía.
            run("Buscar un texto inexistente deja la lista vacía") {
                expect(E02Filtros.filter(Seed.players, "nadie", "", "", emptySet(), false, false).isEmpty(), "Se esperaba una lista vacía")
            },
            // Filtros combinados: club + posición a la vez.
            run("Filtrar por club y posición a la vez") {
                // Los dos delanteros del Barça son Lamine Yamal y Raphinha, en ese orden alfabético.
                expect(E02Filtros.filter(Seed.players, "", "FC Barcelona", "Delantero", emptySet(), false, false).map { it.name } == listOf("Lamine Yamal", "Raphinha"), "Se esperaban los delanteros del FC Barcelona")
            },
            // Solo favoritos: si solo es favorito el id 1, la lista es solo ese jugador.
            run("Solo favoritos muestra únicamente los favoritos") {
                expect(E02Filtros.filter(Seed.players, "", "", "", setOf(1), true, false).map { it.id } == listOf(1), "Se esperaba solo el jugador favorito")
            },
            // Orden: descendente es exactamente la lista ascendente dada la vuelta.
            run("El orden descendente invierte el orden por nombre") {
                // Calculamos primero el orden ascendente de referencia.
                val asc = E02Filtros.filter(Seed.players, "", "", "", emptySet(), false, false)
                // Y comprobamos que bajar invierte esa misma lista.
                expect(E02Filtros.filter(Seed.players, "", "", "", emptySet(), false, true) == asc.reversed(), "La lista descendente no es la ascendente invertida")
            }
        )
        return CheckResult(2, Academy.titles[1], verdict(cases), cases)
    }

    // E03: etiquetas y valoración personal. El contrato: etiquetas únicas
    // (sin repetir aunque cambien mayúsculas/acentos), máximo 5, valoración 0-5.
    private fun check03(): CheckResult {
        // Partimos de una marca con nota para comprobar que no se pierde.
        val base = Mark(note = "Conservar")
        val cases = listOf(
            // "Pase" y " pase " son la misma etiqueta: deben quedar en una sola.
            run("Las etiquetas repetidas se unifican en una") {
                expect(E03Etiquetas.mark(Mark(), listOf("Pase", " pase "), 3).tags.size == 1, "Se esperaba una sola etiqueta tras quitar repetidas")
            },
            // La valoración es personal y solo admite el rango de 0 a 5.
            run("Una valoración de 6 se rechaza") {
                rejects { E03Etiquetas.mark(Mark(), emptyList(), 6) }
            },
            // También por debajo del rango: -1 no es válido.
            run("Una valoración de -1 se rechaza") {
                rejects { E03Etiquetas.mark(Mark(), emptyList(), -1) }
            },
            // Más de 5 etiquetas distintas no caben: se rechaza.
            run("Más de cinco etiquetas se rechazan") {
                rejects { E03Etiquetas.mark(Mark(), listOf("a", "b", "c", "d", "e", "f"), 3) }
            },
            // Guardar etiquetas no debe borrar el resto de datos personales.
            run("La nota existente se conserva al guardar etiquetas") {
                expect(E03Etiquetas.mark(base, emptyList(), 0).note == "Conservar", "La nota original se perdió")
            }
        )
        return CheckResult(3, Academy.titles[2], verdict(cases), cases)
    }

    // E04: comparar fichas. El contrato: entre 2 y 4 jugadores distintos,
    // comparando identidad y posición sin inventar datos.
    private fun check04(): CheckResult {
        val cases = listOf(
            // Caso normal: comparar 2 jugadores menciona sus nombres en el texto.
            run("Comparar dos jugadores incluye sus nombres") {
                expect(E04Comparador.compare(Seed.players.take(2)).contains("Courtois"), "El texto del comparador no menciona a Courtois")
            },
            // Un jugador repetido dos veces no tiene sentido y se rechaza.
            run("Comparar el mismo jugador dos veces se rechaza") {
                rejects { E04Comparador.compare(listOf(Seed.players.first(), Seed.players.first())) }
            },
            // Sin jugadores no hay nada que comparar.
            run("Comparar una lista vacía se rechaza") {
                rejects { E04Comparador.compare(emptyList()) }
            },
            // Con un solo jugador tampoco: el mínimo son dos.
            run("Comparar un solo jugador se rechaza") {
                rejects { E04Comparador.compare(listOf(Seed.players.first())) }
            }
        )
        return CheckResult(4, Academy.titles[3], verdict(cases), cases)
    }

    // E05: sorpresa respetando filtros. El contrato: elegir con un índice que
    // da la vuelta (circular), y devolver null si no hay candidatos.
    // La sacudida real del acelerómetro solo se puede probar en un móvil.
    private fun check05(): CheckResult {
        val cases = listOf(
            // El índice 16 sobre 16 jugadores "da la vuelta" y devuelve el primero (id 1).
            run("El índice 16 sobre 16 jugadores da la vuelta al primero") {
                expect(E05Sensor.choose(Seed.players, 16)?.id == 1, "Se esperaba el id 1 al dar la vuelta")
            },
            // El índice -1 también da la vuelta, pero hacia atrás: devuelve el último (id 16).
            run("El índice -1 devuelve al último jugador") {
                expect(E05Sensor.choose(Seed.players, -1)?.id == 16, "Se esperaba el id 16 con índice -1")
            },
            // Sin candidatos no se puede elegir nadie: se devuelve null, no un error.
            run("Una lista vacía devuelve null") {
                expect(E05Sensor.choose(emptyList(), 0) == null, "Se esperaba null sin candidatos")
            },
            // La sorpresa debe salir solo de la lista visible (respetar filtros).
            run("La sorpresa sale solo de la lista recibida") {
                // Pasamos solo jugadores del Barça: la respuesta debe ser uno de ellos.
                val barca = Seed.players.filter { it.club == "FC Barcelona" }
                expect(barca.contains(E05Sensor.choose(barca, 3)), "El elegido no pertenece a la lista recibida")
            }
        )
        // deviceNote: lo que hay que comprobar manualmente en un dispositivo.
        return CheckResult(5, Academy.titles[4], verdict(cases), cases,
            "Activa la casilla de agitar en el catálogo y comprueba en tu móvil que la ficha sorpresa respeta los filtros visibles.")
    }

    // E06: filtro de contraste reversible. El contrato: valores de 0 a 3,
    // rechazar NaN, y calcular el par (escala, desplazamiento) correcto.
    private fun check06(): CheckResult {
        val cases = listOf(
            // Contraste neutro (1): el desplazamiento debe ser 0.
            run("Contraste 1 deja el desplazamiento en 0") {
                expect(Math.abs(E06Contraste.parameters(1f).second - 0f) < 0.001f, "Se esperaba desplazamiento 0")
            },
            // Contraste mínimo (0): el desplazamiento debe ser 128 (el valor estándar).
            run("Contraste 0 usa desplazamiento 128") {
                expect(Math.abs(E06Contraste.parameters(0f).second - 128f) < 0.001f, "Se esperaba desplazamiento 128")
            },
            // NaN ("no es un número") no es un valor válido y se rechaza.
            run("Un valor NaN se rechaza") {
                rejects { E06Contraste.parameters(Float.NaN) }
            },
            // Fuera del rango 0..3 también se rechaza.
            run("Un contraste de 4 se rechaza") {
                rejects { E06Contraste.parameters(4f) }
            }
        )
        return CheckResult(6, Academy.titles[5], verdict(cases), cases)
    }

    // E07: imagen persistente por jugador. El contrato: nombres de archivo
    // seguros (jugador_ID.png), sin rutas del usuario y con ID positivo.
    // La persistencia real (cerrar y reabrir la app) se prueba en un móvil.
    private fun check07(): CheckResult {
        val cases = listOf(
            // Caso normal: el nombre se construye con el ID y la extensión.
            run("El nombre de archivo es jugador_7.png") {
                expect(E07Imagen.filename(7, "png") == "jugador_7.png", "Se esperaba jugador_7.png")
            },
            // Una ruta con ../ intenta salir de la carpeta privada: se rechaza.
            run("Una ruta con ../ se rechaza") {
                rejects { E07Imagen.filename(1, "../x") }
            },
            // El ID 0 no identifica a ningún jugador y se rechaza.
            run("El ID 0 se rechaza") {
                rejects { E07Imagen.filename(0, "jpg") }
            },
            // Extensiones que no sean png o jpg no forman parte del contrato.
            run("Una extensión gif se rechaza") {
                rejects { E07Imagen.filename(1, "gif") }
            }
        )
        return CheckResult(7, Academy.titles[6], verdict(cases), cases,
            "En tu móvil: importa una imagen propia, pulsa conservar, cierra y reabre la app. La imagen debe reaparecer solo en esa ficha.")
    }

    // E08: playlist de audios. El contrato: moverse adelante/atrás dando la
    // vuelta (circular), dirección válida (-1 o 1) e índice dentro de rango.
    // La reproducción y grabación reales se prueban en un móvil.
    private fun check08(): CheckResult {
        val cases = listOf(
            // Anterior desde el primero de 3 audios salta al último (circular).
            run("Anterior desde el primero salta al último") {
                expect(E08Audio.next(0, 3, -1) == 2, "Se esperaba el índice 2")
            },
            // Siguiente desde el último de 3 audios salta al primero (circular).
            run("Siguiente desde el último salta al primero") {
                expect(E08Audio.next(2, 3, 1) == 0, "Se esperaba el índice 0")
            },
            // La dirección 0 no es ni anterior ni siguiente: se rechaza.
            run("La dirección 0 se rechaza") {
                rejects { E08Audio.next(0, 3, 0) }
            },
            // Un índice que no existe en la lista se rechaza.
            run("Un índice fuera de la lista se rechaza") {
                rejects { E08Audio.next(9, 3, 1) }
            }
        )
        return CheckResult(8, Academy.titles[7], verdict(cases), cases,
            "En tu móvil: graba dos comentarios de una ficha y comprueba anterior/siguiente; al salir sin guardar, la grabación se descarta.")
    }

    // E09: alineación 4-3-3. El contrato: exactamente 11 jugadores distintos
    // y con la distribución 1 portero, 4 defensas, 3 centrocampistas y 3 delanteros.
    private fun check09(): CheckResult {
        // Las 11 posiciones exactas que exige un 4-3-3, en orden.
        val positions = listOf("Portero") + List(4) { "Defensa" } + List(3) { "Centrocampista" } + List(3) { "Delantero" }
        // Un once válido de mentira (jugadores de prueba con IDs 9000+).
        val fixture = positions.mapIndexed { i, p -> Player(9000 + i, "Fixture $i", "Club de prueba", p, "https://example.test") }
        val cases = listOf(
            // Los primeros 11 del catálogo NO forman un 4-3-3 válido (faltan piezas).
            run("Once jugadores mal distribuidos no son una alineación válida") {
                expect(!E09Alineacion.validate(Seed.players.take(11)), "Once con distribución incorrecta dio como válido")
            },
            // Una lista vacía nunca puede ser una alineación.
            run("Una lista vacía no es una alineación válida") {
                expect(!E09Alineacion.validate(emptyList()), "La lista vacía dio como válida")
            },
            // El once de prueba con la distribución exacta SÍ debe validarse.
            run("Un once 4-3-3 exacto es válido") {
                expect(E09Alineacion.validate(fixture), "Un once 4-3-3 correcto dio como inválido")
            },
            // Repetir un jugador (mismo ID) rompe la alineación aunque haya 11 huecos.
            run("Un once con un jugador repetido no es válido") {
                val broken = fixture.toMutableList()
                // Duplicamos al portero en el hueco de un defensa.
                broken[1] = broken[0]
                expect(!E09Alineacion.validate(broken), "Un once con jugador repetido dio como válido")
            }
        )
        return CheckResult(9, Academy.titles[8], verdict(cases), cases)
    }

    // E10: colecciones. El contrato: nombres limpios (sin espacios), sin
    // repetir aunque cambien acentos, y el nombre vacío se rechaza.
    private fun check10(): CheckResult {
        val cases = listOf(
            // " B " con espacios se limpia y se añade a la lista existente.
            run("Un nombre con espacios se limpia y se añade") {
                expect(E10Colecciones.add(listOf("A"), " B ") == listOf("A", "B"), "Se esperaba [A, B]")
            },
            // "Técnica" ya existe (sin acento): es un duplicado y se rechaza.
            run("Un nombre repetido aunque cambien los acentos se rechaza") {
                rejects { E10Colecciones.add(listOf("Tecnica"), "Técnica") }
            },
            // Un nombre de solo espacios no es un nombre y se rechaza.
            run("Un nombre en blanco se rechaza") {
                rejects { E10Colecciones.add(emptyList(), "   ") }
            }
        )
        return CheckResult(10, Academy.titles[9], verdict(cases), cases)
    }

    // E11: copia de seguridad. El contrato: restaurar favoritos y notas desde
    // el JSON de la copia, rechazar JSON corrupto y conservar lo que ya había.
    private fun check11(): CheckResult {
        // Unos datos personales ya guardados que deben conservarse.
        val old = mapOf(2 to Mark(true, "Conservar"))
        val cases = listOf(
            // Caso normal: exportamos un favorito y lo restauramos en otra "base".
            run("Restaurar una copia devuelve favorito y nota") {
                expect(E11Copia.restore(emptyMap(), Wire.encode(mapOf(1 to Mark(true, "n"))), setOf(1))[1]?.note == "n", "No se restauró la nota de la copia")
            },
            // Un texto que no es JSON debe rechazarse con error, sin cerrar nada.
            run("Un texto que no es JSON se rechaza") {
                rejects { E11Copia.restore(emptyMap(), "no-json", setOf(1)) }
            },
            // La restauración es parcial: los datos previos que no están en la
            // copia se conservan (no se borra nada).
            run("Lo que ya existía y no está en la copia se conserva") {
                expect(E11Copia.restore(old, Wire.encode(mapOf(1 to Mark(false, "Nueva"))), setOf(1, 2))[2]?.note == "Conservar", "Se perdió un dato anterior a la copia")
            }
        )
        return CheckResult(11, Academy.titles[10], verdict(cases), cases)
    }

    // E12: datos sin conexión (caché). El contrato: si la descarga falla o no
    // hay datos nuevos, se conservan los guardados (stale = caducado).
    private fun check12(): CheckResult {
        val cases = listOf(
            // Sin datos remotos y con error: los guardados se conservan.
            run("Sin red se conservan los datos guardados") {
                expect(E12Cache.resolve(null, Seed.players, "error").stale, "Se esperaba stale=true sin red")
            },
            // Una lista remota vacía es válida pero no sustituye nada: no es stale.
            run("Una respuesta vacía no se marca como caducada") {
                expect(!E12Cache.resolve(emptyList(), Seed.players, null).stale, "Se esperaba stale=false con respuesta vacía")
            },
            // Sin red y sin nada guardado, la lista visible queda vacía.
            run("Sin red y sin datos guardados la lista queda vacía") {
                expect(E12Cache.resolve(null, emptyList(), "sin red").rows.isEmpty(), "Se esperaba una lista vacía")
            }
        )
        return CheckResult(12, Academy.titles[11], verdict(cases), cases)
    }

    // E13: cuestionario. El contrato: preguntas con 2 opciones distintas,
    // la respuesta correcta entre las opciones, y jugadores sin repetir.
    private fun check13(): CheckResult {
        val cases = listOf(
            // Cinco preguntas de cinco jugadores distintos.
            run("Cinco preguntas usan cinco jugadores distintos") {
                val q = E13Quiz.build(Seed.players, 5)
                expect(q.map { it.playerId }.distinct().size == 5, "Se repetía un jugador en las preguntas")
            },
            // Cada pregunta tiene exactamente 2 opciones y la respuesta está entre ellas.
            run("Cada pregunta tiene dos opciones y la respuesta está entre ellas") {
                val q = E13Quiz.build(Seed.players, 5)
                expect(q.all { it.options.distinct().size == 2 && it.answer in it.options }, "Hay una pregunta mal construida")
            },
            // Pedir más preguntas que jugadores es imposible: se rechaza.
            run("Pedir cinco preguntas con un solo jugador se rechaza") {
                rejects { E13Quiz.build(Seed.players.take(1), 5) }
            }
        )
        return CheckResult(13, Academy.titles[12], verdict(cases), cases)
    }

    // E14: cromo compartido. El contrato: título limpio de 1 a 28 caracteres,
    // color entre 0 y 2, y el texto vacío o demasiado largo se rechazan.
    private fun check14(): CheckResult {
        val cases = listOf(
            // El título se limpia de espacios por delante y por detrás.
            run("El título se limpia de espacios") {
                expect(E14Cromo.title(" Hola ", 0) == "Hola", "Se esperaba Hola")
            },
            // Un título vacío no se puede dibujar en el cromo.
            run("Un título vacío se rechaza") {
                rejects { E14Cromo.title("", 0) }
            },
            // Más de 28 caracteres no caben en el cromo.
            run("Un título de 29 caracteres se rechaza") {
                rejects { E14Cromo.title("x".repeat(29), 0) }
            },
            // Solo hay tres colores de fondo (0, 1, 2).
            run("Un color fuera de 0 a 2 se rechaza") {
                rejects { E14Cromo.title("Hola", 3) }
            }
        )
        return CheckResult(14, Academy.titles[13], verdict(cases), cases)
    }

    // E15: intercambio por Bluetooth. El contrato: decodificar el mensaje JSON
    // de la colección, validar IDs conocidos y rechazar duplicados o corruptos.
    // El envío real entre dos móviles solo se puede probar con dos dispositivos.
    private fun check15(): CheckResult {
        val cases = listOf(
            // Caso normal: el mensaje con ids 1 y 2 se decodifica tal cual.
            run("Un mensaje válido se decodifica en su lista de IDs") {
                expect(E15Bluetooth.decode("{\"version\":1,\"ids\":[1,2]}", setOf(1, 2)) == listOf(1, 2), "Se esperaba [1, 2]")
            },
            // Un mensaje sin los campos obligatorios se rechaza.
            run("Un mensaje sin versión ni ids se rechaza") {
                rejects { E15Bluetooth.decode("{}", setOf(1)) }
            },
            // Un ID repetido dentro del mensaje es un error de protocolo.
            run("Un mensaje con IDs repetidos se rechaza") {
                rejects { E15Bluetooth.decode("{\"version\":1,\"ids\":[1,1]}", setOf(1)) }
            },
            // Un ID que no existe en este móvil no se puede importar.
            run("Un mensaje con un ID desconocido se rechaza") {
                rejects { E15Bluetooth.decode("{\"version\":1,\"ids\":[999]}", setOf(1, 2)) }
            }
        )
        return CheckResult(15, Academy.titles[14], verdict(cases), cases,
            "El intercambio real necesita dos móviles emparejados: prueba el envío con ACK desde la pantalla de colecciones.")
    }

    // E16: panel personal. El contrato: contar favoritos por club, en orden
    // alfabético, sin duplicar jugadores y soportando la lista vacía.
    private fun check16(): CheckResult {
        val cases = listOf(
            // Caso normal: 8 jugadores del Real Madrid en el catálogo inicial.
            run("Cuenta 8 jugadores del Real Madrid") {
                expect(E16Panel.counts(Seed.players)["Real Madrid"] == 8, "Se esperaban 8 del Real Madrid")
            },
            // Sin jugadores el panel no pinta ninguna barra.
            run("Una lista vacía no produce cuentas") {
                expect(E16Panel.counts(emptyList()).isEmpty(), "Se esperaba un mapa vacío")
            },
            // Un jugador repetido dos veces solo cuenta una vez.
            run("Un jugador repetido solo cuenta una vez") {
                expect(E16Panel.counts(listOf(Seed.players[0], Seed.players[0]))["Real Madrid"] == 1, "El jugador repetido contó dos veces")
            }
        )
        return CheckResult(16, Academy.titles[15], verdict(cases), cases)
    }
}
