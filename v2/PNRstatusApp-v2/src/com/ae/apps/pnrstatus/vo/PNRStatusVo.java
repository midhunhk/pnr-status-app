package com.ae.apps.pnrstatus.vo;

import java.io.Serializable;
import java.util.List;


public class PNRStatusVo implements Serializable{

	private static final long serialVersionUID = 521018749963387159L;
	private long rowId;
	private String trainNo;
	private String pnrNumber;
	private String trainDest;
	private String trainName;
	private String trainBoard;
	private String ticketClass;
	private String trainEmbark;
	private String ticketStatus;
	private String trainJourney;
	private String trainCurrentStatus;
	private List<PassengerDataVo> passengers;
	private PassengerDataVo firstPassengerData;
	
	/**
	 * @return the pnrNumber
	 */
	public String getPnrNumber() {
		return pnrNumber;
	}
	/**
	 * @param pnrNumber the pnrNumber to set
	 */
	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
	}
	/**
	 * @return the trainCurrentStatus
	 */
	public String getTrainCurrentStatus() {
		return trainCurrentStatus;
	}
	/**
	 * @param trainCurrentStatus the trainCurrentStatus to set
	 */
	public void setTrainCurrentStatus(String trainCurrentStatus) {
		this.trainCurrentStatus = trainCurrentStatus;
	}
	/**
	 * @return the passengers
	 */
	public List<PassengerDataVo> getPassengers() {
		return passengers;
	}
	/**
	 * @param passengers the passengers to set
	 */
	public void setPassengers(List<PassengerDataVo> passengers) {
		this.passengers = passengers;
	}
	/**
	 * @return the trainDest
	 */
	public String getTrainDest() {
		return trainDest;
	}
	/**
	 * @param trainDest the trainDest to set
	 */
	public void setTrainDest(String trainDest) {
		this.trainDest = trainDest;
	}
	/**
	 * @return the trainJourney
	 */
	public String getTrainJourney() {
		return trainJourney;
	}
	/**
	 * @param trainJourney the trainJourney to set
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
	 * @param trainName the trainName to set
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
	 * @param trainNo the trainNo to set
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
	 * @param trainBoard the trainBoard to set
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
	 * @param trainEmbark the trainEmbark to set
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
	 * @param firstPassengerData the firstPassengerData to set
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
	 * @param ticketClass the ticketClass to set
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
	 * @param rowId the rowId to set
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
}