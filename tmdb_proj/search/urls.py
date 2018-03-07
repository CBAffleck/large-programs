from django.urls import path

from . import views

app_name = 'search'
urlpatterns = [
	path('', views.index, name='index'),
	path('search_results/', views.search_results, name='search_results'),
]
