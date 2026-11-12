@echo off
REM Arranca la web en Windows.
REM Ir a la carpeta donde esta este fichero
cd /d "%~dp0"
REM Comprobar si Node.js esta instalado
where node >nul 2>nul
REM Si no esta (errorlevel distinto de 0), avisar y salir
if errorlevel 1 (
  echo Instala Node.js: https://nodejs.org
  pause
  exit /b 1
)
REM Si no existe node_modules, instalar las librerias (solo la primera vez)
if not exist node_modules (
  echo Instalando...
  call npm install
)
REM Mensaje informativo
echo Web en http://localhost:3000
REM Esperar 5 segundos y abrir el navegador en segundo plano
start "" cmd /c "timeout /t 5 >nul & start http://localhost:3000"
REM Arrancar el servidor
call npm run dev
