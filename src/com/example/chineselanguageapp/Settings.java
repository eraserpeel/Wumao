package com.example.chineselanguageapp;

import java.io.IOException;
import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class Settings extends Activity {

	private SentenceHandler sHandler;
	private SQLiteDatabase sentenceDB;
	private int maxCount;
	ArrayList<SettingsRowItem> tableListArray;  
	ListView lvSets; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_settings);
	    
	    maxCount = 20;
	    tableListArray = new ArrayList<SettingsRowItem>();
		final ListView lvTables = (ListView) findViewById(R.id.listView1);
		final ArrayList<String> theSetList = new ArrayList<String>();
	   
		loadLists(20);
		
		final CustomListAdapter theDecksList = new CustomListAdapter(this, tableListArray);
		lvTables.setAdapter(theDecksList);
		
        RadioButton rb;
		rb = (RadioButton) findViewById(R.id.radioButtonTraditional);
		if(SettingsManager.getInstance().getLanguageType() ==true) {
			
			rb.setChecked(false);
		}
		else {
			rb.setChecked(true);
		}
			
		
		lvTables.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					
					Object o = lvTables.getAdapter().getItem(position);
					//theDecksList.setSelectedIndex(position);
					SettingsRowItem st = (SettingsRowItem) lvTables.getAdapter().getItem(position);
					SettingsManager.getInstance().setDatabaseTableName(st.itemDBName);
					SettingsManager.getInstance().setDatabaseDisplayName(st.itemText);
					view.setPressed(true);
					view.setSelected(true);
			}
		});	
		
		rb = (RadioButton) findViewById(R.id.radioButtonTraditional);
		rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

	                //handle the boolean flag here. 
	                  if(arg1==true)
	                    SettingsManager.getInstance().setLanguageType(false);     
	                    //do something else
	            }
	        });
		
		rb = (RadioButton) findViewById(R.id.radioButtonSimplified);
		rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

	                //handle the boolean flag here. 
	                  if(arg1==true)
	                    SettingsManager.getInstance().setLanguageType(true);     
	                    //do something else
	            }
	        });

	}
	
	public void loadLists(int maxCount) {

		Cursor cursor = null;
		Cursor countProgressCursor = null; 
		String query;
		int rowCount = 1;
		int successCount = 0;
		tableListArray.clear(); 
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
						query = "SELECT SUM(successes_count) FROM " + cursor.getString(0); 
						countProgressCursor = sentenceDB.rawQuery(query, null);
						
						if(countProgressCursor.moveToFirst()) {
						    successCount = countProgressCursor.getInt(0);
						}
					}
					catch (Exception e) {
				            Log.e("Msg", "Error Here");
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

					if(rowCount == 0) {
						rowCount = 1;
						successCount = 1;
					}
					String menuName = cursor.getString(0).replaceAll("_", " ");
					tableListArray.add(new SettingsRowItem(menuName, cursor.getString(0), ((100 * successCount)/(rowCount * 4))));
						
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
	}
}


