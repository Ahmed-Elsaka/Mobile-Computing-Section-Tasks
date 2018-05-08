import pandas as pd 
import json 
import requests
from googlemaps import convert
import gmplot


def getLatAndLang():
	df = pd.read_csv('df_gps.csv')
	lng = df['gps_longitude']
	lat = df['gps_latitude']
	return lat,lng 

def mapMatch(latitude, longitude):
	lat_lng_list = []
	n_lat = []
	n_lng = []

	for i in range(len(latitude)):
		lat_lng_list.append(str(latitude[i])+','+str(longitude[i]))

	for j in range(0,len(lat_lng_list),20):
		locations = convert.location_list(lat_lng_list[j:j+20])
		params = {"path": locations, 'interpolate': "true", 'key': "AIzaSyBVtDB16c0FTGw1-L8iBuEdaQ937nwNXyI"}
		response = requests.get("https://roads.googleapis.com/v1/snapToRoads", params=params)
		newGpsValues  = json.loads(response.text)
		for k in range(len(newGpsValues['snappedPoints'])):
			n_lat.append(newGpsValues['snappedPoints'][k]['location']['latitude'])
			n_lng.append(newGpsValues['snappedPoints'][k]['location']['longitude'])
	return n_lat,n_lng

def main():
	lat, lon = getLatAndLang()
	nlat, nlong = mapMatch(lat, lon)
	# Initialize the map to the first location in the list
	gmap = gmplot.GoogleMapPlotter(nlat[0], nlong[0], 16)
	#draw
	gmap.polygon(nlat, nlong, '#FF6666', edge_width=10)
	# Write the map in an HTML file
	gmap.draw('map.html')
if __name__ == '__main__':
	main()