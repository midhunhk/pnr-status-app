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
import java.util.List;

/**
 * Represents the PNRStatus Vo
 * 
 * @author midhun_harikumar
 * 
 */
public class PNRStatusVo implements Serializable, Comparable<PNRStatusVo> {

	private static final long	serialVersionUID	= 3623209903716348142L;
	private long					rowId;
	private String					pnrNumber;
	private String					trainNo;
	private String					trainName;
	private String					trainDest;
	private String					boardingPoint;
	private String					trainEmbark;
	private String					ticketClass;
	private String					ticketStatus;
	private String					journeyDate;
	private String					chartStatus;
	private String					dateOfJourneyText;
	private String					currentStatus;
	private List<PassengerDataVo>	passengers;
	private PassengerDataVo			firstPassengerData;
	private long					journeyDateTimeStamp;

	/**
	 * @return the pnrNumber
	 */
	public String getPnrNumber() {
		return pnrNumber;
	}

	/**
	 * @param pnrNumber
	 *            the pnrNumber to set
	 */
	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
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

	/**
	 * @return the passengers
	 */
	public List<PassengerDataVo> getPassengers() {
		return passengers;
	}

	/**
	 * @param passengers
	 *            the passengers to set
	 */
	public void setPassengers(List<PassengerDataVo> passengers) {
		this.passengers = passengers;
	}

	/**
	 * @return the trainDest
	 */
	public String getDestination() {
		return trainDest;
	}

	/**
	 * @param trainDest
	 *            the trainDest to set
	 */
	public void setDestination(String trainDest) {
		this.trainDest = trainDest;
	}

	/**
	 * @return the trainJourney
	 */
	public String getTrainJourney() {
		return journeyDate;
	}

	/**
	 * @param trainJourney
	 *            the trainJourney to set
	 */
	public void setTrainJourney(String trainJourney) {
		this.journeyDate = trainJourney;
	}

	/**
	 * @return the trainName
	 */
	public String getTrainName() {
		return trainName;
	}

	/**
	 * @param trainName
	 *            the trainName to set
	 */
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	/**
	 * @return the trainNo
	 */
	public String getTrainNo() {
		return trainNo;
	}

	/**
	 * @param trainNo
	 *            the trainNo to set
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	/**
	 * @return the trainBoard
	 */
	public String getBoardingPoint() {
		return boardingPoint;
	}

	/**
	 * @param trainBoard
	 *            the trainBoard to set
	 */
	public void setBoardingPoint(String trainBoard) {
		this.boardingPoint = trainBoard;
	}

	/**
	 * @return the trainEmbark
	 */
	public String getEmbarkPoint() {
		return trainEmbark;
	}

	/**
	 * @param trainEmbark
	 *            the trainEmbark to set
	 */
	public void setEmbarkPoint(String trainEmbark) {
		this.trainEmbark = trainEmbark;
	}

	/**
	 * @return the firstPassengerData
	 */
	public PassengerDataVo getFirstPassengerData() {
		return firstPassengerData;
	}

