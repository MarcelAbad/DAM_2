"""
FORMULARIOS de Eventos: un ModelForm crea el formulario a partir del modelo y reutiliza sus validaciones.
"""

# Importa las herramientas para crear formularios de Django.
from django import forms

# Importa el modelo Evento.
from .models import Evento


# Formulario basado en el modelo Evento.
class EventoForm(forms.ModelForm):

    # Configuración del formulario.
    class Meta:
        # Modelo en el que se basa.
        model = Evento

        # Campos que aparecen (NO usamos '__all__': elegimos uno a uno).
        fields = ['nombre', 'fecha', 'estilo', 'aforo', 'dj', 'votacion_abierta']

        # Selector de fecha del navegador; format es el formato que entiende <input type="date">.
        widgets = {
            'fecha': forms.DateInput(attrs={'type': 'date'}, format='%Y-%m-%d'),
        }
