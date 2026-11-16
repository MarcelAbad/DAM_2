#!/usr/bin/env sh
set -eu
if [ ! -f gradlew ]; then echo "Falta wrapper: ejecuta con Gradle 8.9: gradle wrapper --gradle-version 8.9"; exit 2; fi
sh ./gradlew testDebugUnitTest --tests 'es.aula.futbol.BaseTest' --tests '*.E*Test.ejemploResuelto' --tests 'es.aula.futbol.CheckerTest' assembleDebug
printf '\nDentro de la app: Academia > Comprobar E## para verificar cada ejercicio en color.\n'
printf '
Para pruebas instrumentadas, conecta emulador/móvil y ejecuta: sh ./gradlew connectedDebugAndroidTest
'
