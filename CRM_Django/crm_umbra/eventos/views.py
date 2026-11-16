"""
VISTAS de Eventos (NIVEL 4): copia de la estructura de la app "tareas".
CRUD completo: listar (index), ver (detalle), crear, editar y borrar.
"""

# Mensajes flash («Guardado correctamente»).
from django.contrib import messages

# Solo usuarios con sesión iniciada.
from django.contrib.auth.decorators import login_required

# Herramientas para mostrar plantillas, buscar objetos y redirigir.
from django.shortcuts import render, get_object_or_404, redirect

# Función del proyecto que prepara los campos de un objeto para la ficha.
from core.utils import campos_de

# Modelo y formulario de esta app.
from .models import Evento
from .forms import EventoForm


@login_required
def index(request):
    """Listado de noches con búsqueda por nombre: /eventos/?q=texto"""
    q = request.GET.get('q', '').strip()                 # Texto buscado ('' si no hay)
    objetos = Evento.objects.select_related('dj')        # select_related evita una consulta extra por cada DJ
    if q:
        objetos = objetos.filter(nombre__icontains=q)    # Nombre que contenga q (sin mayúsculas)
    return render(request, 'eventos/lista.html', {'objetos': objetos, 'q': q})


@login_required
def detalle(request, pk):
    """Ficha de una noche."""
    objeto = get_object_or_404(Evento, pk=pk)            # 404 si no existe
    return render(request, 'core/detalle.html',
                  {'objeto': objeto, 'campos': campos_de(objeto), 'ns': 'eventos'})


@login_required
def crear(request):
    """Crear una noche: GET muestra el formulario vacío; POST lo procesa."""
    form = EventoForm(request.POST or None)              # Formulario vacío o con los datos enviados
    if request.method == 'POST' and form.is_valid():     # Si se envió y es válido...
        obj = form.save()                                # ...guardamos
        messages.success(request, 'Creado correctamente')
        return redirect('eventos:detalle', pk=obj.pk)    # Redirigir tras POST evita reenvíos
    return render(request, 'core/form.html',
                  {'form': form, 'titulo': 'Nueva noche', 'volver': 'eventos:index'})


@login_required
def editar(request, pk):
    """Editar una noche existente (instance=objeto)."""
    objeto = get_object_or_404(Evento, pk=pk)
    form = EventoForm(request.POST or None, instance=objeto)
    if request.method == 'POST' and form.is_valid():
        form.save()
        messages.success(request, 'Cambios guardados')
        return redirect('eventos:detalle', pk=objeto.pk)
    return render(request, 'core/form.html',
                  {'form': form, 'titulo': f'Editar: {objeto}', 'volver': 'eventos:index'})


@login_required
def borrar(request, pk):
    """Borrar: GET pide confirmación; solo el POST borra."""
    objeto = get_object_or_404(Evento, pk=pk)
    if request.method == 'POST':
        objeto.delete()
        messages.success(request, 'Eliminado')
        return redirect('eventos:index')
    return render(request, 'core/confirmar_borrado.html',
                  {'objeto': objeto, 'volver': 'eventos:index'})
