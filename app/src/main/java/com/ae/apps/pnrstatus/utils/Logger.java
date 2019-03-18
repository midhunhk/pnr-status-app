/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ae.apps.pnrstatus.utils;

import android.util.Log;

/**
 * Logger is a wrapper over the android default logger. It will log messages only if IS_DEV_MODE is set as true
 * 
 * @author Midhun
 * 
 */
public class Logger {

	/**
	 * logs a debug entry if we are in developer mode
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int d(String tag, String msg) {
		if (AppConstants.IS_DEV_MODE) {
			return Log.d(tag, msg);
		}
		return 0;
	}

	/**
	 * logs an error entry if we are in developer mode
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int e(String tag, String msg) {
		if (AppConstants.IS_DEV_MODE) {
			return Log.e(tag, msg);
		}
		return 0;
	}

	/**
	 * logs an info entry if we are in developer mode
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int i(String tag, String msg) {
		if (AppConstants.IS_DEV_MODE) {
			return Log.i(tag, msg);
		}
		return 0;
	}

	public static int e(String tag, String msg, Throwable e) {
		if (AppConstants.IS_DEV_MODE) {
			return Log.e(tag, msg, e);
		}
		return 0;
	}

	public static int w(String tag, String msg) {
		if (AppConstants.IS_DEV_MODE) {
			return Log.w(tag, msg);
		}
		return 0;
	}

}
