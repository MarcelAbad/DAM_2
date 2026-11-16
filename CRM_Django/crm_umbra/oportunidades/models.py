"""
MODELOS de Oportunidades. Un modelo = una tabla; cada atributo = una columna.
Tras CUALQUIER cambio aquí ejecuta:
    python manage.py makemigrations     # Detecta el cambio y prepara una migración.
    python manage.py migrate            # Aplica esa migración a la base de datos.
(Si usas iniciar.sh / iniciar.bat, se hace solo al arrancar.)

UMBRA CRM: una "oportunidad" es una reserva de mesa, un cumpleaños o el contrato de un DJ:
cualquier posible ingreso para la discoteca, que va avanzando por etapas hasta cerrarse.
"""

# Importa las herramientas de Django para definir modelos y campos de base de datos.
from django.db import models


# Define el modelo Oportunidad; Django lo convierte en una tabla de la base de datos.
class Oportunidad(models.Model):

    # Lista de opciones posibles para el campo "etapa".
    # En cada pareja, el primer texto es el valor que se guarda en la base de datos;
    # el segundo es el texto que se muestra a las personas.
    # TODO-ALUMNO 3 (HECHO): etapas de una reserva/contrato de discoteca (5 etapas, mínimo 4).
    # IMPORTANTE: el valor 'ganado' NO se ha renombrado, porque el panel (core/views.py)
    # suma las oportunidades con etapa 'ganado'. Solo hemos cambiado su texto visible.
    ETAPAS = [
        ('consulta', 'Consulta'),                  # Alguien pregunta por una mesa / un contrato
        ('presupuesto', 'Presupuesto enviado'),    # Ya le hemos pasado precio
        ('reservado', 'Reservada (señal pagada)'), # Ha pagado una señal y tiene la mesa guardada
        ('ganado', 'Confirmada y pagada'),         # Cerrada con éxito (cuenta como ingreso)
        ('perdido', 'Cancelada'),                  # Se ha caído
    ]

    # Campo de texto para el título. No puede superar los 150 caracteres.
    titulo = models.CharField(max_length=150)

    # Relaciona cada oportunidad con un contacto.
    # Muchas oportunidades pueden pertenecer al mismo contacto.
    # 'contactos.Contacto' indica la app y el modelo, sin tener que importarlo aquí.
    # CASCADE significa que, si se borra el contacto, también se borran sus oportunidades.
    # related_name permite consultar las oportunidades de un contacto así:
    # contacto.oportunidades.all()
    contacto = models.ForeignKey(
        'contactos.Contacto',
        on_delete=models.CASCADE,
        related_name='oportunidades'
    )

    # Guarda la etapa de la oportunidad.
    # choices limita los valores a las opciones de ETAPAS.
    # Si no se especifica una etapa al crearla, comienza como 'consulta'.
    etapa = models.CharField(max_length=20, choices=ETAPAS, default='consulta')

    # Guarda una cantidad con decimales, adecuada para importes de dinero.
    # max_digits=10 permite hasta 10 dígitos en total;
    # decimal_places=2 reserva 2 dígitos para los centavos.
    # Si no se indica un importe, el valor inicial es 0.
    importe = models.DecimalField(
        max_digits=10,
        decimal_places=2,
        default=0
    )

    # Guarda una fecha sin hora (aquí: la noche de la reserva o la fecha del evento).
    # null=True permite que la base de datos guarde NULL si no hay fecha.
    # blank=True permite dejar el campo vacío en formularios.
    cierre_previsto = models.DateField(null=True, blank=True, verbose_name='fecha prevista')

    # Configuración adicional del modelo.
    class Meta:

        # Ordena los resultados por id de mayor a menor;
        # normalmente, las oportunidades creadas más recientemente aparecen primero.
        ordering = ['-id']

        # Nombre plural que Django muestra para este modelo en el panel de administración.
        verbose_name_plural = 'oportunidades'

    # Define cómo se representa una oportunidad como texto.
    # Por ejemplo, en listas desplegables se verá su título.
    def __str__(self):
        return self.titulo
