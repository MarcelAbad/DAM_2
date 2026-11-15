# Informe de validación de la entrega (edición 2.1)

## Qué se ha ejecutado realmente esta vez
A diferencia de la edición anterior (que solo pudo hacer comprobaciones estructurales), esta entrega se ha compilado y probado en un entorno real: JDK 17 (OpenJDK 17.0.20), Gradle 8.9 y SDK de Android (plataforma 35, build-tools 34.0.0).

### Compilación
- `./gradlew assembleDebug`: BUILD SUCCESSFUL en la edición ALUMNO. La app compila sin errores de Kotlin.
- `./gradlew assembleDebugAndroidTest`: los tests instrumentados también compilan.

### Pruebas locales (testDebugUnitTest)
- 64 métodos @Test ejecutados en ALUMNO: 22 superados (6 de BaseTest + 16 de ejemploResuelto), 32 fallidos EXACTAMENTE por los TODO previstos (contrato y casoLimite de E01–E16) y 10 de CheckerTest superados.
- Los 32 fallos son los esperados: demuestran que los evaluables siguen sin resolver. Cero fallos inesperados.

### Prueba de sensibilidad del comprobador
Se comprobó que el botón "Comprobar E##" no da aprobados falsos:
1. Se sustituyó temporalmente E01Catalogo por una implementación incorrecta (sin validaciones).
2. El comprobador la clasificó como INCORRECTO con el caso fallido identificado (prueba SensitivityTest: 2/2 superados).
3. Se restauró el archivo original y se verificó que vuelve a PENDIENTE.
4. En la edición PROFESOR, las 16 soluciones de referencia superan el comprobador y todos sus casos individuales (tests lasDieciseisSolucionesSonCorrectas y todosLosCasosIndividualesPasan).

### Errores encontrados y corregidos respecto a la edición anterior
La edición 2.0 NO compilaba. Se corrigieron tres errores reales sin eliminar ninguna funcionalidad:
1. BluetoothLink.kt: literal de carácter roto por un comentario intercalado (línea 66).
2. Models.kt: secuencia de escape inválida ("\p" en una cadena normal; debe ser "\\p").
3. MainActivity.kt: cuatro `return` no locales dentro de un lambda no inline (guard). Solución: `guard` declarado `inline`.

## No ejecutado aquí
- 3 métodos Android instrumentados (PersistenceTest y SmokeTest): requieren emulador o móvil físico. Compilan correctamente.
- Pruebas manuales de hardware: cámara, micrófono, agitar el acelerómetro y Bluetooth entre dos móviles. El botón Comprobar muestra una nota recordando exactamente cuáles son (E05, E07, E08 y E15).
- No se incluye APK: genelalo con `./gradlew assembleDebug`.

## Diferencia intencional por edición
ALUMNO contiene 16 TODO evaluables; sus tests de contrato y límite fallan hasta implementarlos y no deben ignorarse. PROFESOR implementa los 16 contratos y supera las 66 pruebas locales. Ambas ediciones comparten los 16 ejemplos, la interfaz (con el comprobador) y los recursos. RA2.h (despliegue en móvil real) requiere evidencia de aula.

## Alcance de los tests
Los tests locales y el comprobador cubren la LÓGICA de los contratos (valores, límites y rechazos), no la ejecución en hardware. El código de red/SPP/grabación requiere pruebas adicionales; probar un mensaje no equivale a establecer comunicación real. El material se entrega con Gradle Wrapper para que la compilación sea reproducible sin instalar Gradle.
