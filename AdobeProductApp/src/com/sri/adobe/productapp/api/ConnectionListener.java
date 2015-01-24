package com.sri.adobe.productapp.api;

public interface ConnectionListener {

	public void OnSuccess(String result);
	public void OnFailure(String error,int status);
	public void OnConnectionError(int errorCode);
	
}
