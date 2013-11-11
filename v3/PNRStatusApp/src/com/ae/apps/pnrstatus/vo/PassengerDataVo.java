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
 * Represents a Passenger data
 * 
 * @author midhun_harikumar
 * 
 */
public class PassengerDataVo implements Serializable {
	private static final long	serialVersionUID	= 2264807366835227895L;
	private String				trainPassenger;
	private String				trainBookingBerth;
	private String				trainCurrentStatus;
	private String				berthPosition;

	/**
	 * @return the trainBookingBerth
	 */
	public String getTrainBookingBerth() {
		return trainBookingBerth;
	}

	/**
	 * @param trainBookingBerth
	 *            the trainBookingBerth to set
	 */
	public void setTrainBookingBerth(String trainBookingBerth) {
		this.trainBookingBerth = trainBookingBerth;
	}

	/**
	 * @return the trainPassenger
	 */
	public String getTrainPassenger() {
		return trainPassenger;
	}

	/**
	 * @param trainPassenger
	 *            the trainPassenger to set
	 */
	public void setTrainPassenger(String trainPassenger) {
		this.trainPassenger = trainPassenger;
	}

	/**
	 * @return the trainCurrentStatus
	 */
	public String getTrainCurrentStatus() {
		return trainCurrentStatus;
	}

	/**
	 * @param trainCurrentStatus
	 *            the trainCurrentStatus to set
	 */
	public void setTrainCurrentStatus(String trainCurrentStatus) {
		this.trainCurrentStatus = trainCurrentStatus;
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
		result = prime * result + ((trainBookingBerth == null) ? 0 : trainBookingBerth.hashCode());
		result = prime * result + ((trainCurrentStatus == null) ? 0 : trainCurrentStatus.hashCode());
		result = prime * result + ((trainPassenger == null) ? 0 : trainPassenger.hashCode());
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
		if (trainBookingBerth == null) {
			if (other.trainBookingBerth != null)
				return false;
		} else if (!trainBookingBerth.equals(other.trainBookingBerth))
			return false;
		if (trainCurrentStatus == null) {
			if (other.trainCurrentStatus != null)
				return false;
		} else if (!trainCurrentStatus.equals(other.trainCurrentStatus))
			return false;
		if (trainPassenger == null) {
			if (other.trainPassenger != null)
				return false;
		} else if (!trainPassenger.equals(other.trainPassenger))
			return false;
		return true;
	}

}