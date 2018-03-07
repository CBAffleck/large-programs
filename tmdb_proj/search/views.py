from django.shortcuts import render
from django.http import HttpResponse
from django.views import generic
import operator
import requests


# Create your views here.

def index(request):
	return render(request, 'search/index.html')

def search_results(request):
	if request.method == 'GET':
		search_query = request.GET.get('search_box', None)
		context = get_movie_title_id_dict(search_query)
		return render(request, 'search/search_results.html', context)

#Takes the title the user put in and looks it up. If there's more than one movie with that title, then the user
#is asked to select the correct one. The function returns the TMDB movie id.
def get_movie_title_id_dict(movie_title):
	input_title_compressed = movie_title.replace(" ", "+")
	url = "https://api.themoviedb.org/3/search/movie?api_key=70f9ef47636a6a60f21572480e7758e6&query=" + input_title_compressed
	response = requests.get(url)
	dict = response.json()

	results = dict['results']
	titles = {}
	for d in results:
		if d['title'].find(movie_title) == 0:
			titles[d['title']] = [d['title'], d['id'], d['release_date']]
	return titles