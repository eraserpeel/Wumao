package com.example.chineselanguageapp;



public class SettingsRowItem {
	public String itemText;
	public int itemProgress;
	public String itemDBName;
	
	SettingsRowItem(String it, String dbName, int ip) {
		itemText = it; 
		itemDBName = dbName;
		itemProgress = ip;
	}
}
