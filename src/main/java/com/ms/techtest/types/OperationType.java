package com.ms.techtest.types;

public enum OperationType {
	
	ADD ("ADD"),
	SUBTRACT ("SUBTRACT"),
	MULTIPLY ("MULTIPLY");
	
	private String operationTypeCode;
	
	private OperationType(String operationTypeCode) {
		this.operationTypeCode=operationTypeCode;
	}
	
	public String getOperationTypeCode() {
		return operationTypeCode;
	}
}
