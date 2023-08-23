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
        val another = other as PNRStatusVo
        if (boardingPoint == null) {
            if (another.boardingPoint != null) return false
        } else if (boardingPoint != another.boardingPoint) return false
        if (chartStatus == null) {
            if (another.chartStatus != null) return false
        } else if (chartStatus != another.chartStatus) return false
        if (currentStatus == null) {
            if (another.currentStatus != null) return false
        } else if (currentStatus != another.currentStatus) return false
        if (dateOfJourneyText == null) {
            if (another.dateOfJourneyText != null) return false
        } else if (dateOfJourneyText != another.dateOfJourneyText) return false
        if (firstPassengerData == null) {
            if (another.firstPassengerData != null) return false
        } else if (firstPassengerData != another.firstPassengerData) return false
        if (trainJourneyDate == null) {
            if (another.trainJourneyDate != null) return false
        } else if (trainJourneyDate != another.trainJourneyDate) return false
        if (journeyDateTimeStamp != another.journeyDateTimeStamp) return false
        if (passengers == null) {
            if (another.passengers != null) return false
        } else if (passengers != another.passengers) return false
        if (pnrNumber == null) {
            if (another.pnrNumber != null) return false
        } else if (pnrNumber != another.pnrNumber) return false
        if (rowId != another.rowId) return false
        if (ticketClass == null) {
            if (another.ticketClass != null) return false
        } else if (ticketClass != another.ticketClass) return false
        if (ticketStatus == null) {
            if (another.ticketStatus != null) return false
        } else if (ticketStatus != another.ticketStatus) return false
        if (destination == null) {
            if (another.destination != null) return false
        } else if (destination != another.destination) return false
        if (embarkPoint == null) {
            if (another.embarkPoint != null) return false
        } else if (embarkPoint != another.embarkPoint) return false
        if (trainName == null) {
            if (another.trainName != null) return false
        } else if (trainName != another.trainName) return false
        if (trainNo == null) {
            if (another.trainNo != null) return false
        } else if (trainNo != another.trainNo) return false
        return true
    }

    companion object {
        private const val serialVersionUID = 3623209903716348142L
    }
}