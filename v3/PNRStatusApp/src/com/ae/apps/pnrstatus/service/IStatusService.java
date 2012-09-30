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
	 * @param responseString
	 * @return
	 */
	PNRStatusVo getResponse(String pnrNumber) throws JSONException, StatusException, IOException;

	/**
	 * Returns a PNRSTatusVo object based on the value for stubResponse
	 * 
	 * @param pnrNumber
	 * @param stubResponse
	 * @return
	 * @throws JSONException
	 * @throws StatusException
	 * @throws IOException
	 */
	PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws JSONException, StatusException, IOException;
}
