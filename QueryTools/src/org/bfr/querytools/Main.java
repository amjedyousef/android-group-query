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

import java.io.IOException;

import org.bfr.querytools.spectrumbridge.SpectrumBridgeQuery;


public class Main
{
	
	public static void main(String args[]) throws IOException
	{
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime","true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "ERROR");
		
//		GoogleSpectrumQuery.query(40.69940, -74.04056);
//		MsrSpectrumQuery.query(40.69940, -74.04056);
		SpectrumBridgeQuery.query(40.69940, -74.04056, true);
		
	}

}
