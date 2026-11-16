@echo off
if not exist gradlew.bat (
 echo Falta wrapper. Ejecuta con Gradle 8.9: gradle wrapper --gradle-version 8.9
 exit /b 2
)
call gradlew.bat testDebugUnitTest --tests "es.aula.futbol.BaseTest" --tests "*.E*Test.ejemploResuelto" --tests "es.aula.futbol.CheckerTest" assembleDebug
if errorlevel 1 exit /b 1
echo Para instrumentadas: gradlew.bat connectedDebugAndroidTest
