"""
ADMIN de Eventos: gestión desde /admin/ sin escribir vistas.
"""

# Importa las herramientas del sitio de administración.
from django.contrib import admin

# Importa el modelo Evento.
from .models import Evento


# Registra Evento en el administrador.
@admin.register(Evento)
class EventoAdmin(admin.ModelAdmin):
    # Columnas del listado.
    list_display = ('nombre', 'fecha', 'estilo', 'aforo', 'dj', 'votacion_abierta',)
    # Filtros laterales.
    list_filter = ('estilo', 'votacion_abierta',)
    # Caja de búsqueda (por nombre del evento o del DJ).
    search_fields = ('nombre', 'dj__nombre',)
