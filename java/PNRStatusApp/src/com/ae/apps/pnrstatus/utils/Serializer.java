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

package com.ae.apps.pnrstatus.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Serializer {

	/**
	 * Serializes an Object and returns a bytearray
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();

			// Get the bytes of the serialized object
			byte[] buf = bos.toByteArray();

			return buf;
		} catch (IOException ioe) {
			Logger.e("serializeObject", "error", ioe);

			return null;
		}
	}

	/**
	 * Deserializes an object from a byte array
	 * 
	 * @param b
	 * @return
	 */
	public static Object deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
			Object object = in.readObject();
			in.close();
			return object;
		} catch (ClassNotFoundException cnfe) {
			Logger.e("deserializeObject", "class not found error", cnfe);
		} catch (IOException ioe) {
			Logger.e("deserializeObject", "io error", ioe);
		}
		return null;
	}
}
