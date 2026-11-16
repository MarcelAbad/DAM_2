"""
Comando:  python manage.py cargar_ejemplo
Crea el usuario admin (clave admin1234) y datos de ejemplo para que el CRM no esté vacío.
Es seguro ejecutarlo varias veces: no duplica datos.
TODO-ALUMNO 10 (HECHO): datos de ejemplo de UMBRA (discoteca): 12 contactos, 9 reservas/contratos,
6 interacciones, 6 tareas y 3 noches. Todos los nombres y datos son inventados.
"""
from datetime import date, timedelta
from django.apps import apps
from django.contrib.auth.models import User
from django.core.management.base import BaseCommand

class Command(BaseCommand):
    help = 'Carga usuario admin y datos de ejemplo'

    def handle(self, *args, **opciones):
        # Crea el superusuario si no existe todavía (usuario admin / clave admin1234)
        if not User.objects.filter(username='admin').exists():
            User.objects.create_superuser('admin', 'admin@example.com', 'admin1234')
            self.stdout.write('Usuario creado: admin / admin1234')
        try:
            Contacto = apps.get_model('contactos', 'Contacto')
        except LookupError:
            return
        if Contacto.objects.exists():                      # Si ya hay datos, no duplicamos
            self.stdout.write('Ya había datos; no se carga nada más.')
            return

        hoy = date.today()                                  # Fecha de hoy, para crear fechas relativas

        # ---- CONTACTOS (12): clubbers, VIP, promotores, DJs y proveedores ----
        # Campos propios: fecha_nacimiento y monedas (Frecuencia Coins).
        def nuevo(nombre, tipo, nacimiento=None, monedas=0, email='', telefono='', notas=''):
            """Pequeña función auxiliar para no repetir Contacto.objects.create(...) 12 veces."""
            return Contacto.objects.create(nombre=nombre, tipo=tipo, fecha_nacimiento=nacimiento,
                                           monedas=monedas, email=email, telefono=telefono, notas=notas)

        ana = nuevo('Ana García', 'clubber', date(1999, 4, 12), 650, 'ana@example.com', '600111222', 'Viene casi todos los sábados')
        luis = nuevo('Luis Pérez', 'clubber', date(2001, 9, 3), 120, 'luis@example.com', '600333444')
        marta = nuevo('Marta Ruiz', 'vip', date(1995, 2, 20), 1200, 'marta@example.com', '600555666', 'Reserva mesa en su cumpleaños')
        carlos = nuevo('Carlos Medina', 'vip', date(1992, 11, 30), 2100, 'carlos@example.com', '600777888', 'Cliente VIP desde la primera noche')
        sofia = nuevo('Sofía Navarro', 'clubber', date(2002, 6, 15), 340, 'sofia@example.com')
        ivan = nuevo('Iván Torres', 'promotor', date(1993, 7, 8), 0, 'ivan@example.com', '600999000', 'Trae grupos de 15-20 personas')
        lucia = nuevo('Lucía Campos', 'promotor', date(1996, 1, 25), 0, 'lucia@example.com')
        kairo = nuevo('Kairo', 'dj', date(1990, 3, 14), 0, 'kairo@example.com', '611222333', 'Melodic techno / house')
        nocturna = nuevo('Nocturna', 'dj', date(1994, 10, 9), 0, 'nocturna@example.com', notas='Techno')
        bebidas = nuevo('Bebidas Levante SL', 'proveedor', None, 0, 'pedidos@bebidas.example', notas='Suministro mensual')
        sonido = nuevo('Sonido Pro Madrid', 'proveedor', None, 0, 'info@sonidopro.example')
        # Menor de edad a propósito: en su ficha aparece el aviso "(menor de edad: no puede entrar)".
        pablo = nuevo('Pablo Serrano', 'clubber', date(hoy.year - 17, 6, 1), 0, 'pablo@example.com', notas='Dato de ejemplo: menor de edad')

        # ---- RESERVAS Y CONTRATOS (oportunidades): una en cada etapa o más ----
        try:
            Op = apps.get_model('oportunidades', 'Oportunidad')
            Op.objects.create(titulo='Mesa VIP cumpleaños de Marta', contacto=marta, etapa='reservado', importe=600, cierre_previsto=hoy + timedelta(days=12))
            Op.objects.create(titulo='Mesa para 4 personas', contacto=ana, etapa='presupuesto', importe=240, cierre_previsto=hoy + timedelta(days=5))
            Op.objects.create(titulo='Contrato DJ · Frecuencia Vol. 06', contacto=kairo, etapa='ganado', importe=1500, cierre_previsto=hoy - timedelta(days=20))
            Op.objects.create(titulo='Contrato DJ · Frecuencia Vol. 07', contacto=nocturna, etapa='presupuesto', importe=1200, cierre_previsto=hoy + timedelta(days=30))
            Op.objects.create(titulo='Reserva de grupo (20 personas)', contacto=ivan, etapa='consulta', importe=900, cierre_previsto=hoy + timedelta(days=18))
            Op.objects.create(titulo='Suministro de bebidas del mes', contacto=bebidas, etapa='ganado', importe=3200)
            Op.objects.create(titulo='Pack de sonido sesión VIP', contacto=sonido, etapa='perdido', importe=800)
            Op.objects.create(titulo='Zona privada cumpleaños de Carlos', contacto=carlos, etapa='ganado', importe=1800, cierre_previsto=hoy + timedelta(days=9))
            Op.objects.create(titulo='Despedida de soltero', contacto=luis, etapa='consulta', importe=450, cierre_previsto=hoy + timedelta(days=40))
        except LookupError:
            pass

        # ---- INTERACCIONES: usan los canales llamada, whatsapp, instagram, email y reunión ----
        try:
            Inter = apps.get_model('interacciones', 'Interaccion')
            admin = User.objects.get(username='admin')      # El autor será el usuario admin
            Inter.objects.create(contacto=marta, canal='whatsapp', resumen='Confirma que quiere la mesa VIP el día de su cumpleaños', autor=admin)
            Inter.objects.create(contacto=ivan, canal='llamada', resumen='Pregunta por precios para un grupo de 20 personas', autor=admin)
            Inter.objects.create(contacto=ana, canal='instagram', resumen='Escribe por mensaje directo pidiendo una mesa', autor=admin)
            Inter.objects.create(contacto=nocturna, canal='email', resumen='Enviado el contrato para la próxima sesión', autor=admin)
            Inter.objects.create(contacto=kairo, canal='reunion', resumen='Reunión para cerrar el cartel de la noche', autor=admin)
            Inter.objects.create(contacto=bebidas, canal='nota', resumen='Pendiente de revisar el albarán de octubre', autor=admin)
        except LookupError:
            pass

        # ---- TAREAS: con prioridad; una de ellas está VENCIDA (para que se vea la métrica del panel) ----
        try:
            Tarea = apps.get_model('tareas', 'Tarea')
            Tarea.objects.create(titulo='Cobrar la señal de la mesa VIP', contacto=marta, vence=hoy + timedelta(days=3), prioridad='alta')
            Tarea.objects.create(titulo='Enviar presupuesto de la mesa', contacto=ana, vence=hoy + timedelta(days=1), prioridad='media')
            Tarea.objects.create(titulo='Llamar al promotor para cerrar el grupo', contacto=ivan, vence=hoy - timedelta(days=2), prioridad='alta')   # VENCIDA
            Tarea.objects.create(titulo='Revisar el rider técnico del DJ', contacto=kairo, vence=hoy + timedelta(days=10), prioridad='baja')
            Tarea.objects.create(titulo='Pedir presupuesto de sonido', contacto=sonido, vence=hoy - timedelta(days=5), prioridad='media', hecha=True)  # Hecha
            Tarea.objects.create(titulo='Actualizar la lista de invitados', vence=hoy + timedelta(days=2), prioridad='media')   # Sin contacto
        except LookupError:
            pass

        # ---- EVENTOS (módulo propio): tres noches ----
        try:
            Evento = apps.get_model('eventos', 'Evento')
            Evento.objects.create(nombre='Frecuencia Vol. 06', fecha=hoy - timedelta(days=20), estilo='melodic', aforo=400, dj=kairo, votacion_abierta=False)
            Evento.objects.create(nombre='Frecuencia Vol. 07', fecha=hoy + timedelta(days=21), estilo='melodic', aforo=400, dj=nocturna, votacion_abierta=True)
            Evento.objects.create(nombre='Noche Techno Underground', fecha=hoy + timedelta(days=35), estilo='techno', aforo=250, dj=nocturna, votacion_abierta=True)
        except LookupError:
            pass

        self.stdout.write('Datos de ejemplo cargados.')
