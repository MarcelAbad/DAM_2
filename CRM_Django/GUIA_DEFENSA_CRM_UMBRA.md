# Guía de defensa — UMBRA CRM (Django)

> Está escrita en llano y en orden. Primero cómo abrirlo, luego qué has hecho, luego qué te pueden preguntar.
> Todo lo que pone "comprobado" lo he probado yo: **19 pruebas automáticas pasan** y el zip arranca en una carpeta limpia.

---

## 0. Cómo abrirlo y arrancarlo

**Carpeta del proyecto:** `C:\Users\Marcel\Desktop\DAM 2\DAM_2\CRM_Django\crm_umbra`
**Zip de entrega (sin venv ni db):** `C:\Users\Marcel\Desktop\DAM 2\DAM_2\CRM_Django\Entrega\UMBRA_CRM_Marcel.zip`

En el PC de clase:
1. Descomprime el zip.
2. Entra en la carpeta `crm_umbra` y haz **doble clic en `iniciar.bat`** (hace todo solo: crea el entorno, instala Django, prepara la base de datos y carga los datos).
3. Abre http://127.0.0.1:8000/ → usuario **admin**, clave **admin1234**.
4. **No cierres la ventana negra** mientras enseñas el CRM.

Si `iniciar.bat` falla porque no encuentra `python`, mira la guía de instalación de tu Drive (`guia_django_sin_admin.md`): hay que instalar Python marcando *"Add python.exe to PATH"*.

Para pasar las pruebas: en una terminal dentro de la carpeta, con el entorno activado, `python manage.py test` (deben salir 19 en verde).

---

## 1. Qué es el proyecto (cómo empezar)

"Es un CRM hecho con Django para una **discoteca** (UMBRA). Un CRM guarda las personas con las que trabaja una empresa y todo lo que pasa con ellas: reservas, llamadas, tareas. Partí de un CRM genérico que ya funcionaba y lo adapté a mi temática: cambié los modelos, añadí campos, filtros, un botón, una métrica, dos funciones extra y un módulo nuevo."

Está relacionado con tu trabajo de Figma: la app de UMBRA donde el público vota canciones. El CRM es la herramienta **interna** del local.

**Los 4 módulos del esqueleto + 1 propio:**

| Módulo | Qué guarda en UMBRA |
|---|---|
| Contactos | Clubbers, VIP, promotores, DJs y proveedores |
| Oportunidades | Reservas de mesa y contratos (DJs, bebidas, sonido) |
| Interacciones | Cada llamada, WhatsApp, Instagram, email o reunión |
| Tareas | Cosas por hacer del equipo, con prioridad |
| **Eventos (Nivel 4)** | Las noches: fecha, estilo, aforo, DJ y votación |

**Cómo funciona Django (por si preguntan):** cada módulo tiene `models.py` (la tabla), `forms.py` (el formulario), `views.py` (la lógica), `urls.py` (las direcciones) y `templates/` (el HTML). Siempre se sigue este orden: **modelo → migraciones → formulario/admin/lista → probar.**

---

## 2. Lo que he hecho, tarea por tarea

### Nivel 1 — Personalizar (3 puntos)

| TODO | Qué hice | Dónde está |
|---|---|---|
| **0** | `NOMBRE_CRM = 'UMBRA CRM'` y `COLOR_PRINCIPAL = '#1f5c3d'` (el verde de la marca). Escribí `TEMATICA.md` (5 líneas) | `crm_project/settings.py`, `TEMATICA.md` |
| **1** | Tipos de contacto: clubber, VIP Member, promotor, DJ, proveedor, otro | `contactos/models.py` |
| **2** | Dos campos propios en Contacto: **`fecha_nacimiento`** (DateField) y **`monedas`** (Frecuencia Coins, PositiveIntegerField). Los llevé al formulario, al admin, al listado y a la ficha. Además calculo la **edad** con una propiedad | `contactos/models.py`, `forms.py`, `admin.py`, templates |
| **3** | 5 etapas: consulta → presupuesto → reservado → **ganado** → perdido. **No renombré el valor `ganado`** porque el panel suma las oportunidades con ese valor; solo cambié el texto visible ("Confirmada y pagada") | `oportunidades/models.py` |
| **4** | Dos canales nuevos: WhatsApp e Instagram | `interacciones/models.py` |
| **5** | Campo `prioridad` con `choices` (alta/media/baja) en tareas, también en formulario, admin y listado | `tareas/models.py`, `forms.py`, `admin.py` |
| **10** | `cargar_ejemplo.py` con datos de discoteca: 12 contactos, 9 reservas/contratos, 6 interacciones, 6 tareas y 3 noches. Todo inventado | `core/management/commands/cargar_ejemplo.py` |

**Detalle que sabe poca gente (y luce):** en el formulario de fecha puse `format='%Y-%m-%d'`. Sin eso, al **editar** un contacto la fecha de nacimiento salía vacía porque el navegador no entendía el formato.

### Nivel 2 — Ampliar (3 puntos)

| TODO | Qué hice | Cómo funciona |
|---|---|---|
| **6** | Columnas de los 4 listados adaptadas | Contactos: Edad y Coins · Oportunidades: Fecha prevista · Interacciones: Autor · Tareas: Prioridad |
| **7** | Filtro por **tipo** en contactos y por **etapa** en oportunidades | Un `<select>` en el listado envía `?tipo=dj`; la vista hace `.filter(tipo=tipo)`. Compruebo que el valor exista en `TIPOS` (si alguien inventa uno en la URL, se ignora y no rompe) |
| **8** | Botón **"Marcar como ganada"** en la ficha de la oportunidad | Es un formulario `method="post"` con `{% csrf_token %}`. La vista `ganar` lleva `@require_POST`: si alguien entra escribiendo la dirección (GET), Django responde **405** y no cambia nada |
| **9** | Métrica propia en el panel: **tareas vencidas** | `Tarea.objects.filter(hecha=False, vence__lt=date.today()).count()`. `lt` = "menor que" (fecha anterior a hoy). Si hay alguna, se destaca en amarillo |

