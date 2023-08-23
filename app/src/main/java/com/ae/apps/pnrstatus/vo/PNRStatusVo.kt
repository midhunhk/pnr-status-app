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
 * Represents the PNRStatus Vo
 *
 * @author midhun_harikumar
 */
class PNRStatusVo : Serializable, Comparable<PNRStatusVo> {
	@JvmField
	var rowId: Long = 0
	@JvmField
	var pnrNumber: String? = null
	@JvmField
	var trainNo: String? = null
	@JvmField
	var trainName: String? = null
    var destination: String? = null
	@JvmField
	var boardingPoint: String? = null
    var embarkPoint: String? = null
	var ticketClass: String? = null
	var ticketStatus: String? = null
    var trainJourneyDate: String? = null
	var chartStatus: String? = null
    @JvmField
	var dateOfJourneyText: String? = null
	var currentStatus: String? = null
	@JvmField
	var passengers: List<PassengerDataVo>? = null
	var firstPassengerData: PassengerDataVo? = null
    @JvmField
	var journeyDateTimeStamp: Long = 0
    override fun compareTo(other: PNRStatusVo): Int {
        return if (journeyDateTimeStamp < other.journeyDateTimeStamp) {
            -1
        } else if (journeyDateTimeStamp > other.journeyDateTimeStamp) {
            1
        } else {
            0
        }
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (boardingPoint == null) 0 else boardingPoint.hashCode()
        result = prime * result + if (chartStatus == null) 0 else chartStatus.hashCode()
        result = prime * result + if (currentStatus == null) 0 else currentStatus.hashCode()
        result = prime * result + if (dateOfJourneyText == null) 0 else dateOfJourneyText.hashCode()
        result =
            prime * result + if (firstPassengerData == null) 0 else firstPassengerData.hashCode()
        result = prime * result + if (trainJourneyDate == null) 0 else trainJourneyDate.hashCode()
        result = prime * result + (journeyDateTimeStamp xor (journeyDateTimeStamp ushr 32)).toInt()
        result = prime * result + if (passengers == null) 0 else passengers.hashCode()
        result = prime * result + if (pnrNumber == null) 0 else pnrNumber.hashCode()
        result = prime * result + (rowId xor (rowId ushr 32)).toInt()
        result = prime * result + if (ticketClass == null) 0 else ticketClass.hashCode()
        result = prime * result + if (ticketStatus == null) 0 else ticketStatus.hashCode()
        result = prime * result + if (destination == null) 0 else destination.hashCode()
        result = prime * result + if (embarkPoint == null) 0 else embarkPoint.hashCode()
        result = prime * result + if (trainName == null) 0 else trainName.hashCode()
        result = prime * result + if (trainNo == null) 0 else trainNo.hashCode()
        return result
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val other = other as PNRStatusVo
        if (boardingPoint == null) {
            if (other.boardingPoint != null) return false
        } else if (boardingPoint != other.boardingPoint) return false
        if (chartStatus == null) {
            if (other.chartStatus != null) return false
        } else if (chartStatus != other.chartStatus) return false
        if (currentStatus == null) {
            if (other.currentStatus != null) return false
        } else if (currentStatus != other.currentStatus) return false
        if (dateOfJourneyText == null) {
            if (other.dateOfJourneyText != null) return false
        } else if (dateOfJourneyText != other.dateOfJourneyText) return false
        if (firstPassengerData == null) {
            if (other.firstPassengerData != null) return false
        } else if (firstPassengerData != other.firstPassengerData) return false
        if (trainJourneyDate == null) {
            if (other.trainJourneyDate != null) return false
        } else if (trainJourneyDate != other.trainJourneyDate) return false
        if (journeyDateTimeStamp != other.journeyDateTimeStamp) return false
        if (passengers == null) {
            if (other.passengers != null) return false
        } else if (passengers != other.passengers) return false
        if (pnrNumber == null) {
            if (other.pnrNumber != null) return false
        } else if (pnrNumber != other.pnrNumber) return false
        if (rowId != other.rowId) return false
        if (ticketClass == null) {
            if (other.ticketClass != null) return false
        } else if (ticketClass != other.ticketClass) return false
        if (ticketStatus == null) {
            if (other.ticketStatus != null) return false
        } else if (ticketStatus != other.ticketStatus) return false
        if (destination == null) {
            if (other.destination != null) return false
        } else if (destination != other.destination) return false
        if (embarkPoint == null) {
            if (other.embarkPoint != null) return false
        } else if (embarkPoint != other.embarkPoint) return false
        if (trainName == null) {
            if (other.trainName != null) return false
        } else if (trainName != other.trainName) return false
        if (trainNo == null) {
            if (other.trainNo != null) return false
        } else if (trainNo != other.trainNo) return false
        return true
    }

    companion object {
        private const val serialVersionUID = 3623209903716348142L
    }
}