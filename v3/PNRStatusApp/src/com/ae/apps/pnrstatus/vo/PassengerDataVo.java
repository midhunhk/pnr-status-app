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