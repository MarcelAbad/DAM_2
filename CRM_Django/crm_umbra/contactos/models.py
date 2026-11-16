"""
MODELOS de Contactos. Un modelo = una tabla; cada atributo = una columna.
Tras CUALQUIER cambio aquí ejecuta:
    python manage.py makemigrations     # prepara los cambios
    python manage.py migrate            # los aplica a la base de datos
(Si usas iniciar.sh / iniciar.bat, se hace solo al arrancar.)

UMBRA CRM: los "contactos" son las personas que se relacionan con la discoteca:
clubbers (público), VIP, promotores, DJs y proveedores.
"""
from datetime import date          # Para calcular la edad a partir de la fecha de nacimiento

from django.db import models


class Contacto(models.Model):
    # choices: lista de pares (valor_guardado_en_BD, texto_visible).
    # TODO-ALUMNO 1 (HECHO): tipos de contacto propios de una discoteca.
    TIPOS = [
        ('clubber', 'Clubber'),          # Público que viene a bailar (el tipo por defecto)
        ('vip', 'VIP Member'),           # Cliente con rango VIP (mesas, accesos anticipados)
        ('promotor', 'Promotor'),        # Persona que trae público (RRPP)
        ('dj', 'DJ / Artista'),          # Artistas que pinchan en las sesiones
        ('proveedor', 'Proveedor'),      # Bebidas, sonido, seguridad, limpieza...
        ('otro', 'Otro'),
    ]

    nombre = models.CharField(max_length=120)                        # Texto corto. max_length es OBLIGATORIO
    tipo = models.CharField(max_length=20, choices=TIPOS, default='clubber')  # choices -> desplegable en formularios
    email = models.EmailField(blank=True)                            # blank=True -> se puede dejar vacío
    telefono = models.CharField(max_length=30, blank=True)           # Teléfono como texto (admite +34, espacios)
    notas = models.TextField(blank=True)                             # Texto largo
    creado = models.DateTimeField(auto_now_add=True)                 # auto_now_add -> fecha de creación automática

    # TODO-ALUMNO 2 (HECHO): dos campos propios de la temática "discoteca".
    # 1) Fecha de nacimiento: sirve para comprobar que el cliente es mayor de edad.
    #    null=True -> la BD admite vacío; blank=True -> el formulario también.
    fecha_nacimiento = models.DateField(null=True, blank=True, verbose_name='fecha de nacimiento')
    # 2) Frecuencia Coins: la moneda de fidelidad de la app UMBRA (se gana votando).
    #    PositiveIntegerField = número entero que nunca es negativo; empieza en 0.
    monedas = models.PositiveIntegerField(default=0, verbose_name='Frecuencia Coins',
                                          help_text='Puntos canjeables que ha ganado votando')

    class Meta:
        ordering = ['nombre']              # Orden por defecto. '-campo' = descendente

    def __str__(self):
        return self.nombre                 # Cómo se muestra el objeto en admin, desplegables, títulos

    @property
    def edad(self):
        """Edad en años (o None si no tenemos la fecha de nacimiento). No se guarda en la BD: se calcula."""
        if not self.fecha_nacimiento:                      # Sin fecha no podemos calcular nada
            return None
        hoy = date.today()                                  # Fecha de hoy
        # Restamos los años y, si todavía no ha cumplido años este año, restamos 1 más.
        # (hoy.month, hoy.day) < (mes, día de nacimiento) es True si aún no ha llegado su cumpleaños.
        return hoy.year - self.fecha_nacimiento.year - (
            (hoy.month, hoy.day) < (self.fecha_nacimiento.month, self.fecha_nacimiento.day))

    @property
    def es_mayor_de_edad(self):
        """True si tiene 18 años o más. Si no hay fecha, devuelve None (desconocido)."""
        return None if self.edad is None else self.edad >= 18


# ---------------------------------------------------------------------------
# CATÁLOGO DE CAMPOS (referencia; todo comentado)
# CharField(max_length=N) texto corto | TextField() texto largo | EmailField() | URLField()
# IntegerField() / PositiveIntegerField() enteros | FloatField() decimal aproximado (no para dinero)
# DecimalField(max_digits=10, decimal_places=2) dinero | BooleanField(default=False) sí/no
# DateField() fecha | DateTimeField() fecha y hora | ImageField(upload_to='fotos/') (necesita Pillow)
# Opciones: null=True (BD admite vacío) · blank=True (formulario admite vacío) · default=valor
#           unique=True (sin repetidos) · choices=[(valor, 'Texto')] · verbose_name='Etiqueta'
#           help_text='Ayuda' · auto_now_add=True (fecha al crear) · auto_now=True (fecha al guardar)
# Relaciones: ForeignKey(Modelo, on_delete=..., related_name='x') muchos-a-uno
#             ManyToManyField(Modelo, blank=True) muchos-a-muchos | OneToOneField(Modelo, on_delete=...) uno-a-uno
# on_delete: CASCADE | PROTECT | SET_NULL (con null=True) | SET_DEFAULT | DO_NOTHING
# ---------------------------------------------------------------------------
