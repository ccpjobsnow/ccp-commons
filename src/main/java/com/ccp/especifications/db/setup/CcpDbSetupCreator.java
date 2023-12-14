package com.ccp.especifications.db.setup;

public interface CcpDbSetupCreator {

	void createTables(String folder);

	void insertValues(String folder);
	
	void dropAllTables();

	default void setup(String createFolder, String insertFolder) {
		this.dropAllTables();
		this.createTables(createFolder);
		this.insertValues(insertFolder);
	}

}
