package com.david.cursomc.resources.exception;

import java.io.Serializable;

public class FildMessage implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String fielName;
	private String message;
	
	public FildMessage() {
		
	}
	
	public FildMessage(String fielName, String message) {
		super();
		this.fielName = fielName;
		this.message = message;
	}

	public String getFielName() {
		return fielName;
	}
	public void setFielName(String fielName) {
		this.fielName = fielName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
