"""
VISTAS de Contactos: reciben la petición (request) y devuelven una respuesta (HTML o redirección).
CRUD completo ya funcionando: listar (index), ver (detalle), crear, editar y borrar.
Añadido en UMBRA CRM: filtro por tipo (TODO 7) y exportación a CSV (Nivel 3).
"""
import csv                                                 # Módulo estándar de Python para escribir archivos CSV

from django.contrib import messages                       # Avisos tipo "Guardado correctamente"
from django.contrib.auth.decorators import login_required  # Exige haber iniciado sesión
from django.http import HttpResponse                      # Respuesta HTTP "a mano" (aquí, el archivo CSV)
from django.shortcuts import render, get_object_or_404, redirect
from core.utils import campos_de
from .models import Contacto
from .forms import ContactoForm


def _filtrar(request):
    """Aplica la búsqueda (?q=) y el filtro por tipo (?tipo=) y devuelve (queryset, q, tipo).
    Es una función auxiliar (empieza por _) que usan el listado y la exportación a CSV,
    para que el CSV contenga EXACTAMENTE lo que el usuario está viendo en pantalla."""
    q = request.GET.get('q', '').strip()             # request.GET = parámetros de la URL (?q=...)
    tipo = request.GET.get('tipo', '').strip()       # Valor elegido en el desplegable de tipo ('' = todos)
    objetos = Contacto.objects.all()                # .all() = todos. Otros: .filter() .exclude() .get() .order_by('-campo') .count()
    if q:
        # __icontains = "contiene, sin distinguir mayúsculas". Otros: __exact __startswith __gt __lt __gte __lte __in=[..] __isnull=True
        objetos = objetos.filter(nombre__icontains=q)
    # TODO-ALUMNO 7 (HECHO): filtro por un campo con choices (tipo de contacto).
    # dict(Contacto.TIPOS) convierte las choices en diccionario; así solo aceptamos valores válidos
    # (si alguien escribe ?tipo=loquesea en la URL, lo ignoramos en vez de romper).
    if tipo in dict(Contacto.TIPOS):
        objetos = objetos.filter(tipo=tipo)          # WHERE tipo = 'dj'
    else:
        tipo = ''                                    # Valor no válido -> sin filtro
    return objetos, q, tipo


@login_required
def index(request):
    """LISTADO con búsqueda y filtro: /contactos/?q=texto&tipo=dj"""
    objetos, q, tipo = _filtrar(request)
    return render(request, 'contactos/lista.html', {
        'objetos': objetos, 'q': q,
        'tipos': Contacto.TIPOS,      # Opciones del desplegable
        'tipo': tipo,                 # Opción actualmente elegida (para dejarla seleccionada)
    })


@login_required
def exportar_csv(request):
    """NIVEL 3 · EXPORTAR a CSV: descarga los contactos (respetando búsqueda y filtro) como archivo .csv"""
    objetos, _, _ = _filtrar(request)                 # Reutilizamos el mismo filtrado que el listado
    # content_type='text/csv' avisa al navegador de que es un CSV; charset utf-8 para las tildes y la ñ.
    respuesta = HttpResponse(content_type='text/csv; charset=utf-8')
    # Content-Disposition: attachment -> el navegador lo DESCARGA con este nombre en vez de mostrarlo.
    respuesta['Content-Disposition'] = 'attachment; filename="contactos_umbra.csv"'
    respuesta.write('﻿')                         # BOM: marca que hace que Excel lea bien las tildes
    # delimiter=';' porque el Excel en español separa las columnas con punto y coma.
    escritor = csv.writer(respuesta, delimiter=';')
    escritor.writerow(['Nombre', 'Tipo', 'Email', 'Teléfono', 'Fecha de nacimiento', 'Edad',
                       'Frecuencia Coins', 'Alta'])  # Fila de cabecera
    for c in objetos:                                 # Una fila por contacto
        escritor.writerow([
            c.nombre,
            c.get_tipo_display(),                     # Texto visible ('DJ / Artista'), no el valor interno ('dj')
            c.email,
            c.telefono,
            c.fecha_nacimiento.strftime('%d/%m/%Y') if c.fecha_nacimiento else '',   # Fecha en formato español
            c.edad if c.edad is not None else '',     # Edad calculada (o vacío si no hay fecha)
            c.monedas,
            c.creado.strftime('%d/%m/%Y'),
        ])
    return respuesta


@login_required
def detalle(request, pk):
    """FICHA de un objeto. <int:pk> en la URL llega aquí como pk."""
    objeto = get_object_or_404(Contacto, pk=pk)     # Si no existe -> error 404 (en vez de romperse)
    return render(request, 'contactos/detalle.html', {'objeto': objeto, 'campos': campos_de(objeto), 'ns': 'contactos'})


@login_required
def crear(request):
    """CREAR. GET = muestra el formulario vacío. POST = el usuario lo envió."""
    form = ContactoForm(request.POST or None, initial={})   # initial = valores precargados
    if request.method == 'POST' and form.is_valid():          # is_valid() ejecuta las validaciones del modelo
        obj = form.save()
        messages.success(request, 'Creado correctamente')
        return redirect('contactos:detalle', pk=obj.pk)           # Redirigir tras POST evita reenvíos al recargar
    return render(request, 'core/form.html', {'form': form, 'titulo': 'Nuevo: contacto', 'volver': 'contactos:index'})


@login_required
def editar(request, pk):
    """EDITAR: igual que crear pero con instance=objeto existente."""
    objeto = get_object_or_404(Contacto, pk=pk)
    form = ContactoForm(request.POST or None, instance=objeto)
    if request.method == 'POST' and form.is_valid():
        form.save()
        messages.success(request, 'Cambios guardados')
        return redirect('contactos:detalle', pk=objeto.pk)
    return render(request, 'core/form.html', {'form': form, 'titulo': f'Editar: {objeto}', 'volver': 'contactos:index'})


@login_required
def borrar(request, pk):
    """BORRAR: GET pide confirmación; solo el POST borra de verdad."""
    objeto = get_object_or_404(Contacto, pk=pk)
    if request.method == 'POST':
        objeto.delete()
        messages.success(request, 'Eliminado')
        return redirect('contactos:index')
    return render(request, 'core/confirmar_borrado.html', {'objeto': objeto, 'volver': 'contactos:index'})
