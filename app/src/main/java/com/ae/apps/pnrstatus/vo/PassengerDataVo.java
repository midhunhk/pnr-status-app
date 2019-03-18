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

package com.ae.apps.pnrstatus.vo;

import java.io.Serializable;

/**
 * Represents a Passenger's data
 * 
 * @author midhun_harikumar
 * 
 */
public class PassengerDataVo implements Serializable {
	private static final long	serialVersionUID	= 2264807366835227895L;
	private String				passengerName;
	private String				bookingBerth;
	private String				currentStatus;
	private String				berthPosition;

	/**
	 * @return the trainBookingBerth
	 */
	public String getBookingBerth() {
		return bookingBerth;
	}

	/**
	 * @param trainBookingBerth
	 *            the trainBookingBerth to set
	 */
	public void setBookingBerth(String trainBookingBerth) {
		this.bookingBerth = trainBookingBerth;
	}

	/**
	 * @return the trainPassenger
	 */
	public String getPassenger() {
		return passengerName;
	}

	/**
	 * @param trainPassenger
	 *            the trainPassenger to set
	 */
	public void setPassenger(String trainPassenger) {
		this.passengerName = trainPassenger;
	}

	/**
	 * @return the trainCurrentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param trainCurrentStatus
	 *            the trainCurrentStatus to set
	 */
	public void setCurrentStatus(String trainCurrentStatus) {
		this.currentStatus = trainCurrentStatus;
	}

	public void setBerthPosition(String berthPosition) {
		this.berthPosition = berthPosition;
	}

	public String getBerthPosition() {
		return berthPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((berthPosition == null) ? 0 : berthPosition.hashCode());
		result = prime * result + ((bookingBerth == null) ? 0 : bookingBerth.hashCode());
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + ((passengerName == null) ? 0 : passengerName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassengerDataVo other = (PassengerDataVo) obj;
		if (berthPosition == null) {
			if (other.berthPosition != null)
				return false;
		} else if (!berthPosition.equals(other.berthPosition))
			return false;
		if (bookingBerth == null) {
			if (other.bookingBerth != null)
				return false;
		} else if (!bookingBerth.equals(other.bookingBerth))
			return false;
		if (currentStatus == null) {
			if (other.currentStatus != null)
				return false;
		} else if (!currentStatus.equals(other.currentStatus))
			return false;
		if (passengerName == null) {
			if (other.passengerName != null)
				return false;
		} else if (!passengerName.equals(other.passengerName))
			return false;
		return true;
	}

}