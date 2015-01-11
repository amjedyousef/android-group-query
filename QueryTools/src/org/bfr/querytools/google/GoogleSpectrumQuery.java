/**
	Copyright 2014 [BFR]
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
**/
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

/**
 * 
 * Google <a href="https://www.google.com/get/spectrumdatabase/">spectrum database</a> client.  
 * <p>
 * This is an implementation of the <a href="https://developers.google.com/spectrum/paws/gettingstarted">Spectrum Database API</a> 
 * which uses JSON as a transport protocol. Note that the API key is hard coded, which makes this non-production code. Note that each
 * key has a daily query limit, and the service will start returning error messages after this limit has been reached.
 * 
 */
public class GoogleSpectrumQuery
{

	// API key. 
	private static String apiKey1 = "AIzaSyDivuCJrME_Z6KNI_OUVfatuA2vn9A0UHo";
	private static String apiKey2 = "AIzaSyAl9rewC1BA-FQyu3iN5xb06_7d9eiiArU";
	private static String apiKey3 = "AIzaSyCtTWgg9Bsb3oUblpRFEYuV34nXUoKSF1M";

	private static int methodCallCounter = 0;
	private static int querySize = 32;
	
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
	
/*	private static JSONObject createCapabilities(long ... hz) throws JSONException
	{
		JSONObject object = new JSONObject();
		
		JSONObject[] ranges = new JSONObject[hz.length/2];
		for (int i=0; i<hz.length/2; i++)
		{
			ranges[i] = new JSONObject();
			ranges[i].put("startHz", hz[i*2]);
			ranges[i].put("stopHz", hz[i*2+1]);
		}
		
		object.put("frequencyRanges", ranges);
		
		return object;
	}
*/	
//	private static JSONObject createQuery(double latitude, double longitude) throws JSONException
	private static JSONArray createQuery(double latitude, double longitude) throws JSONException
	{
		//Amjad 
		// create a JSON array to send more than one json objects
		methodCallCounter++;
		if (methodCallCounter * querySize < 1000){
			
		}else if(methodCallCounter * querySize < 2000){
			apiKey1 = apiKey2;
		}else{
			apiKey1 = apiKey3;
		}
		
		JSONArray arr = new JSONArray();
		
		//Object One
		
		
		for(int i = 0 ; i < querySize ; i++){
			
			JSONObject object = new JSONObject();
			object.put("jsonrpc", "2.0");
			object.put("method", "spectrum.paws.getSpectrum");
			object.put("apiVersion", "v1explorer");
			object.put("id", "any_string");
			JSONObject params = new JSONObject();
			params.put("type", "AVAIL_SPECTRUM_REQ");
			params.put("version", "1.0");
			params.put("deviceDesc", createFromStrings("serialNumber", "your_serial_number", "fccId", fccId, "fccTvbdDeviceType", mode));
			params.put("location", createPoint(latitude+0.02, longitude+0.02));
			params.put("antenna", createAntenna());
			params.put("owner", createOwner());
	//		params.put("capabilities", createCapabilities(800000000, 850000000, 900000000, 950000000));		
			params.put("key", apiKey1);
			object.put("params", params);
			
			arr.put(object);
		}
		
	//  Amjad
	//	return object;
	    return arr	;
	}
	
//	public static void googleQuery() throws ClientProtocolException, IOException
//	{
//		Logger.log("query-start");
//		
//		HttpClient client = new DefaultHttpClient();
//		HttpGet request = new HttpGet("https://www.google.com");
//		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10*1000);
//		HttpConnectionParams.setSoTimeout(client.getParams(), 10*1000);
//		
//		Logger.log("query-execute");
//		HttpResponse response = client.execute(request);
//
//		// Get the response
//		InputStream input = response.getEntity().getContent();
//		byte[] block = new byte[1024 * 8];
//		
//		boolean first = true;
//		while (input.read(block)!=-1)
//		{
//			if (first)
//			{
//				Logger.log("query-first-data");
//				first = false;
//			}
//		}
//		
//		Logger.log("query-done");
//	}
	
	public static void query(double latitude, double longitude)
	{
		
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost("https://www.googleapis.com/rpc");
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10*1000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 10*1000);
		
		request.addHeader("Content-Type", "application/json");
		
		try
		{
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

		
		Logger.log("google-query-done");
		
	}
	
}
