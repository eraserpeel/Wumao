package com.example.chineselanguageapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Statistics extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		SentenceHandler sHandler;
		SQLiteDatabase sentenceDB = null;
		
		Cursor cursor = null;
		Cursor countProgressCursor = null; 
		String query;
		int rowCount = 1;
		int successCount = 0;
		int lvl0 = 0;
		int lvl1 = 0;
		int lvl2 = 0; 
		int lvl3 = 0;
		int lvl4 = 0; 
		int totalCards = 0;
		
		try {
			DatabaseHandler dbOpenHelper = new DatabaseHandler(this.getBaseContext(), "sentences1.db");
		    String selectQuery = "SELECT name FROM sqlite_master WHERE type='table'";
			sentenceDB = dbOpenHelper.openDataBase();
			cursor = sentenceDB.rawQuery(selectQuery, null);
			cursor.moveToFirst();
			
			int count = 0;
			while(!cursor.isLast()) {
				count++;
				cursor.moveToNext();
				if((!cursor.getString(0).equals("Settings")) &&
				   (!cursor.getString(0).equals("Base"))) {
					
					
					try {
						query = "SELECT COUNT(*) FROM " + cursor.getString(0); 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						if(countProgressCursor.moveToFirst()) {
							totalCards = totalCards + countProgressCursor.getInt(0);
						}
						countProgressCursor.close();
						
						query = "SELECT COUNT(*) FROM " + cursor.getString(0) + " WHERE successes_count=0"; 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						if(countProgressCursor.moveToFirst()) {
							lvl0 = lvl0 + countProgressCursor.getInt(0);
						}
						countProgressCursor.close(); 
						
						query = "SELECT COUNT(*) FROM " + cursor.getString(0) + " WHERE successes_count=1"; 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						if(countProgressCursor.moveToFirst()) {
						    lvl1 = lvl1 + countProgressCursor.getInt(0);
						}
						countProgressCursor.close(); 
						
						query = "SELECT COUNT(*) FROM " + cursor.getString(0) + " WHERE successes_count=2"; 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						if(countProgressCursor.moveToFirst()) {
						    lvl2 = lvl2 + countProgressCursor.getInt(0);
						}
						countProgressCursor.close(); 
						
						query = "SELECT COUNT(*) FROM " + cursor.getString(0) + " WHERE successes_count=3"; 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						if(countProgressCursor.moveToFirst()) {
						    lvl3 = lvl3 + countProgressCursor.getInt(0);
						}
						countProgressCursor.close(); 
						
						query = "SELECT COUNT(*) FROM " + cursor.getString(0) + " WHERE successes_count=4";
						Log.e("Msg", "Here: " + query);
						countProgressCursor = sentenceDB.rawQuery(query, null);
						
						if(countProgressCursor.moveToFirst()) {
						    lvl4 = lvl4 + countProgressCursor.getInt(0);
						}
						countProgressCursor.close(); 

					}
					catch (Exception e) {
				            Log.e("Msg", "Error Here: " + e);
				     }
					finally {
						countProgressCursor.close();
					}
					
					try {
						query = "SELECT COUNT(*) FROM " + cursor.getString(0); 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						
						if(countProgressCursor.moveToFirst()) {
							rowCount = countProgressCursor.getInt(0);
						}
					}
					catch (Exception e) {
				        Log.e("Msg", "Error Here");
				    }
					finally {
						countProgressCursor.close();
					}
				}
			}
		}
		
		catch (Exception e) {
            Log.e("Msg", "Error Here");
         
	    }
		finally {
			cursor.close();
			sentenceDB.close();
		}
		
		TextView tx = (TextView) findViewById(R.id.txtCardsTotal);
		tx.setText("" + totalCards);
		
		tx = (TextView) findViewById(R.id.txtLevel0);
		tx.setText("" + lvl0);
		
		tx = (TextView) findViewById(R.id.txtLevel1);
		tx.setText("" + lvl1);
		
		tx = (TextView) findViewById(R.id.txtLevel2);
		tx.setText("" + lvl2);
		
		tx = (TextView) findViewById(R.id.txtLevel3);
		tx.setText("" + lvl3);
		
		tx = (TextView) findViewById(R.id.txtLevel4);
		tx.setText("" + lvl4);
	}


}
