#!/usr/bin/env bash
# Arranca la web en Linux / macOS.
# Ir a la carpeta donde esta este fichero
cd "$(dirname "$0")"
# Si Node.js no esta instalado, avisar y salir
if ! command -v node >/dev/null 2>&1; then echo "Instala Node.js: https://nodejs.org"; exit 1; fi
# Si no existe la carpeta node_modules, instalar las librerias (solo la primera vez)
if [ ! -d node_modules ]; then echo "Instalando..."; npm install || exit 1; fi
# Mensaje informativo
echo "Web en http://localhost:3000"
# En segundo plano: esperar 4 segundos y abrir el navegador
(sleep 4; (xdg-open http://localhost:3000 || open http://localhost:3000) >/dev/null 2>&1) &
# Arrancar el servidor
npm run dev
