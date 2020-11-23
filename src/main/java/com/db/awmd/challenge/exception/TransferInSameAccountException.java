package com.db.awmd.challenge.exception;

public class TransferInSameAccountException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TransferInSameAccountException(String message){
        super(message);
    }
}
