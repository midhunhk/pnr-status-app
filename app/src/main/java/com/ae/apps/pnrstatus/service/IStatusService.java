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

package com.ae.apps.pnrstatus.service;

import java.io.IOException;

import org.json.JSONException;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

public interface IStatusService {

	/**
	 * Returns the name of the service.
	 * 
	 * @return
	 */
	String getServiceName();

	/**
	 * Returns a PNRStatusVo object after parsing the JSON response
	 * 
	 * @param pnrNumber
	 * @return
	 */
	PNRStatusVo getResponse(String pnrNumber) throws StatusException;

	/**
	 * Returns a PNRStatusVo object based on the value for stubResponse
	 * 
	 * @param pnrNumber
	 * @param stubResponse
	 * @return
	 * @throws JSONException
	 * @throws StatusException
	 * @throws IOException
	 */
	PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException;
}
