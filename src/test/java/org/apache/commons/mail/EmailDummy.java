package org.apache.commons.mail;

import java.util.Map;

public class EmailDummy extends Email {

	@Override
	public Email setMsg(String msg) throws EmailException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String, String> getHeaders() {
		return this.headers;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	/*
	@Override
	public void buildMimeMessage() {
		buildMimeMessage();
	}*/

}
