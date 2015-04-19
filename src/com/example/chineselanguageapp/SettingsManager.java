package com.example.chineselanguageapp;

public class SettingsManager {

	public static SettingsManager instance; 
	private boolean showCorrections = true; 
	private String databaseTableName = "Hsk_1_Set_1"; 
	private String databaseDisplayName = "Hsk 1 Set 1"; 
	
	private boolean isSimplifiedText = true; 
	
	public static SettingsManager getInstance()
	{
		if(instance == null)
			instance = new SettingsManager(); 
		
		return instance; 
	}
	
	public boolean getShowCorrections() 
	{
		return showCorrections;
	}
	
	public void setDatabaseTableName(String newName)
	{
		databaseTableName = newName; 
	}
	
	public void setDatabaseDisplayName(String newName) 
	{
		databaseDisplayName = newName;
	}
	
	public String getDatabaseDisplayName() 
	{
		return databaseDisplayName;
	}
	public String getDatabaseTableName() 
	{
		return databaseTableName; 
	}
	
	public void setLanguageType(boolean checkTextType)
	{
		isSimplifiedText= checkTextType;
	}
	
	public boolean getLanguageType() {
		return isSimplifiedText;
	}
	

}
