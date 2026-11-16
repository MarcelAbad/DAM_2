from datetime import date                                      # Fecha de hoy (para las tareas vencidas)

from django.apps import apps
from django.contrib.auth.decorators import login_required
from django.db.models import Sum
from django.shortcuts import render

def _modelo(app, nombre):
    """Devuelve el modelo o None si el módulo está desactivado en MODULOS_CRM (así el panel no se rompe)."""
    try:
        return apps.get_model(app, nombre)
    except LookupError:
        return None

@login_required   # Sin sesión iniciada -> redirige al login
def inicio(request):
    # Tarjetas con el número de objetos de cada módulo activo
    resumen = []
    for app, modelo, etiqueta in [('contactos', 'Contacto', 'Contactos'),
                                  ('oportunidades', 'Oportunidad', 'Reservas y contratos'),
                                  ('interacciones', 'Interaccion', 'Interacciones'),
                                  ('tareas', 'Tarea', 'Tareas'),
                                  ('eventos', 'Evento', 'Noches')]:       # Módulo propio (Nivel 4)
        M = _modelo(app, modelo)
        if M:
            resumen.append({'etiqueta': etiqueta, 'n': M.objects.count(), 'url': f'{app}:index'})

    ultimas, pendientes, ganado = [], None, None
    vencidas, monedas = None, None
    Inter = _modelo('interacciones', 'Interaccion')
    if Inter:
        ultimas = Inter.objects.select_related('contacto')[:5]      # Últimas 5 (el modelo ya ordena por -fecha)
    Tarea = _modelo('tareas', 'Tarea')
    if Tarea:
        pendientes = Tarea.objects.filter(hecha=False).count()      # filter(campo=valor) = WHERE
        # TODO-ALUMNO 9 (HECHO): métrica propia = TAREAS VENCIDAS.
        # vence__lt=date.today() significa "la fecha límite es ANTERIOR a hoy" (lt = less than);
        # hecha=False para no contar las que ya se han completado.
        vencidas = Tarea.objects.filter(hecha=False, vence__lt=date.today()).count()
    Op = _modelo('oportunidades', 'Oportunidad')
    if Op:
        ganado = Op.objects.filter(etapa='ganado').aggregate(t=Sum('importe'))['t'] or 0   # Sum = suma de la columna
    Contacto = _modelo('contactos', 'Contacto')
    if Contacto:
        # Segunda métrica propia: Frecuencia Coins que hay en circulación (suma de las monedas de todos los contactos).
        monedas = Contacto.objects.aggregate(t=Sum('monedas'))['t'] or 0

    return render(request, 'core/inicio.html', {
        'resumen': resumen, 'ultimas': ultimas, 'pendientes': pendientes, 'ganado': ganado,
        'vencidas': vencidas, 'monedas': monedas})
