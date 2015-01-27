package org.bfr.querytools.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.bfr.querytools.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleSpectrumQuery
{
	// API key. 
	private static String apiKey1 = "AIzaSyCX5QLIbuWlyRE32uRe9VkdmDjn7T-Won8";
	private static String apiKey2 = "";
	private static String apiKey3 = "AIzaSyBORRyv4vfrKJ6D73P0qJZjp-dgYlD9If4";
	private static String apiKey4 = "AIzaSyBw4Pt8NYYIwRo-9GHsMWbqzlWVLO90_5c";
	private static String apiKey5 = "AIzaSyDniefmKJNv42I6w7kEkDQc2QLkLs-omQ0";
	private static String apiKey6 = "AIzaSyCDriOhv2l_pXTMkTLab3oJk0zVstjY-Hw";
	private static String apiKey7 = "AIzaSyDdYFocs4Jdvp0DhfbPFNsvZeviNC9x6eo";
	private static String apiKey8 = "AIzaSyDESRe8rnVr4r-be1higwGmfAgE8nOS1CU";
	
	private static String apiKey9 = "AIzaSyCtRwDLeP8iUl-f4k8xeUPpUt6jYkDBir4";
	private static String apiKey10 = "AIzaSyB4sYk0TxrMElO2X4KjkhwjZBM2Xk49kuk";
	private static String apiKey11 = "AIzaSyCX5QLIbuWlyRE32uRe9VkdmDjn7T-Won8";
	private static String apiKey12 = "AIzaSyA9uNfnvSK1JpiYGd3VcCHM555U1ul7lxc";
	private static String apiKey13 = "AIzaSyA9uNfnvSK1JpiYGd3VcCHM555U1ul7lxc";
	private static String apiKey14 = "AIzaSyCAdizltwoqC3uDKpNH799inmGJHWrJJgM";
	private static String apiKey15 = "AIzaSyBFdDReJgCwrWZDuRdBeXITdYLkKLgaIn8";


	private static int methodCallCounter = 0;
//	private static int querySize = 45;//,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,38,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69,71,73,75};
	private static int querySize[] ={1,3,5,7,9,11,13,15,17,19};
	private static int querySizeController = 0;
	private static int k = 0;
	
	// Constants
	private final static String fccId = "TEST";
	private final static String mode = "MODE_1";
	
	private static JSONObject createFromStrings(String ... str) throws JSONException
	{
		JSONObject object = new JSONObject();
		
		for (int i=0; i<str.length; i+=2)
			object.put(str[i], str[i+1]);
		
		return object;
	}
	
	private static JSONObject createPoint(double latitude, double longitude) throws JSONException
	{
		JSONObject object = new JSONObject();

		JSONObject center = new JSONObject();
		center.put("latitude", latitude);
		center.put("longitude", longitude);
		
		JSONObject point = new JSONObject();
		point.put("center", center);
		
		object.put("point", point);
		
		return object;
	}
	
	private static JSONObject createAntenna() throws JSONException
	{
		JSONObject object = new JSONObject();
		
		object.put("height", 30.0);
		object.put("heightType", "AGL");
		
		return object;
	}
	
	private static JSONObject createOwner() throws JSONException
	{
		JSONObject object = new JSONObject();
		object.put("owner", new JSONObject());
		
		return object;
	}
		
//	private static JSONObject createQuery(double latitude, double longitude) throws JSONException
	private static JSONArray createQuery(double latitude, double longitude) throws JSONException
	{
		//Amjad 
		// create a JSON array to send more than one json objects
		querySizeController++;
		
		if(querySizeController % 10 == 0){
			k++;
		}
		methodCallCounter+=querySize[k];
		if (methodCallCounter  < 1000){
			//do nothing
		}
		else if(methodCallCounter < 2000){
			apiKey1 = apiKey2;
		}else if(methodCallCounter < 3000){
			apiKey1 = apiKey3;
		}else if(methodCallCounter < 4000){
			apiKey1 = apiKey4;
		}else if(methodCallCounter < 5000){
			apiKey1 = apiKey5;
		}else if(methodCallCounter < 6000){
			apiKey1 = apiKey6;
		}else if(methodCallCounter < 7000){
			apiKey1 = apiKey7;
		}else if(methodCallCounter < 8000){
			apiKey1 = apiKey8;
		}else if(methodCallCounter < 9000){
			apiKey1 = apiKey9;
		}else if(methodCallCounter < 10000){
			apiKey1 = apiKey10;
		}else if(methodCallCounter < 11000){
			apiKey1 = apiKey11;
		}else if(methodCallCounter < 12000){
			apiKey1 = apiKey12;
		}else if(methodCallCounter < 13000){
			apiKey1 = apiKey13;
		}else if(methodCallCounter < 14000){
			apiKey1 = apiKey14;
		}else{
			apiKey1 = apiKey15;
		}
		
		JSONArray arr = new JSONArray();
	
		for(int i = 0 ; i < querySize[k] ; i++){
			
			// 0.02 * i = a step forward each iteration 
			double lat =latitude+0.0009*i;
			double lng = longitude+0.0009*i;
			
			JSONObject object = new JSONObject();
			object.put("jsonrpc", "2.0");
			object.put("method", "spectrum.paws.getSpectrum");
			object.put("apiVersion", "v1explorer");
			object.put("id", "any_string");
			JSONObject params = new JSONObject();
			params.put("type", "AVAIL_SPECTRUM_REQ");
			params.put("version", "1.0");
			params.put("deviceDesc", createFromStrings("serialNumber", "your_serial_number", "fccId", fccId, "fccTvbdDeviceType", mode));
			params.put("location", createPoint(lat, lng));
			params.put("antenna", createAntenna());
			params.put("owner", createOwner());
			params.put("key", apiKey1);
			object.put("params", params);
			
			arr.put(object);
			object = null;
		}
	    return arr	;
	}

	public static void query(double latitude, double longitude)
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost("https://www.googleapis.com/rpc");
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10*1000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 10*1000);
		
		request.addHeader("Content-Type", "application/json");
		
		try
		{
			Logger.log(String.format("google-query-start"));
			request.setEntity(new StringEntity(createQuery(latitude, longitude).toString(), HTTP.UTF_8));
			
			Logger.log(String.format("google-query-execute %.4f %.4f", latitude, longitude));
			
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			boolean first = true;
			while ((line = rd.readLine()) != null)
			{
				if (first)
				{
					Logger.log("google-query-first-data");
					first = false;
				}
				Logger.log(line);
			}
			Logger.log("google-query-done");
			
		} catch (UnsupportedEncodingException e)
		{
			Logger.log("google-query-error Unsupported Encoding: " + e.getMessage());
		} catch (JSONException e)
		{
			Logger.log("google-query-error JSON exception: " + e.getMessage());
		} catch (IOException e)
		{
			Logger.log("google-query-error i/o exception: " + e.getMessage());
		}
	}
	
}
