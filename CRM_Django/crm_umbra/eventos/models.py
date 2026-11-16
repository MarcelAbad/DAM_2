"""
MODELOS de Eventos (NIVEL 4 · módulo propio de UMBRA CRM).
Un Evento es una NOCHE / sesión de la discoteca (por ejemplo, "Frecuencia Vol. 06"):
tiene fecha, estilo musical, aforo, un DJ principal y un estado (votación abierta o no).
Tras cualquier cambio: python manage.py makemigrations && python manage.py migrate
"""

# Importa las herramientas para definir modelos y campos de base de datos.
from django.db import models


# Define el modelo Evento; Django lo convierte en una tabla.
class Evento(models.Model):

    # Estilos musicales posibles (valor guardado, texto visible).
    ESTILOS = [
        ('techno', 'Techno'),
        ('house', 'House'),
        ('melodic', 'Melodic techno / House'),
        ('reggaeton', 'Reggaeton'),
        ('otro', 'Otro'),
    ]

    # Nombre de la sesión, por ejemplo "Frecuencia Vol. 06".
    nombre = models.CharField(max_length=120)

    # Fecha de la noche (solo día, sin hora).
    fecha = models.DateField()

    # Estilo de música, limitado a las opciones de ESTILOS.
    estilo = models.CharField(max_length=20, choices=ESTILOS, default='melodic')

    # Aforo máximo (nº de personas). PositiveIntegerField = entero que nunca es negativo.
    aforo = models.PositiveIntegerField(default=300)

    # DJ principal de la noche: relación con un Contacto (el que tenga tipo DJ).
    # limit_choices_to={'tipo': 'dj'} hace que en el formulario SOLO aparezcan contactos de tipo DJ.
    # SET_NULL: si se borra el DJ, el evento se conserva sin DJ (null=True permite NULL en la BD).
    # related_name='eventos' permite consultar desde un DJ: contacto.eventos.all()
    dj = models.ForeignKey(
        'contactos.Contacto',
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
        limit_choices_to={'tipo': 'dj'},
        related_name='eventos',
        verbose_name='DJ principal'
    )

    # ¿Está abierta la votación de canciones? Por defecto, sí.
    votacion_abierta = models.BooleanField(default=True, verbose_name='votación abierta')

    # Configuración adicional del modelo.
    class Meta:
        # Las noches más próximas (fecha mayor) primero.
        ordering = ['-fecha']

    # Cómo se muestra un evento como texto (en el admin y en desplegables).
    def __str__(self):
        return f'{self.nombre} ({self.fecha:%d/%m/%Y})'
