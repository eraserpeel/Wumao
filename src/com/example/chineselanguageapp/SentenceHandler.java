package com.example.chineselanguageapp;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import java.lang.String;
import java.util.Random; 
import android.util.*; 


public class SentenceHandler {

	private DatabaseHandler dbHandler; 
	private DatabaseHandler dbOpenHelper; 
	private SQLiteDatabase sentenceDB;
	private int   numOfSentenceType;
	private int   numOfSentences;
	private int[] numOfSentencesPerType; 
	private Random rn; 
	private int    currentEntryIndex;
	private String currentTable; 
	
	public int	  sentenceID;
	public int    viewCount; 
	public String englishSentence;
	public String chineseTrad;
	public String chineseSimp; 
	public String pinyin; 
	public int    levelDifficulty; 
	public int    masteredCount; 
	public Context currentContext; 
	
	public SentenceHandler(Context pIn) {
		currentContext = pIn; 
		DatabaseHandler dbOpenHelper = new DatabaseHandler(currentContext, "sentences1.db");
		sentenceDB = dbOpenHelper.openDataBase();
		
	}
	
		
	public String RandomSelect()
	{
		Cursor cursor = null; 
		String str   = ""; 
		String table = ""; 
		String countQuery = "";
		int [] masteryValue = new int[5];  
		int numOfRows = 0;
		int rowSelection;
		int rowCount;
		rn = new Random(); 
		
		for(int i = 0; i < 5; i++)
		{
			//Get our values for mastery
			try { 
				countQuery = "SELECT COUNT(*) FROM " + SettingsManager.getInstance().getDatabaseTableName()
													 + " WHERE successes_count=" + i;
				
				cursor = sentenceDB.rawQuery(countQuery, null);
				cursor.moveToFirst();
				 
				masteryValue[i] = Integer.parseInt(cursor.getString(0));
			}
			finally {
				if(cursor != null)
					cursor.close();
			}
			
			
		}
		 
		//Get the number of rows we have
		String rowsNumber  = "SELECT Count(*) FROM " + SettingsManager.getInstance().getDatabaseTableName(); 
		try {
			cursor = sentenceDB.rawQuery(rowsNumber, null);
			cursor.moveToFirst();
			rowCount = cursor.getInt(0);
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		
		
		double topPercent = 0.0;
		double topPercentLimit = (((double)rowCount - (double)masteryValue[4]) / (double)rowCount);
		
		int level = 0;
		if(topPercentLimit == 0.0) {
			topPercentLimit = 0.9;
					
		} 
		topPercent = 0.01;
		while(topPercent < topPercentLimit) {
			topPercent = topPercent + (double)masteryValue[level]/(double)rowCount;
			level++; 
		}
		
		int dev = rn.nextInt(10);
		if(dev < 3)
			level = 5;
		String selectQuery = "SELECT * FROM " + SettingsManager.getInstance().getDatabaseTableName()+
							 " WHERE successes_count=0 ";
		
		for(int i = 1; i < level; i++) {	
			 selectQuery = selectQuery + " OR successes_count=" + i; 
		}
		
		selectQuery = selectQuery + " ORDER BY RANDOM() LIMIT 1";
		try
		{
			
			cursor = sentenceDB.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) // data?
			{
				sentenceID      = Integer.parseInt(cursor.getString(0));
				englishSentence = cursor.getString(1);
				chineseTrad     = cursor.getString(2); 
				chineseSimp     = cursor.getString(3);
				pinyin          = cursor.getString(4);
				masteredCount   = Integer.parseInt(cursor.getString(8));
				viewCount       = Integer.parseInt(cursor.getString(7));
						
			}
			currentTable      = table; 
			currentEntryIndex = 0;
		}
		catch(Exception e)
		{
			Log.e("Msg", "Exception on table");
		}
		finally {
			cursor.close();
		}
		
		return selectQuery;
 	}//End RandomSelect

	public void incrementViewCount(int sentenceId, int newCount) {
	
		ContentValues values = new ContentValues(); 
		values.put("views", newCount);
		sentenceDB.update(SettingsManager.getInstance().getDatabaseTableName(), values, "sentence_id=" + sentenceId, null);
		
	}
	
	public void incrementMasterCount(int sentenceId, int newMasteredCount)
	{
		if(newMasteredCount < 5)
		{
			ContentValues values = new ContentValues(); 
			values.put("successes_count", newMasteredCount);
			sentenceDB.update(SettingsManager.getInstance().getDatabaseTableName(), values, "sentence_id=" + sentenceId, null);
		}
		
	}
	
	public void resetMasterCount(int sentenceId)
	{
		ContentValues values = new ContentValues(); 
		values.put("successes_count", 0);
		sentenceDB.update(SettingsManager.getInstance().getDatabaseTableName(), values, "sentence_id=" + sentenceId, null);
	}
	
	public void closeDB() {
		sentenceDB.close();
	}
		
}
