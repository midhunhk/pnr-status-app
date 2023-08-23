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
package com.ae.apps.pnrstatus.vo

import java.io.Serializable

/**
 * Represents a Passenger's data
 *
 */
class PassengerDataVo : Serializable {
    var passenger: String? = null
	var bookingBerth: String? = null
	var currentStatus: String? = null
	var berthPosition: String? = null

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (berthPosition == null) 0 else berthPosition.hashCode()
        result = prime * result + if (bookingBerth == null) 0 else bookingBerth.hashCode()
        result = prime * result + if (currentStatus == null) 0 else currentStatus.hashCode()
        result = prime * result + if (passenger == null) 0 else passenger.hashCode()
        return result
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val another = other as PassengerDataVo
        if (berthPosition == null) {
            if (another.berthPosition != null) return false
        } else if (berthPosition != another.berthPosition) return false
        if (bookingBerth == null) {
            if (another.bookingBerth != null) return false
        } else if (bookingBerth != another.bookingBerth) return false
        if (currentStatus == null) {
            if (another.currentStatus != null) return false
        } else if (currentStatus != another.currentStatus) return false
        if (passenger == null) {
            if (another.passenger != null) return false
        } else if (passenger != another.passenger) return false
        return true
    }

    companion object {
        private const val serialVersionUID = 2264807366835227895L
    }
}