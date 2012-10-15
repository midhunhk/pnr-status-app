package com.ae.apps.pnrstatus.vo;

import java.io.Serializable;


public class PassengerDataVo implements Serializable
{
	private static final long serialVersionUID = 2264807366835227895L;
	private String trainPassenger;
	private String trainBookingBerth;
	private String trainCurrentStatus;
	private String berthPosition;
	
	/**
	 * @return the trainBookingBerth
	 */
	public String getTrainBookingBerth() {
		return trainBookingBerth;
	}
	
	/**
	 * @param trainBookingBerth the trainBookingBerth to set
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
	 * @param trainPassenger the trainPassenger to set
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
	 * @param trainCurrentStatus the trainCurrentStatus to set
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
	
}