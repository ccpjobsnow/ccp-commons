package com.ccp.decorators;

public class CcpPasswordDecorator {

	public final String content;

	public CcpPasswordDecorator(String content) {
		this.content = content;
	}


	public boolean isStrongPassword() {
		if (this.content.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")){
		   return true;
		} 
		return false;
	}
	
}
