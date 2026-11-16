"""
VISTAS de Oportunidades: reciben una petición del navegador (request)
y devuelven una respuesta, como una página HTML o una redirección.
CRUD significa crear, leer, actualizar y borrar.

Añadido en UMBRA CRM:
  - filtro por etapa (TODO 7),
  - botón "Marcar como ganada" que solo funciona por POST (TODO 8),
  - importe total por etapa con values().annotate(Sum()) (Nivel 3).
"""


# Permite mostrar avisos temporales, como «Guardado correctamente».
from django.contrib import messages

# Hace que solo puedan entrar usuarios que hayan iniciado sesión.
from django.contrib.auth.decorators import login_required

# Funciones de agregación de la base de datos: Sum = suma de una columna, Count = nº de filas.
from django.db.models import Sum, Count

# Herramientas para mostrar plantillas, buscar objetos o redirigir a otra página.
from django.shortcuts import render, get_object_or_404, redirect

# Decorador que rechaza (error 405) cualquier petición que NO sea POST.
from django.views.decorators.http import require_POST

# Función del proyecto que prepara los campos de un objeto para mostrarlos.
from core.utils import campos_de

# Importa el modelo y el formulario de esta aplicación.
from .models import Oportunidad
from .forms import OportunidadForm


def _totales_por_etapa():
    """NIVEL 3 · Importe total por etapa.
    values('etapa') agrupa las filas por etapa y annotate(...) calcula, para cada grupo,
    la suma de importes y el número de oportunidades (equivale a un GROUP BY de SQL)."""
    filas = Oportunidad.objects.values('etapa').annotate(total=Sum('importe'), n=Count('id'))
    por_etapa = {f['etapa']: f for f in filas}          # Lo pasamos a diccionario para buscar por etapa
    resultado = []
    for valor, texto in Oportunidad.ETAPAS:             # Recorremos TODAS las etapas, en su orden...
        f = por_etapa.get(valor)                        # ...aunque alguna no tenga oportunidades todavía
        resultado.append({
            'etapa': texto,                             # Texto visible de la etapa
            'n': f['n'] if f else 0,                    # Cuántas oportunidades hay en esa etapa
            'total': f['total'] if f else 0,            # Suma de sus importes (0 si no hay ninguna)
        })
    return resultado


# Protege esta vista: si el usuario no ha iniciado sesión, Django no la muestra.
@login_required
def index(request):
    """Muestra el listado. También permite buscar con /oportunidades/?q=texto&etapa=reservado."""

    # Lee el parámetro q de la URL. Si no existe, usa texto vacío.
    # strip() quita espacios sobrantes al principio y al final.
    q = request.GET.get('q', '').strip()

    # Lee la etapa elegida en el desplegable ('' = todas las etapas).
    etapa = request.GET.get('etapa', '').strip()

    # Obtiene todas las oportunidades de la base de datos.
    objetos = Oportunidad.objects.all()

    # Solo aplica la búsqueda si q contiene algún texto.
    if q:
        # Busca oportunidades cuyo título contenga q, ignorando mayúsculas y minúsculas.
        # __icontains es un filtro especial de Django para ese tipo de búsqueda.
        objetos = objetos.filter(titulo__icontains=q)

    # TODO-ALUMNO 7 (HECHO): filtro por etapa.
    # Solo aceptamos etapas que existan en ETAPAS (así una URL manipulada no rompe nada).
    if etapa in dict(Oportunidad.ETAPAS):
        objetos = objetos.filter(etapa=etapa)           # WHERE etapa = 'reservado'
    else:
        etapa = ''                                      # Valor no válido -> sin filtro

    # Carga la plantilla lista.html y le entrega los datos que necesita:
    # objetos = oportunidades encontradas; q = texto buscado;
    # etapas/etapa = opciones del desplegable y la elegida; totales = importe por etapa (Nivel 3).
    return render(
        request,
        'oportunidades/lista.html',
        {'objetos': objetos, 'q': q, 'etapas': Oportunidad.ETAPAS, 'etapa': etapa,
         'totales': _totales_por_etapa()}
    )


# Solo permite acceder a esta vista después de iniciar sesión.
@login_required
def detalle(request, pk):
    """Muestra la ficha de una oportunidad. pk es su identificador."""

    # Busca la oportunidad cuyo identificador coincide con pk.
    # Si no existe, muestra una página de error 404 («no encontrado»).
    objeto = get_object_or_404(Oportunidad, pk=pk)

    # Muestra la plantilla de detalle PROPIA de oportunidades (con el botón "Marcar como ganada")
    # y le pasa: objeto = oportunidad; campos = datos preparados para mostrar.
    return render(
        request,
        'oportunidades/detalle.html',
        {
            'objeto': objeto,
            'campos': campos_de(objeto),
        }
    )


