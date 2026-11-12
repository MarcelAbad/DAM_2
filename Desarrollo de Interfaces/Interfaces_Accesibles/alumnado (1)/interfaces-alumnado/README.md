# Web de ejercicios - Interfaces de Usuario (2 DAM)

## Arrancar
- Windows: doble clic en `start.bat`
- Linux / macOS: `chmod +x start.sh` y luego `./start.sh`
Necesitas Node.js 18 o superior. Se abre http://localhost:3000 (usa Chrome o Edge).

## Carpetas (el alumno SOLO toca `public/`)
- `public/index.html`      -> pagina central que enlaza a todos los ejercicios.
- `public/resueltos/`      -> ejercicios resueltos y explicados.
- `public/evaluables/`     -> ejercicios para completar (busca los `TODO`).
- `public/componentes/`    -> componentes web propios.
- `public/css/estilos.css` -> estilos de toda la web.
- `pages/api/`, `data/`, `next.config.js`, `package.json` -> BACKEND. NO TOCAR.
- `tools/empaquetar.js`    -> une los componentes (`npm run empaquetar`).
- `tests/`                 -> pruebas automaticas (`npm test`).

## Version profesor
El ZIP `profesor` incluye ademas `public/profesor/` con las soluciones de E1-E7
y `tests/profesor.test.js` con las pruebas de las soluciones.
El ZIP `alumnado` NO incluye esas carpetas.
