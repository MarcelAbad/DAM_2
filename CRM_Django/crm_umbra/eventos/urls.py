"""URLs de eventos. Se montan bajo /eventos/ desde crm_project/urls.py."""

# Importa path para asociar direcciones con vistas.
from django.urls import path

# Importa las vistas de esta app.
from . import views

# Espacio de nombres: en plantillas {% url 'eventos:index' %}.
app_name = 'eventos'

urlpatterns = [
    path('', views.index, name='index'),                 # /eventos/
    path('nuevo/', views.crear, name='crear'),           # /eventos/nuevo/
    path('<int:pk>/', views.detalle, name='detalle'),    # /eventos/3/
    path('<int:pk>/editar/', views.editar, name='editar'),
    path('<int:pk>/borrar/', views.borrar, name='borrar'),
]
