package com.ae.apps.pnrstatus.vo;

public class EmptyPNRStatusVo extends PNRStatusVo {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1737184279550777140L;

	public EmptyPNRStatusVo() {
		super();
		setCurrentStatus("---");
		setTicketStatus("--");
	}

}
