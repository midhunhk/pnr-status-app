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
package com.ae.apps.pnrstatus.service

import com.ae.apps.pnrstatus.exceptions.StatusException
import com.ae.apps.pnrstatus.vo.PNRStatusVo

interface IStatusService {
    /**
     * Returns the name of the service.
     *
     * @return
     */
	val serviceName: String?

    /**
     * Returns a PNRStatusVo object after parsing the JSON response
     *
     * @param pnrNumber
     * @return
     */
    @Throws(StatusException::class)
    fun getResponse(pnrNumber: String?): PNRStatusVo?

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
    @Throws(StatusException::class)
    fun getResponse(pnrNumber: String?, stubResponse: Boolean?): PNRStatusVo?
}