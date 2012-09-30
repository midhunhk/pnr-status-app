package com.ae.apps.pnrstatus.vo;

import java.io.Serializable;
import java.util.List;

import com.ae.apps.pnrstatus.utils.AppConstants;

import android.util.Log;

/**
 * Represents the PNRStatus Vo
 * 
 * @author midhun_harikumar
 * 
 */
public class PNRStatusVo implements Serializable, Comparable<PNRStatusVo> {

	private static final long		serialVersionUID	= 521018749963387159L;
	private long					rowId;
	private String					trainNo;
	private String					pnrNumber;
	private String					trainDest;
	private String					trainName;
	private String					trainBoard;
	private String					ticketClass;
	private String					trainEmbark;
	private String					ticketStatus;
	private String					trainJourney;
	private String					dateOfJourneyText;
	private String					trainCurrentStatus;
	private List<PassengerDataVo>	passengers;
	private PassengerDataVo			firstPassengerData;

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
		return trainCurrentStatus;
	}

	/**
	 * @param trainCurrentStatus
	 *            the trainCurrentStatus to set
	 */
	public void setCurrentStatus(String trainCurrentStatus) {
		this.trainCurrentStatus = trainCurrentStatus;
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
		return trainJourney;
	}

	/**
	 * @param trainJourney
	 *            the trainJourney to set
	 */
	public void setTrainJourney(String trainJourney) {
		this.trainJourney = trainJourney;
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
	public String getTrainBoard() {
		return trainBoard;
	}

	/**
	 * @param trainBoard
	 *            the trainBoard to set
	 */
	public void setTrainBoard(String trainBoard) {
		this.trainBoard = trainBoard;
	}

	/**
	 * @return the trainEmbark
	 */
	public String getTrainEmbark() {
		return trainEmbark;
	}

	/**
	 * @param trainEmbark
	 *            the trainEmbark to set
	 */
	public void setTrainEmbark(String trainEmbark) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstPassengerData == null) ? 0 : firstPassengerData.hashCode());
		result = prime * result + ((passengers == null) ? 0 : passengers.hashCode());
		result = prime * result + ((pnrNumber == null) ? 0 : pnrNumber.hashCode());
		result = prime * result + (int) (rowId ^ (rowId >>> 32));
		result = prime * result + ((ticketClass == null) ? 0 : ticketClass.hashCode());
		result = prime * result + ((ticketStatus == null) ? 0 : ticketStatus.hashCode());
		result = prime * result + ((trainBoard == null) ? 0 : trainBoard.hashCode());
		result = prime * result + ((trainCurrentStatus == null) ? 0 : trainCurrentStatus.hashCode());
		result = prime * result + ((trainDest == null) ? 0 : trainDest.hashCode());
		result = prime * result + ((trainEmbark == null) ? 0 : trainEmbark.hashCode());
		result = prime * result + ((trainJourney == null) ? 0 : trainJourney.hashCode());
		result = prime * result + ((trainName == null) ? 0 : trainName.hashCode());
		result = prime * result + ((trainNo == null) ? 0 : trainNo.hashCode());
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
		PNRStatusVo other = (PNRStatusVo) obj;
		if (firstPassengerData == null) {
			if (other.firstPassengerData != null)
				return false;
		} else if (!firstPassengerData.equals(other.firstPassengerData))
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
		if (trainBoard == null) {
			if (other.trainBoard != null)
				return false;
		} else if (!trainBoard.equals(other.trainBoard))
			return false;
		if (trainCurrentStatus == null) {
			if (other.trainCurrentStatus != null)
				return false;
		} else if (!trainCurrentStatus.equals(other.trainCurrentStatus))
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
		if (trainJourney == null) {
			if (other.trainJourney != null)
				return false;
		} else if (!trainJourney.equals(other.trainJourney))
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

	@Override
	public int compareTo(PNRStatusVo another) {
		Log.d(AppConstants.TAG, "1  : " + this.trainJourney);
		Log.d(AppConstants.TAG, "2  : " + another.trainJourney);
		Log.d(AppConstants.TAG, "Compare result  : " + this.trainJourney.compareTo(another.trainJourney));
		return this.trainJourney.compareTo(another.trainJourney);
	}

	public String getDateOfJourneyText() {
		return dateOfJourneyText;
	}

	public void setDateOfJourneyText(String dateOfJourneyText) {
		this.dateOfJourneyText = dateOfJourneyText;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PNRStatusVo [rowId=" + rowId + ", trainNo=" + trainNo + ", pnrNumber=" + pnrNumber + ", trainDest="
				+ trainDest + ", trainName=" + trainName + ", trainBoard=" + trainBoard + ", ticketClass="
				+ ticketClass + ", trainEmbark=" + trainEmbark + ", ticketStatus=" + ticketStatus + ", trainJourney="
				+ trainJourney + ", dateOfJourneyText=" + dateOfJourneyText + ", trainCurrentStatus="
				+ trainCurrentStatus + ", passengers=" + passengers + ", firstPassengerData=" + firstPassengerData
				+ "]";
	}
}