# TODO-ALUMNO 8 (HECHO): vista que marca una oportunidad como ganada.
# @require_POST: si alguien abre esta dirección escribiéndola en el navegador (GET),
# Django responde "405 Método no permitido" y NO cambia nada. Solo el botón (formulario POST) funciona.
@login_required
@require_POST
def ganar(request, pk):
    """Cambia la etapa a 'ganado' y guarda. Solo por POST."""
    objeto = get_object_or_404(Oportunidad, pk=pk)      # Busca la oportunidad (404 si no existe)
    objeto.etapa = 'ganado'                             # Cambia la etapa al valor 'ganado'
    objeto.save()                                       # Guarda el cambio en la base de datos
    messages.success(request, f'«{objeto.titulo}» marcada como ganada')   # Aviso para el usuario
    return redirect('oportunidades:detalle', pk=objeto.pk)               # Vuelve a la ficha


# Solo permite acceder a esta vista después de iniciar sesión.
@login_required
def crear(request):
    """Muestra un formulario vacío y, cuando se envía, crea una oportunidad."""

    # request.POST contiene los datos si el formulario se envió.
    # Si no se envió, None hace que se muestre vacío.
    # initial precarga el contacto indicado en la URL, por ejemplo:
    # /oportunidades/nuevo/?contacto=3
    form = OportunidadForm(
        request.POST or None,
        initial={'contacto': request.GET.get('contacto')}
    )

    # Comprueba que el formulario se haya enviado con POST
    # y que todos sus datos pasen las validaciones.
    if request.method == 'POST' and form.is_valid():

        # Guarda la nueva oportunidad en la base de datos.
        obj = form.save()

        # Añade un mensaje de éxito para mostrarlo en la siguiente página.
        messages.success(request, 'Creado correctamente')

        # Redirige a la ficha recién creada.
        # obj.pk es su identificador. Redirigir después de guardar evita
        # que recargar la página vuelva a enviar el formulario.
        return redirect('oportunidades:detalle', pk=obj.pk)

    # Si el formulario no se ha enviado o tiene errores, muestra la página
    # del formulario con su título y la dirección para volver al listado.
    return render(
        request,
        'core/form.html',
        {
            'form': form,
            'titulo': 'Nueva: reserva o contrato',
            'volver': 'oportunidades:index'
        }
    )


# Solo permite acceder a esta vista después de iniciar sesión.
@login_required
def editar(request, pk):
    """Muestra el formulario de una oportunidad existente y guarda los cambios."""

    # Busca la oportunidad que se quiere editar.
    # Si no existe, responde con un error 404.
    objeto = get_object_or_404(Oportunidad, pk=pk)

    # Crea el formulario usando los datos enviados, si los hay.
    # instance=objeto indica que se editará ese objeto existente,
    # en vez de crear uno nuevo.
    form = OportunidadForm(request.POST or None, instance=objeto)

    # Si el usuario envió el formulario y los datos son válidos:
    if request.method == 'POST' and form.is_valid():

        # Guarda los cambios en la oportunidad existente.
        form.save()

        # Añade un aviso de éxito.
        messages.success(request, 'Cambios guardados')

        # Vuelve a la ficha de la oportunidad editada.
        return redirect('oportunidades:detalle', pk=objeto.pk)

    # Muestra el formulario, tanto al abrirlo como si contiene errores.
    # f'Editar: {objeto}' construye un texto con el título de la oportunidad.
    return render(
        request,
        'core/form.html',
        {
            'form': form,
            'titulo': f'Editar: {objeto}',
            'volver': 'oportunidades:index'
        }
    )


# Solo permite acceder a esta vista después de iniciar sesión.
@login_required
def borrar(request, pk):
    """Pide confirmación al abrir la página y borra solo cuando se envía por POST."""

    # Busca la oportunidad que se quiere borrar.
    objeto = get_object_or_404(Oportunidad, pk=pk)

    # Solo borra si la petición es POST, normalmente al confirmar el formulario.
    # Así, visitar la dirección con GET no borra nada por accidente.
    if request.method == 'POST':

        # Elimina la oportunidad de la base de datos.
        objeto.delete()

        # Añade un aviso de que se eliminó.
        messages.success(request, 'Eliminado')

        # Vuelve al listado de oportunidades.
        return redirect('oportunidades:index')

    # Si la petición no es POST, muestra la página que pide confirmar el borrado.
    return render(
        request,
        'core/confirmar_borrado.html',
        {
            'objeto': objeto,
            'volver': 'oportunidades:index'
        }
    )
