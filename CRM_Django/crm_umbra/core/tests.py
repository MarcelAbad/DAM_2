"""
PRUEBAS AUTOMÁTICAS de UMBRA CRM. Ejecuta:  python manage.py test
Cada prueba comprueba una tarea del enunciado (TODO 0-10 y Niveles 2, 3 y 4).
Django crea una base de datos temporal para las pruebas: no toca db.sqlite3.
"""
from datetime import date, timedelta                      # Fechas para crear datos de prueba

from django.contrib.auth.models import User               # Usuario para iniciar sesión en las pruebas
from django.core.management import call_command           # Para ejecutar cargar_ejemplo desde el código
from django.test import TestCase                          # Clase base de pruebas de Django
from django.urls import reverse                           # Convierte el nombre de una URL en su dirección

from contactos.models import Contacto
from interacciones.models import Interaccion
from oportunidades.models import Oportunidad
from tareas.models import Tarea
from eventos.models import Evento


class UmbraCRMTests(TestCase):

    def setUp(self):
        """Se ejecuta antes de CADA prueba: carga los datos de ejemplo y entra como admin."""
        call_command('cargar_ejemplo', verbosity=0)        # Mismo comando que usa iniciar.bat
        self.client.login(username='admin', password='admin1234')   # Inicia sesión

    # ---------- Datos de ejemplo (TODO 10) ----------
    def test_datos_de_ejemplo(self):
        """Hay al menos 8 contactos y datos en los 4 módulos + eventos."""
        self.assertGreaterEqual(Contacto.objects.count(), 8)
        self.assertGreater(Oportunidad.objects.count(), 0)
        self.assertGreater(Interaccion.objects.count(), 0)
        self.assertGreater(Tarea.objects.count(), 0)
        self.assertGreater(Evento.objects.count(), 0)

    def test_cargar_ejemplo_no_duplica(self):
        """Ejecutarlo otra vez no duplica los contactos."""
        antes = Contacto.objects.count()
        call_command('cargar_ejemplo', verbosity=0)
        self.assertEqual(Contacto.objects.count(), antes)

    # ---------- Todas las páginas cargan (funcionamiento sin errores) ----------
    def test_todas_las_paginas_cargan(self):
        """Panel y listados de los 5 módulos responden 200 (sin errores)."""
        for nombre in ['inicio', 'contactos:index', 'oportunidades:index',
                       'interacciones:index', 'tareas:index', 'eventos:index']:
            with self.subTest(pagina=nombre):               # subTest: si falla una, indica cuál
                self.assertEqual(self.client.get(reverse(nombre)).status_code, 200)

    def test_sin_login_redirige(self):
        """Sin iniciar sesión, el panel redirige al login."""
        self.client.logout()
        self.assertEqual(self.client.get(reverse('inicio')).status_code, 302)

    # ---------- Nivel 1 ----------
    def test_tipos_etapas_canales_prioridad(self):
        """TODO 1, 3, 4 y 5: tipos de contacto, ≥4 etapas, canal nuevo y prioridad."""
        self.assertIn('dj', dict(Contacto.TIPOS))
        self.assertGreaterEqual(len(Oportunidad.ETAPAS), 4)
        self.assertIn('ganado', dict(Oportunidad.ETAPAS))   # El panel depende de este valor
        self.assertIn('whatsapp', dict(Interaccion.CANALES))
        self.assertEqual(set(dict(Tarea.PRIORIDADES)), {'alta', 'media', 'baja'})

    def test_campos_propios_en_formulario_y_ficha(self):
        """TODO 2: fecha_nacimiento y monedas se pueden crear y se ven en la ficha."""
        respuesta = self.client.post(reverse('contactos:crear'), {
            'nombre': 'Prueba Test', 'tipo': 'clubber', 'email': '', 'telefono': '',
            'fecha_nacimiento': '2000-05-20', 'monedas': 77, 'notas': ''})
        c = Contacto.objects.get(nombre='Prueba Test')
        self.assertRedirects(respuesta, reverse('contactos:detalle', args=[c.pk]))
        self.assertEqual(c.monedas, 77)
        ficha = self.client.get(reverse('contactos:detalle', args=[c.pk])).content.decode()
        self.assertIn('77', ficha)                          # Las monedas aparecen en la ficha
        self.assertIn('Edad', ficha)                        # Y la edad calculada

    def test_edad_y_mayoria_de_edad(self):
        """La edad se calcula bien y se detecta al menor de edad."""
        hoy = date.today()
        mayor = Contacto(nombre='A', fecha_nacimiento=date(hoy.year - 30, 1, 1))
        menor = Contacto(nombre='B', fecha_nacimiento=date(hoy.year - 10, 1, 1))
        self.assertEqual(mayor.edad, 30)
        self.assertTrue(mayor.es_mayor_de_edad)
        self.assertFalse(menor.es_mayor_de_edad)
        self.assertIsNone(Contacto(nombre='C').edad)         # Sin fecha -> None

    def test_tarea_con_prioridad(self):
        """TODO 5: la prioridad se guarda desde el formulario de tareas."""
        self.client.post(reverse('tareas:crear'), {
            'titulo': 'Tarea test', 'vence': '2030-01-01', 'prioridad': 'alta'})
        self.assertEqual(Tarea.objects.get(titulo='Tarea test').prioridad, 'alta')

    # ---------- Nivel 2 ----------
    def test_filtro_contactos_por_tipo(self):
        """TODO 7: ?tipo=dj solo muestra DJs."""
        html = self.client.get(reverse('contactos:index'), {'tipo': 'dj'}).content.decode()
        self.assertIn('Kairo', html)                         # Es DJ
        self.assertNotIn('Ana García', html)                 # Es clubber

    def test_filtro_tipo_invalido_no_rompe(self):
        """Un tipo inventado en la URL se ignora (no da error)."""
        self.assertEqual(self.client.get(reverse('contactos:index'), {'tipo': 'zzz'}).status_code, 200)

    def test_filtro_oportunidades_por_etapa(self):
        """TODO 7: ?etapa=perdido solo muestra las canceladas."""
        html = self.client.get(reverse('oportunidades:index'), {'etapa': 'perdido'}).content.decode()
        self.assertIn('Pack de sonido sesión VIP', html)     # Está 'perdido'
        self.assertNotIn('Despedida de soltero', html)       # Está en 'consulta'

    def test_marcar_como_ganada_solo_post(self):
        """TODO 8: el botón cambia la etapa a 'ganado'; por GET no hace nada (405)."""
        op = Oportunidad.objects.get(titulo='Despedida de soltero')
        url = reverse('oportunidades:ganar', args=[op.pk])
        self.assertEqual(self.client.get(url).status_code, 405)      # GET rechazado
        op.refresh_from_db()
        self.assertEqual(op.etapa, 'consulta')                       # Sigue igual
        self.assertRedirects(self.client.post(url), reverse('oportunidades:detalle', args=[op.pk]))
        op.refresh_from_db()
        self.assertEqual(op.etapa, 'ganado')                         # Ahora sí cambia

    def test_boton_ganar_en_la_ficha(self):
        """La ficha muestra el botón si la oportunidad no está ganada, y no si ya lo está."""
        pendiente = Oportunidad.objects.get(titulo='Despedida de soltero')
        ganada = Oportunidad.objects.get(titulo='Suministro de bebidas del mes')
        self.assertIn('Marcar como ganada',
                      self.client.get(reverse('oportunidades:detalle', args=[pendiente.pk])).content.decode())
        self.assertNotIn('Marcar como ganada',
                         self.client.get(reverse('oportunidades:detalle', args=[ganada.pk])).content.decode())

    def test_metrica_tareas_vencidas(self):
        """TODO 9: el panel cuenta las tareas vencidas (y no las hechas)."""
        # En los datos de ejemplo hay 1 vencida pendiente y 1 vencida ya hecha.
        respuesta = self.client.get(reverse('inicio'))
        self.assertEqual(respuesta.context['vencidas'], 1)
        # Si añadimos otra vencida sin hacer, pasa a 2.
        Tarea.objects.create(titulo='Otra', vence=date.today() - timedelta(days=1))
        self.assertEqual(self.client.get(reverse('inicio')).context['vencidas'], 2)

    def test_panel_suma_importe_ganado(self):
        """El panel suma las oportunidades con etapa 'ganado' (1500 + 3200 + 1800)."""
        self.assertEqual(self.client.get(reverse('inicio')).context['ganado'], 6500)

    # ---------- Nivel 3 (elegidas: importe por etapa y exportar CSV) ----------
    def test_importe_total_por_etapa(self):
        """La lista de oportunidades incluye el total por etapa calculado con Sum."""
        totales = self.client.get(reverse('oportunidades:index')).context['totales']
        por_etapa = {t['etapa']: t for t in totales}
        self.assertEqual(por_etapa['Confirmada y pagada']['total'], 6500)
        self.assertEqual(por_etapa['Confirmada y pagada']['n'], 3)
        self.assertEqual(por_etapa['Cancelada']['total'], 800)

    def test_exportar_contactos_csv(self):
        """Se descarga un CSV con todos los contactos, y respeta el filtro de tipo."""
        respuesta = self.client.get(reverse('contactos:exportar'))
        self.assertEqual(respuesta.status_code, 200)
        self.assertIn('text/csv', respuesta['Content-Type'])
        self.assertIn('attachment', respuesta['Content-Disposition'])
        filas = respuesta.content.decode('utf-8-sig').strip().splitlines()
        self.assertEqual(len(filas), 1 + Contacto.objects.count())   # Cabecera + 1 por contacto
        solo_djs = self.client.get(reverse('contactos:exportar'), {'tipo': 'dj'}).content.decode('utf-8-sig')
        self.assertIn('Kairo', solo_djs)
        self.assertNotIn('Ana García', solo_djs)

    # ---------- Nivel 4: módulo propio ----------
    def test_modulo_eventos(self):
        """El módulo eventos aparece en el menú y permite crear una noche."""
        self.assertIn('/eventos/', self.client.get(reverse('inicio')).content.decode())   # Enlace en el menú
        dj = Contacto.objects.get(nombre='Kairo')
        respuesta = self.client.post(reverse('eventos:crear'), {
            'nombre': 'Noche test', 'fecha': '2030-02-02', 'estilo': 'techno',
            'aforo': 200, 'dj': dj.pk, 'votacion_abierta': 'on'})
        e = Evento.objects.get(nombre='Noche test')
        self.assertRedirects(respuesta, reverse('eventos:detalle', args=[e.pk]))

    def test_eventos_solo_permite_djs(self):
        """El desplegable de DJ solo ofrece contactos de tipo DJ (limit_choices_to)."""
        clubber = Contacto.objects.get(nombre='Ana García')
        respuesta = self.client.post(reverse('eventos:crear'), {
            'nombre': 'Mal', 'fecha': '2030-02-02', 'estilo': 'techno',
            'aforo': 100, 'dj': clubber.pk})
        self.assertEqual(respuesta.status_code, 200)         # Se queda en el formulario (error)
        self.assertFalse(Evento.objects.filter(nombre='Mal').exists())
