/*
 * Copyright 2012 Midhun Harikumar
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
 * Denotes an exception in the Status
 * 
 * @author midhun_harikumar
 * 
 */
public class StatusException extends Exception {

	private static final long	serialVersionUID	= 4678860372933762653L;

	private String				statusString;

	public StatusException() {
		super();
	}

	public StatusException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public StatusException(String detailMessage) {
		super(detailMessage);
	}

	public StatusException(Throwable throwable) {
		super(throwable);
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getStatusString() {
		return statusString;
	}

}
