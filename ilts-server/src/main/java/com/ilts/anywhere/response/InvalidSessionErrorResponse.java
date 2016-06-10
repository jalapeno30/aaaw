package com.ilts.anywhere.response;

public class InvalidSessionErrorResponse extends Response {

	InvalidSessionErrorResponse() {
		super(ResponseType.ERRORINVALIDSESSION);
		construct();
	}

	@Override
	protected void construct() {
		System.out.println("Session is invalid");
		setStatus("error");
	}

}
