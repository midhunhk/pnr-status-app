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
package com.ae.apps.pnrstatus.exceptions

/**
 * Denotes an exception that occured while checking for status
 *
 * @author midhun_harikumar
 */
class StatusException : Exception {
    enum class ErrorCodes {
        PARSE_ERROR, NETWORK_ERROR, EMPTY_RESPONSE, URL_ERROR
    }

    var errorCode: ErrorCodes? = null
        private set

    constructor() : super()

    constructor(detailMessage: String?, throwable: Throwable?) : super(detailMessage, throwable)

    constructor(detailMessage: String?) : super(detailMessage)

    constructor(message: String?, code: ErrorCodes?) : super(message) {
        errorCode = code
    }

    constructor(throwable: Throwable?, code: ErrorCodes?) : super(throwable) {
        errorCode = code
    }

    constructor(message: String?, throwable: Throwable?, code: ErrorCodes?) : super(
        message,
        throwable
    ) {
        errorCode = code
    }

    companion object {
        private const val serialVersionUID = 4678860372933762653L
    }
}
