#TMDB API key(v3): 70f9ef47636a6a60f21572480e7758e6

import operator
import requests

#Provides choice for user and then takes the users selection
print("0 | Movie \n1 | TV Show")
input_type = input("What are you looking for? Please enter 0 or 1. ").rstrip()
if int(input_type) == 0:
	input_title = input("What movie are you looking for? (Capitalization is important!) ").rstrip()
	inType = "movie"
else:
	input_title = input("What TV show are you looking for? (Capitalization is important!) ").rstrip()
	inType = "tv"

#Takes the title the user put in and looks it up. If there's more than one movie with that title, then the user
#is asked to select the correct one. The function returns the TMDB movie id.
def get_movie_id(movie_title):
	input_title_compressed = movie_title.replace(" ", "+")
	url = "https://api.themoviedb.org/3/search/movie?api_key=70f9ef47636a6a60f21572480e7758e6&query=" + input_title_compressed
	response = requests.get(url)
	dict = response.json()

	results = dict['results']
	titles = [[d['title'], d['id'], d['release_date']] for d in results if d['title'].find(movie_title) == 0]
	if len(titles) == 1:
		return titles[0][1]
	else:
		count = 0
		for x in titles:
			print(str(count) + " | " + x[0] + " | Release date: " + x[2])
			count += 1
		correct_movie = int(input("Which number corresponds to the movie you're looking for? "))
		return titles[correct_movie][1]

#Takes the title the user put in and looks it up. If there's more than one tv show with that title, then the user
#is asked to select the correct one. The function returns the TMDB tv show id.
def get_tv_show_id(tv_show_title):
	input_title_compressed = tv_show_title.replace(" ", "+")
	url = "https://api.themoviedb.org/3/search/tv?api_key=70f9ef47636a6a60f21572480e7758e6&query=" + input_title_compressed
	response = requests.get(url)
	dict = response.json()

	results = dict['results']
	titles = [[d['name'], d['id']] for d in results]
	if len(titles) == 1:
		return titles[0][1]
	else:
		count = 0
		for x in titles:
			print(str(count) + " | " + x[0])
			count += 1
		correct_show = int(input("Which number corresponds to the TV show you're looking for? "))
		return titles[correct_show][1]

#Gets the id corresponding to the user's input
if inType == "movie":
	inType_id = get_movie_id(input_title)
else:
	inType_id = get_tv_show_id(input_title)

#Gets the movie's info based on the id
def get_movie_info(movie_id):
	url = "http://api.themoviedb.org/3/movie/" + str(movie_id) + "?language=en-US&api_key=70f9ef47636a6a60f21572480e7758e6"
	response = requests.get(url)
	dict = response.json()
	return dict

#Gets the tv show's info based on the id
def get_tv_show_info(show_id):
	url = "http://api.themoviedb.org/3/tv/" + str(show_id) + "?language=en-US&api_key=70f9ef47636a6a60f21572480e7758e6"
	response = requests.get(url)
	dict = response.json()
	return dict

#Gets the info corresponding to the user's input 
if inType == "movie":
	inType_info = get_movie_info(inType_id)
else:
	inType_info = get_tv_show_info(inType_id)

#Returns a list of genres for the movie
def get_movie_genres(movie_info):
	return [d['name'] for d in movie_info['genres']]

#Returns a list of genres for the tv show
def get_tv_show_genres(show_info):
	return [d['name'] for d in show_info['genres']]

#Gets the average of runtimes for the episodes in the tv show
def get_avg_episode_runtime(show_info):
	runtimes = show_info['episode_run_time']
	avg = 0
	for x in runtimes:
		avg += x
	avg = avg/len(runtimes)
	return avg

#Prints different stats based on whether the user's input was a movie or tv show
if inType == "movie":
	print("Info for " + inType_info['title'])
	print("Runtime: " + str(inType_info['runtime']))
	print("Genres:", get_movie_genres(inType_info))
else:
	print("Info for " + inType_info['name'])
	print("Average Episode Runtime: " + str(get_avg_episode_runtime(inType_info)))
	print("Number of Episodes: " + str(inType_info['number_of_episodes']))
	print("Number of Seasons: " + str(inType_info['number_of_seasons']))
	print("Genres:", get_tv_show_genres(inType_info))


