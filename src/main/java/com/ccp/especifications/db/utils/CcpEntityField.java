package com.ccp.especifications.db.utils;

public interface CcpEntityField {

	String name();
	
	boolean isPrimaryKey();
	
	CcpEntityField TIMESTAMP = new CcpEntityField() {
		
		public String name() {
			return "timestamp";
		}
		
		public boolean isPrimaryKey() {
			return false;
		}
	};

	CcpEntityField DATE = new CcpEntityField() {
		
		public String name() {
			return "date";
		}
		
		public boolean isPrimaryKey() {
			return false;
		}
	};
}
