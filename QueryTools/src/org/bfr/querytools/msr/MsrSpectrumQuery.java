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
package org.bfr.querytools.msr;

import org.bfr.querytools.logging.Logger;
import org.bfr.querytools.msr.driver.AuthHeader;
import org.bfr.querytools.msr.driver.Driver;
import org.bfr.querytools.msr.driver.IWsdl2CodeEvents;
import org.bfr.querytools.msr.driver.VectorBoolean;

public class MsrSpectrumQuery
{
	
	private final static String user = "";
	private final static String password = "";
	
	public static void query(double latitude, double longitude)
	{
		Driver driver = new Driver();
        AuthHeader credentials = new AuthHeader();
        credentials.username = user;
        credentials.passwd = password;
        driver.authHeaderValue = credentials;
        
        driver.eventHandler = eventHandler;

		Logger.log(String.format("msr-query-execute %.4f %.4f", latitude, longitude));
		
        VectorBoolean availableChannelList = driver.GetSpectrumMap(latitude, longitude, "Rice", -114, true, true, false, true, true);
        if (availableChannelList!=null)
        {
            String channels = "msr-query-channels";
            
            for (int i=0; i<availableChannelList.size(); i++)
            	if (availableChannelList.get(i))
            		channels += " " + i;

            Logger.log(channels);
        }

        Logger.log("msr-query-done");

		
	}
	
	private static IWsdl2CodeEvents eventHandler = new IWsdl2CodeEvents()
	{

		@Override
		public void Wsdl2CodeStartedRequest()
		{
			Logger.log("msr-query-request-start");
		}

		@Override
		public void Wsdl2CodeFinished(String methodName, Object Data)
		{
			Logger.log("msr-query-request-finished");
		}

		@Override
		public void Wsdl2CodeFinishedWithException(Exception ex)
		{
			Logger.log("msr-query-error " + ex.getMessage());
			ex.printStackTrace();
		}

		@Override
		public void Wsdl2CodeEndedRequest()
		{
			Logger.log("msr-query-done");
		}
		
	};

}
