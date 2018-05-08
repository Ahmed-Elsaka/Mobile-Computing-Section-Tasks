# Dead Reckoning : compute new location based on previous location, displacement taken and angle.



import pandas as pd 
import geopy
import math 
from geopy.distance import VincentyDistance
import gmplot

def getDataFromCsvFiles():
	#read csv files 
	dfForGPS 	 = pd.read_csv(r"df_gps.csv")
	dfForSensors = pd.read_csv(r"df_sensors.csv")
	#get values "gps_latitude",""gps_longitude" Columns from "df_gps.csv" file
	lat = dfForGPS["gps_latitude"]
	lng = dfForGPS["gps_longitude"]
	#print(lat)
	#read acc Accelerator , seconds and  azimuth  columns from "df_sensors.csv" file 
	Acc_x = dfForSensors["Accelerator_x"]
	Acc_y = dfForSensors["Accelerator_y"]
	Acc_z = dfForSensors["Accelerator_z"]
	azimuth   = dfForSensors['Azimuth']
	seconds = dfForSensors['seconds']
	return lat[0], lng[0], Acc_x,Acc_y,Acc_z , azimuth, seconds

def getPredictedDisAndAzmuth(Acc_x, Acc_y,Acc_z,azimuth,seconds):
	# in this function we calculate displacement and azmimuth for part of samples in each step 
	# in this code i have choosed to calulate every 20 (step =20) sample
	Azimuth = azimuth[0]			
	v_current = 0 
	displacement = 0 
	t = seconds[0]
	step = 20          
	#lists for velocity and displacement azimuth
	velocity = []
	dis_list = []
	azimuth_list = []
	length = len(seconds)
	count = 0
	for i in range(length):
		if(count == step):
			velocity.append(v_current)
			dis_list.append(displacement)
			azimuth_list.append(float(azimuth[i]))
			count =0 
			v_current = 0 
			displacement = 0
		acc_Mag = math.sqrt(((Acc_x[i]**2) + (Acc_y[i]**2) + (Acc_z[i]**2)))
		deltaT = (float(seconds[i]-t))
		t = float(seconds[i])
		v_current = v_current + acc_Mag *deltaT
		#Compute displacement : from double integration of accelerometer
		displacement = displacement +(v_current *deltaT) +(0.5*acc_Mag *deltaT * deltaT) 
		count +=1
	return dis_list,azimuth_list

def getNewLatLong(startLat,startLong,dis_list,azimuth_list):
	length = len(dis_list)
	NewLat =[]
	NewLong = []
	NewLat.append(startLat)
	NewLong.append(startLong)
	for i in range(length):
		startPoint = geopy.Point(NewLat[i],NewLong[i])
		newPoint = VincentyDistance(meters=(dis_list[i])).destination(startPoint,azimuth_list[i])
		NewLat.append(newPoint.latitude)
		NewLong.append(newPoint.longitude)
	return NewLat, NewLong


def main():
	startLat,startLong,Acc_x, Acc_y,Acc_z,azimuth,seconds = getDataFromCsvFiles() 
	dis_list, azimuth_list = getPredictedDisAndAzmuth(Acc_x, Acc_y,Acc_z,azimuth,seconds)
	NewLat,NewLong = getNewLatLong(startLat,startLong,dis_list,azimuth_list)
	gmap = gmplot.GoogleMapPlotter(NewLat[0],NewLong[0],15)
	gmap.polygon(NewLat, NewLong, '#FF6666', edge_width=10)
	gmap.draw('Elsaka.html')


if __name__ == '__main__':
	main()