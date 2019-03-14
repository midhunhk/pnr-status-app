/*
 * Copyright 2013 Midhun Harikumar
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

package com.ae.apps.pnrstatus.exceptions;

/**
 * Denotes an exception that occured while checking for status
 * 
 * @author midhun_harikumar
 * 
 */
public class StatusException extends Exception {

	private static final long	serialVersionUID	= 4678860372933762653L;

	public enum ErrorCodes {
		PARSE_ERROR, NETWORK_ERROR, EMPTY_RESPONSE, URL_ERROR
	}

	private ErrorCodes	errorCode;

	public StatusException() {
		super();
	}

	public StatusException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public StatusException(String detailMessage) {
		super(detailMessage);
	}

	public StatusException(String message, ErrorCodes code) {
		super(message);
		errorCode = code;
	}

	public StatusException(Throwable throwable, ErrorCodes code) {
		super(throwable);
		errorCode = code;
	}

	public StatusException(String message, Throwable throwable, ErrorCodes code) {
		super(message, throwable);
		errorCode = code;
	}

	public ErrorCodes getErrorCode() {
		return errorCode;
	}

}
