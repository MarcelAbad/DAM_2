"""
FORMULARIOS: un ModelForm genera el formulario HTML a partir del modelo (con sus validaciones).
"""
from django import forms
from .models import Contacto


class ContactoForm(forms.ModelForm):
    class Meta:
        model = Contacto
        # fields = lista de campos que aparecen. Alternativas: '__all__' (todos) o exclude = [...]
        # TODO-ALUMNO 2 (HECHO): añadimos los dos campos propios: fecha_nacimiento y monedas.
        fields = ['nombre', 'tipo', 'email', 'telefono', 'fecha_nacimiento', 'monedas', 'notas']
        widgets = {
            # Selector de fecha del navegador. format='%Y-%m-%d' es el formato que entiende <input type="date">
            # (sin esto, al EDITAR un contacto la fecha no aparecería rellenada).
            'fecha_nacimiento': forms.DateInput(attrs={'type': 'date'}, format='%Y-%m-%d'),
            # Cuadro de texto de 3 líneas para las notas.
            'notas': forms.Textarea(attrs={'rows': 3}),
        }
