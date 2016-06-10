package com.ilts.anywhere.response;

public class NewOrderResponse extends Response {

	NewOrderResponse() {
		super(ResponseType.SUCCESSNEWORDER);
		construct();
	}

	@Override
	protected void construct() {
		System.out.println("Successfully added order");
		setStatus("success");
	}

}
