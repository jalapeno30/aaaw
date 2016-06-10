package com.ilts.anywhere.response.http;

public class NotFoundHttpResponse extends HttpResponse {
	
	public NotFoundHttpResponse(Object data) {
		super(HttpResponseType.NOTFOUND);
		construct();
		this.setData(data);
	}
	
	@Override
	protected void construct() {
		// TODO Auto-generated method stub

	}

}
