from django.urls import path

from . import views

app_name = 'search'
urlpatterns = [
	path('', views.index, name='index'),
	path('search_results/', views.search_results, name='search_results'),
	path('<int:movie_id>/movie_info/', views.movie_info, name='movie_info'),
	path('<int:tv_show_id>/tv_show_info/', views.tv_show_info, name='tv_show_info'),
]
