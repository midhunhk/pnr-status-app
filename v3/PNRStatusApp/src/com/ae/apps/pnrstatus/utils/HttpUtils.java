/*
 * Copyright 2014 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ae.apps.pnrstatus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * utility class for perform Http related functions
 */
public class HttpUtils {

	// http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
	// http://www.androidsnippets.com/get-the-content-from-a-httpresponse-or-any-inputstream-as-a-string

	/**
	 * send a post request
	 * 
	 * @param url
	 * @param requestHeaders
	 * @param formParameters
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> requestHeaders, Map<String, String> formParameters) {
		String requestResult = null;
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();

		// Map all the headers for the request
		for (String key : requestHeaders.keySet()) {
			httpPost.setHeader(key, requestHeaders.get(key));
		}

		// Add all the params for the request
		for (String key : formParameters.keySet()) {
			postParams.add(new BasicNameValuePair(key, formParameters.get(key)));
		}

		try {
			// Add any post data
			httpPost.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
			requestResult = executeHttpRequest(httpPost);
		} catch (Exception e) {
			Logger.e(AppConstants.TAG, e.getMessage());
		}

		return requestResult;
	}

	/**
	 * send a get request
	 * 
	 * @param url
	 * @return
	 */
	public static String sendGet(String url) {
		HttpGet httpGet = new HttpGet(url);

		// execute and return the result
		return executeHttpRequest(httpGet);
	}

	/**
	 * Creates the WebRequestResult object from the response
	 * 
	 * @param response
	 * @return
	 */
	private static String executeHttpRequest(HttpUriRequest request) {
		String requestResult = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			String reasonPhrase = response.getStatusLine().getReasonPhrase();
			Logger.d(AppConstants.TAG, "ExecuteHttpRequest : " + responseCode + " " + reasonPhrase);

			// Wrap a BufferedReader around the InputStream
			StringBuilder result = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = null;
			// Read response until the end
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();

			// create a request result object
			requestResult = result.toString();
		} catch (IllegalStateException e) {
			if (AppConstants.IS_DEV_MODE) {
				e.printStackTrace();
				Logger.e(AppConstants.TAG, "ExecuteHttpRequest : " + e.getMessage());
			}
		} catch (IOException e) {
			if (AppConstants.IS_DEV_MODE) {
				e.printStackTrace();
				Logger.e(AppConstants.TAG, "ExecuteHttpRequest : " + e.getMessage());
			}
		}
		return requestResult;
	}

}
