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
		search_query = request.GET.get('search_box', None).rstrip()
		movie_titles_list = get_movie_title_id_dict(search_query)
		tv_show_titles_list = get_tv_show_title_id(search_query)
		titles_dict = {'movie_titles':movie_titles_list, 'tv_show_titles':tv_show_titles_list}
		return render(request, 'search/search_results.html', titles_dict)

#Takes the title the user put in and looks it up. If there's more than one movie with that title, then the user
#is asked to select the correct one. The function returns the TMDB movie id.
def get_movie_title_id_dict(movie_title):
	input_title_compressed = movie_title.replace(" ", "+")
	url = "https://api.themoviedb.org/3/search/movie?api_key=70f9ef47636a6a60f21572480e7758e6&query=" + input_title_compressed
	response = requests.get(url)
	dict = response.json()

	results = dict['results']
	titles = [[d['title'], d['id'], d['release_date'], d['overview'], d['poster_path']] for d in results if d['title'].find(movie_title) == 0]
	return titles

#Takes the title the user put in and looks it up. If there's more than one tv show with that title, then the user
#is asked to select the correct one. The function returns the TMDB tv show id.
def get_tv_show_title_id(tv_show_title):
	input_title_compressed = tv_show_title.replace(" ", "+")
	url = "https://api.themoviedb.org/3/search/tv?api_key=70f9ef47636a6a60f21572480e7758e6&query=" + input_title_compressed
	response = requests.get(url)
	dict = response.json()

	results = dict['results']
	titles = [[d['name'], d['id'], d['first_air_date'], d['overview'], d['poster_path']] for d in results]
	return titles

def movie_info(request, movie_id):
	info_list = get_movie_info(movie_id)
	info_dict = {'movie_info':info_list}
	return render(request, 'search/movie_info.html', info_dict)

def tv_show_info(request, tv_show_id):
	info_list = get_tv_show_info(tv_show_id)
	info_dict = {'tv_show_info':info_list}
	return render(request, 'search/tv_show_info.html', info_dict)

#Gets the movie's info based on the id
def get_movie_info(movie_id):
	url = "http://api.themoviedb.org/3/movie/" + str(movie_id) + "?language=en-US&api_key=70f9ef47636a6a60f21572480e7758e6"
	response = requests.get(url)
	dict = response.json()

	results = [dict['overview'], dict['release_date'], dict['genres'], dict['runtime'], dict['title'], dict['poster_path']]
	return results

#Gets the tv show's info based on the id
def get_tv_show_info(tv_show_id):
	url = "http://api.themoviedb.org/3/tv/" + str(tv_show_id) + "?language=en-US&api_key=70f9ef47636a6a60f21572480e7758e6"
	response = requests.get(url)
	dict = response.json()

	results = [dict['name'], dict['overview'], dict['first_air_date'], dict['number_of_seasons'], dict['number_of_episodes'], dict['status'], dict['poster_path']]
	return results




