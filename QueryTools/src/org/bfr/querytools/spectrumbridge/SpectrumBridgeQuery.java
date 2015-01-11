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
package org.bfr.querytools.spectrumbridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.bfr.querytools.logging.Logger;

/**
 * 
 */
@SuppressWarnings("deprecation")
public class SpectrumBridgeQuery
{
	
	// Spectrum Bridge query constants
	private final static String baseUrl = "https://tvws-demo.spectrumbridge.com/v3";
	private final static String countryCode = "US";
	private final static String fccId = "";
	private final static int serial = 101;
	private final static int deviceType = 3;

	private final static int antennaHeight = 10;
	
	// XML Template
	private static String xmlTemplate = 
	"<RegistrationRequest xmlns=\"http://schemas.datacontract.org/2004/07/SpectrumBridge.WhiteSpaces.Services.v3\">\r\n" +
	"	<AntennaHeight>%d</AntennaHeight>\r\n" +
	"	<ContactCity>City</ContactCity>\r\n" +
	"	<ContactCountry>US</ContactCountry>\r\n" +
	"	<ContactEmail>bfr@bfr.nl</ContactEmail>\r\n" +
	"	<ContactName>Bfr</ContactName>\r\n" +
	"	<ContactPhone>Phone</ContactPhone>\r\n" +
	"	<ContactState>State</ContactState>\r\n" +
	"	<ContactStreet>Street</ContactStreet>\r\n" +
	"	<ContactZip>Zip</ContactZip>\r\n" +
	"	<DeviceOwner>DeviceOwner</DeviceOwner>\r\n" +
	"	<DeviceType>%d</DeviceType>\r\n" +
	"	<Latitude>%.6f</Latitude>\r\n" +
	"	<Longitude>%.6f</Longitude>\r\n" +
	"</RegistrationRequest>";
	
	private static HttpClient createClient()
	{
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);		

		HttpClient client = new DefaultHttpClient(params);
		
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10*1000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 10*1000);
		
	    return client;
	}

	private static HttpResponse channelQuery(double latitude, double longitude) throws ClientProtocolException, IOException
	{
		HttpClient client = createClient();
		
		String queryUrl = String.format(Locale.US, "%s/channels/%s/%.6f/%.6f/?fccid=%s&serial=%d&type=%d",
				baseUrl, countryCode, latitude, longitude, fccId, serial, deviceType );
		
		HttpGet request = new HttpGet(queryUrl);
		
		request.addHeader("SBI-Sequence", "100");
		request.addHeader("User-Agent", "HTMLGET 1.0");
		
		return client.execute(request);		
	}
	
	private static SbiReturnCode sbiReturnCode(HttpResponse response)
	{
		Header statusCode = response.getFirstHeader("SBI-Status");
		
		if (statusCode == null)
			return SbiReturnCode.MissingSbiReturnCode;
		
		try
		{
			int code = Integer.parseInt(statusCode.getValue());
			
			if (code>=0 && code<SbiReturnCode.values().length)
				return SbiReturnCode.values()[Integer.parseInt(statusCode.getValue())];
			else
				return SbiReturnCode.GenericError;
			
			
		} catch (NumberFormatException ex)
		{
			return SbiReturnCode.MalformedSbiReturnCode;
		}
		
		
	}
	
	private static void readResponseBody(HttpResponse response) throws IllegalStateException, IOException
	{
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		boolean first = true;
		while ((line = rd.readLine()) != null)
		{
			if (first)
			{
				Logger.log("spectrumbridge-query-first-data");
				first = false;
			}
			Logger.log(line);
		}
	}
	
	public static HttpResponse register(int antennaHeight, double latitude, double longitude) throws ClientProtocolException, IOException
	{
		String queryUrl = String.format(Locale.US, "%s/devices/%s/%s/%d",baseUrl, countryCode, fccId, serial );

		// Query XML
		String queryXml = String.format(Locale.US, xmlTemplate, antennaHeight, deviceType, latitude, longitude );

		HttpClient client = createClient();
		
		HttpPut request = new HttpPut(queryUrl);

		request.addHeader("Content-Type", "application/xml");
		
	
		request.setEntity(new StringEntity(queryXml, "UTF-8"));
		
		request.addHeader("SBI-Sequence", "100");
		
//		for (Header header : request.getAllHeaders())
//			System.out.println(">> " + header.getName() + ": " + header.getValue() );
		
		return client.execute(request);
		
	}
	
	public static void query(double latitude, double longitude, boolean register)
	{
		Logger.log(String.format(Locale.US, "spectrumbridge-query-execute %.4f %.4f", latitude, longitude));
		
		HttpResponse response = null;
		SbiReturnCode returnCode;
		try
		{

			// Register
			if (register)
			{
				Logger.log("spectrumbridge-query registering");
				
				response = register(antennaHeight, latitude, longitude);			
	
				returnCode = sbiReturnCode(response);
				
				Logger.log("spectrumbridge-query registration: " + returnCode);
				
				if (returnCode!=SbiReturnCode.Succes || returnCode==SbiReturnCode.GenericError)
				{
					Logger.log("spectrumbridge-query-error Registration Query Returned: " + returnCode);
				} else
				{
					readResponseBody(response);
				}
			} else
				Logger.log("spectrumbridge-query skipping registration");

			// Query channel list
			Logger.log("spectrumbridge-query querying channel list");
			response = channelQuery(latitude, longitude);

			returnCode = sbiReturnCode(response);

			Logger.log("spectrumbridge-query channel list: " + returnCode);
			
			if (returnCode!=SbiReturnCode.Succes)
			{
				Logger.log("spectrumbridge-query-error Channel Query Returned: " + returnCode);
			} else
			{
				readResponseBody(response);
			}
			
		} catch (UnsupportedEncodingException e)
		{
			Logger.log("spectrumbridge-query-error Unsupported Encoding: " + e.getMessage());
		} catch (IOException e)
		{
			e.printStackTrace();
			Logger.log("spectrumbridge-query-error i/o exception: " + e.getMessage());
		}
		

		Logger.log("spectrumbridge-query-done");
		
	}
	
}
