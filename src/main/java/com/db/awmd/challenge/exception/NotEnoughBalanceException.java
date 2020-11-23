package com.db.awmd.challenge.exception;

public class NotEnoughBalanceException extends RuntimeException{
	 
	private static final long serialVersionUID = 1L;

	public NotEnoughBalanceException(String message){
	        super(message);
	    }
}
