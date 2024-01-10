package com.ccp.especifications.db.setup;

public interface CcpDbSetupCreator {

	void createTables(String folder);

	void insertValues(String prefix, String folder);
	
	void dropAllTables();

	default void recreateAllTables(String prefix, String createFolder, String insertFolder) {
		this.dropAllTables();
		this.createTables(createFolder);
		this.insertValues(prefix, insertFolder);
	}
	
	void deleteAllData();
	
	default void reinsertAllTables(String prefix, String folder) {
		this.deleteAllData();
		this.insertValues(prefix, folder);
	}

}
