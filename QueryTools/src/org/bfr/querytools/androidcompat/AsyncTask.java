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
package org.bfr.querytools.androidcompat;

/**
 * The Microsoft research driver was generated for Android, and depends on the AsyncTask class to perform asynchronous 
 * queries. Since this class is Android-only, this class implements a partial, synchronous version.  
 */
public abstract class AsyncTask<Params, Progress, Result>
{

	@SuppressWarnings("unchecked")
	protected abstract Result doInBackground(Params... params);

	protected void onPreExecute()
	{
	}

	protected void onPostExecute(Result result)
	{
	}
	
	public void execute()
	{
		onPreExecute();
		
		Result result = doInBackground((Params[])null);
		
		onPostExecute(result);
	}

}