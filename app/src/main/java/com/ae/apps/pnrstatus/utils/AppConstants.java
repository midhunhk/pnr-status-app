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

public interface AppConstants {
    /**
     * Constant used for log tag for the application
     */
    String TAG = "PNRStatusApp";

    // flag to indicate running in development mode
    boolean IS_DEV_MODE = Boolean.valueOf("false");
    String PREF_KEY_DEV_STUB = "pref_key_dev_stub";

    String APP_ACTION = "action";
    String PNR_NUMBER = "pnr_number";
    String ACTION_DELETE = "actionDelete";
    String ACTION_CHECK = "actionCheck";
    String PNR_STATUS_VO = "pnrStatusVo";
    String GOOGLE_SDK = "google_sdk";
    String APP_HASH_TAG = "#PNRStatusApp";
    String TICKET_STATUS_CONFIRM = "CNF";

    /**
     * Ticket Classes
     */
    String CLASS_SLEEPER = "SL";
    String CLASS_UNKNOWN = "unknown";
    String CLASS_AC2 = "AC2";
    String CLASS_AC3 = "AC3";

    /**
     * Ticket Status
     */
    String STATUS_CNF = "CNF";
    String STATUS_RAC = "RAC";
    String STATUS_WL = "W/L";

    /**
     * Number of seats in compartments
     */
    int SEATS_IN_SLEEPER_COMPARTMENT = 8;
    int SEATS_IN_AC2_COMPARTMENT = 6;

    /**
     * Berth Positions
     */
    String BERTH_LOWER = "Lower";
    String BERTH_MIDDLE = "Middle";
    String BERTH_UPPER = "Upper";
    String BERTH_SIDE_LOWER = "Side Lower";
    String BERTH_SIDE_UPPER = "Side Upper";
    String BERTH_DEFAULT = "--";

    /**
     * Request Methods
     */
    String METHOD_GET = "GET";
    String METHOD_POST = "POST";

}
