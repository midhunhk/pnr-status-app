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

package com.ae.apps.pnrstatus.exceptions;

/**
 * This exception is thrown when an unknown Service type is requested to the ServiceFactory.
 * 
 * @author Midhun_Harikumar
 * 
 */
public class InvalidServiceException extends Exception {

	private static final long	serialVersionUID	= 7374739211962780312L;

	public InvalidServiceException() {
		super("The requested Service name is invalid.");
	}

	public InvalidServiceException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidServiceException(Throwable throwable) {
		super(throwable);
	}

	public InvalidServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
