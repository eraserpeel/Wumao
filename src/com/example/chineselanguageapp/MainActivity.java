package com.example.chineselanguageapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.database.sqlite.*;
import java.io.IOException;
import android.view.*;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.*;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity {

	Button bPlay;
	Button bStats;
	Button bSettings;
	Button bAbout; 
	EditText mytext; 
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		SettingsManager.getInstance().setDatabaseTableName("Hsk_1_Set_1");
	}		      		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addListenerOnButton() {

        //Select a specific button to bundle it with the action you want
		bPlay	    = (Button) findViewById(R.id.bPlay);
		bStats 		= (Button) findViewById(R.id.bStatistics);
		bSettings   = (Button) findViewById(R.id.bSettings);
		bAbout      = (Button) findViewById(R.id.bAbout);
		
		bPlay.setOnClickListener(
				new OnClickListener()
				{ 
					public void onClick(View view) {
						
						Intent i = new Intent(getApplicationContext(), Play.class);
						startActivity(i);
				
					}
				});
		bStats.setOnClickListener(
				new OnClickListener()
				{ 
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), Statistics.class);
						startActivity(i);
					
					}
				});
		bSettings.setOnClickListener(
				new OnClickListener()
				{ 
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), Settings.class);
						startActivity(i);
					
					}
				});
		bAbout.setOnClickListener(
				new OnClickListener()
				{ 
					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(), About.class);
						startActivity(i);
					
					}
				});

	}

}