### Nivel 3 — Funcionalidades CRM (elegí 2; 2 puntos)

1. **Importe total por etapa:** `Oportunidad.objects.values('etapa').annotate(total=Sum('importe'), n=Count('id'))`. Es como un `GROUP BY` de SQL: agrupa por etapa y suma. Sale en una tabla arriba del listado de oportunidades.
2. **Exportar contactos a CSV:** una vista que devuelve `HttpResponse(content_type='text/csv')` con la cabecera `attachment` (el navegador lo descarga). **Respeta la búsqueda y el filtro** que estén activos, porque usa la misma función `_filtrar()` que el listado. Uso `;` como separador y un BOM para que Excel en español lea bien las tildes.

### Nivel 4 — Módulo propio (+1 punto)

Creé la app **`eventos`** ("Noches"), copiando la estructura de `tareas`: modelo `Evento` (nombre, fecha, estilo, aforo, DJ principal, votación abierta), formulario, admin, vistas CRUD, URLs y plantilla. La añadí a `MODULOS_CRM` y **aparece sola en el menú**. El DJ principal es una relación con Contacto y solo deja elegir contactos de tipo DJ (`limit_choices_to={'tipo': 'dj'}`).

### Comentarios, reglas y entrega

- Todo lo nuevo está **comentado línea a línea**, con el mismo estilo que el esqueleto, y marqué cada cambio con `TODO-ALUMNO n (HECHO)`.
- No uso `fields='__all__'` en interacciones (el autor se asigna en la vista).
- Entrega: zip **sin `venv/` ni `db.sqlite3`**, con `TEMATICA.md` y `LEEME.txt` (credenciales + lista de TODOs).

---

## 3. Demostración en vivo (5 minutos)

1. **Panel:** enseña las tarjetas (12 contactos, 9 reservas...) y la métrica amarilla de **tareas vencidas**.
2. **Contactos:** filtra por *DJ / Artista*. Pulsa **Exportar CSV** y abre el archivo.
3. **Una ficha:** abre a *Pablo Serrano*: sale "(menor de edad: no puede entrar)" y la edad calculada.
4. **Oportunidades:** enseña el **importe total por etapa**. Filtra por *Reservada*. Entra en una oportunidad *Consulta* y pulsa **Marcar como ganada**; vuelve al panel y verás que el importe ganado ha subido.
5. **Tareas:** filtra por prioridad *Alta*.
6. **Noches (módulo propio):** crea una noche nueva eligiendo un DJ.
7. Si te lo piden: abre http://127.0.0.1:8000/oportunidades/1/ganar/ en la barra de direcciones y enseña el error **405** (así demuestras que solo funciona por POST).

---

## 4. Preguntas que te pueden hacer (con respuesta)

| Pregunta | Respuesta |
|---|---|
| ¿Qué es una migración? | Un archivo que describe un cambio en los modelos. `makemigrations` lo prepara y `migrate` lo aplica a la base de datos. |
| ¿Por qué no renombraste "ganado"? | Porque `core/views.py` suma las oportunidades con `etapa='ganado'`. Cambié solo el texto visible. |
| ¿Qué es `choices`? | Una lista de pares (valor guardado, texto visible); Django la convierte en desplegable y valida el valor. |
| ¿Qué es `{% csrf_token %}`? | Una protección: Django comprueba que el formulario POST salió de su propia web. Sin ella da error "CSRF verification failed". |
| ¿Por qué `@require_POST` en ganar? | Para que una acción que **modifica datos** no se pueda lanzar solo abriendo un enlace. |
| ¿Qué hace `vence__lt=date.today()`? | `__lt` = "less than": tareas cuya fecha límite es anterior a hoy. |
| ¿Qué es `values().annotate(Sum())`? | Agrupar filas y calcular una suma por grupo, como un `GROUP BY`. |
| ¿Cómo calculas la edad? | Año actual − año de nacimiento, restando 1 si todavía no ha pasado su cumpleaños este año. Es una `@property`: no se guarda en la BD, se calcula. |
| ¿Qué es `related_name`? | Permite ir "al revés": `contacto.oportunidades.all()` da todas las oportunidades de un contacto. |
| ¿Qué es `limit_choices_to`? | Restringe las opciones de una relación; en eventos solo deja elegir DJs. |
| ¿Cómo se añade un módulo? | `startapp`, copiar la estructura, añadirlo a `MODULOS_CRM` y crear sus migraciones. |
| ¿Por qué no se entrega el `db.sqlite3` ni el `venv`? | Son archivos que se regeneran (la base se crea con `migrate` y `cargar_ejemplo`; el venv con `pip install`). |

---

## 5. Honestidad: lo que hay que saber

- **Probado:** 19 pruebas automáticas (una por cada tarea), el panel y el listado de oportunidades en el navegador y el zip en una carpeta limpia (entorno nuevo, instalación, migraciones, datos y pruebas).
- **No probado:** `iniciar.bat` tal cual, haciendo doble clic. Lo probé con los mismos pasos a mano, porque en este PC `python` no está en el PATH. En clase, si falla, instala Python como dice la guía de tu Drive.
- Los nombres de personas y datos son **inventados**; "Pablo Serrano" es un menor de edad de ejemplo a propósito.
- La versión de Django que se instala es la última (6.x). Con `django>=4.2` en `requirements.txt` funciona igual.
