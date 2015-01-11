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
package org.bfr.querytools;

import org.bfr.querytools.google.GoogleSpectrumQuery;
import org.bfr.querytools.logging.DefaultLogger;
import org.bfr.querytools.logging.Logger;
import org.bfr.querytools.msr.MsrSpectrumQuery;

public class SpectrumQuery
{
	
	public static String[] googleQuery(double latitude, double longitude)
	{
		StringListLogger logger = new StringListLogger();
		Logger.setLogger(logger);
		
		GoogleSpectrumQuery.query(40.69940, -74.04056);
		
		Logger.setLogger(new DefaultLogger());
		
		return logger.getMessages();
	}

	public static String[] microsoftQuery(double latitude, double longitude)
	{
		StringListLogger logger = new StringListLogger();
		Logger.setLogger(logger);
		
		MsrSpectrumQuery.query(40.69940, -74.04056);
		
		Logger.setLogger(new DefaultLogger());
		
		return logger.getMessages();
	}
}