	/**
	 * @param firstPassengerData
	 *            the firstPassengerData to set
	 */
	public void setFirstPassengerData(PassengerDataVo firstPassengerData) {
		this.firstPassengerData = firstPassengerData;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	/**
	 * @param ticketClass
	 *            the ticketClass to set
	 */
	public void setTicketClass(String ticketClass) {
		this.ticketClass = ticketClass;
	}

	/**
	 * @return the ticketClass
	 */
	public String getTicketClass() {
		return ticketClass;
	}

	/**
	 * @param rowId
	 *            the rowId to set
	 */
	public void setRowId(long rowId) {
		this.rowId = rowId;
	}

	/**
	 * @return the rowId
	 */
	public long getRowId() {
		return rowId;
	}

	public String getDateOfJourneyText() {
		return dateOfJourneyText;
	}

	public void setDateOfJourneyText(String dateOfJourneyText) {
		this.dateOfJourneyText = dateOfJourneyText;
	}

	public long getJourneyDateTimeStamp() {
		return journeyDateTimeStamp;
	}

	public void setJourneyDateTimeStamp(long journeyDateTimeStamp) {
		this.journeyDateTimeStamp = journeyDateTimeStamp;
	}

	public String getChartStatus() {
		return chartStatus;
	}

	public void setChartStatus(String chartStatus) {
		this.chartStatus = chartStatus;
	}

	@Override
	public int compareTo(PNRStatusVo another) {
		if (this.journeyDateTimeStamp < another.journeyDateTimeStamp) {
			return -1;
		} else if (this.journeyDateTimeStamp > another.journeyDateTimeStamp) {
			return 1;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardingPoint == null) ? 0 : boardingPoint.hashCode());
		result = prime * result + ((chartStatus == null) ? 0 : chartStatus.hashCode());
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + ((dateOfJourneyText == null) ? 0 : dateOfJourneyText.hashCode());
		result = prime * result + ((firstPassengerData == null) ? 0 : firstPassengerData.hashCode());
		result = prime * result + ((journeyDate == null) ? 0 : journeyDate.hashCode());
		result = prime * result + (int) (journeyDateTimeStamp ^ (journeyDateTimeStamp >>> 32));
		result = prime * result + ((passengers == null) ? 0 : passengers.hashCode());
		result = prime * result + ((pnrNumber == null) ? 0 : pnrNumber.hashCode());
		result = prime * result + (int) (rowId ^ (rowId >>> 32));
		result = prime * result + ((ticketClass == null) ? 0 : ticketClass.hashCode());
		result = prime * result + ((ticketStatus == null) ? 0 : ticketStatus.hashCode());
		result = prime * result + ((trainDest == null) ? 0 : trainDest.hashCode());
		result = prime * result + ((trainEmbark == null) ? 0 : trainEmbark.hashCode());
		result = prime * result + ((trainName == null) ? 0 : trainName.hashCode());
		result = prime * result + ((trainNo == null) ? 0 : trainNo.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		PNRStatusVo other = (PNRStatusVo) obj;
		if (boardingPoint == null) {
			if (other.boardingPoint != null)
				return false;
		} else if (!boardingPoint.equals(other.boardingPoint))
			return false;
		if (chartStatus == null) {
			if (other.chartStatus != null)
				return false;
		} else if (!chartStatus.equals(other.chartStatus))
			return false;
		if (currentStatus == null) {
			if (other.currentStatus != null)
				return false;
		} else if (!currentStatus.equals(other.currentStatus))
			return false;
		if (dateOfJourneyText == null) {
			if (other.dateOfJourneyText != null)
				return false;
		} else if (!dateOfJourneyText.equals(other.dateOfJourneyText))
			return false;
		if (firstPassengerData == null) {
			if (other.firstPassengerData != null)
				return false;
		} else if (!firstPassengerData.equals(other.firstPassengerData))
			return false;
		if (journeyDate == null) {
			if (other.journeyDate != null)
				return false;
		} else if (!journeyDate.equals(other.journeyDate))
			return false;
		if (journeyDateTimeStamp != other.journeyDateTimeStamp)
			return false;
		if (passengers == null) {
			if (other.passengers != null)
				return false;
		} else if (!passengers.equals(other.passengers))
			return false;
		if (pnrNumber == null) {
			if (other.pnrNumber != null)
				return false;
		} else if (!pnrNumber.equals(other.pnrNumber))
			return false;
		if (rowId != other.rowId)
			return false;
		if (ticketClass == null) {
			if (other.ticketClass != null)
				return false;
		} else if (!ticketClass.equals(other.ticketClass))
			return false;
		if (ticketStatus == null) {
			if (other.ticketStatus != null)
				return false;
		} else if (!ticketStatus.equals(other.ticketStatus))
			return false;
		if (trainDest == null) {
			if (other.trainDest != null)
				return false;
		} else if (!trainDest.equals(other.trainDest))
			return false;
		if (trainEmbark == null) {
			if (other.trainEmbark != null)
				return false;
		} else if (!trainEmbark.equals(other.trainEmbark))
			return false;
		if (trainName == null) {
			if (other.trainName != null)
				return false;
		} else if (!trainName.equals(other.trainName))
			return false;
		if (trainNo == null) {
			if (other.trainNo != null)
				return false;
		} else if (!trainNo.equals(other.trainNo))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PNRStatusVo [rowId=" + rowId + ", pnrNumber=" + pnrNumber + ", trainNo=" + trainNo + ", trainName="
				+ trainName + ", trainDest=" + trainDest + ", boardingPoint=" + boardingPoint + ", trainEmbark="
				+ trainEmbark + ", ticketClass=" + ticketClass + ", ticketStatus=" + ticketStatus + ", journeyDate="
				+ journeyDate + ", chartStatus=" + chartStatus + ", dateOfJourneyText=" + dateOfJourneyText
				+ ", currentStatus=" + currentStatus + ", passengers=" + passengers + ", firstPassengerData="
				+ firstPassengerData + ", journeyDateTimeStamp=" + journeyDateTimeStamp + "]";
	}

}