package com.example.chineselanguageapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import java.util.Random;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.*;


public class Play extends Activity {

	private MovingButton [] shuffledCards;
	private MovingButton [] solvedCards;
	private int [] solvedCardsAutoplay; 
		
	String currentStringSolved;
	String currentStringSolvedPinyin; 
	String currentStringShuffled;
	String [] pinyinSentenceArray;
	
	int currentStringLength; 
	int currentCardShuffled;
	int currentCardSolved;
	int lastButtonClickedId; 
	int cardsSeen; 
	int cardsCorrect; 
	int currentCardMastered;
	int currentCardViewCount;
	int currentCardId; 
	int currentAutoSlot;
	
	boolean pointCounted;
	boolean autoPlay;
	
	TextView txtMasteredCount; 
	TextView txtCardSetName; 
	SentenceHandler sHandler; 
	Button btnReturnToMenu;
	
	TextToSpeech tts; 
	private boolean isTTSInitialized = false;
	private static int TTS_DATA_CHECK = 1;
	
	/////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		//Variable assignments
		currentCardShuffled  = 0;
		currentCardSolved    = 0;
		currentCardViewCount = 0;
		currentCardId        = 0; 
		currentCardMastered  = 0;
		lastButtonClickedId  = 0;
		cardsSeen            = 0;
		cardsCorrect         = 0;
		pointCounted	     = false;
		autoPlay			 = false; 
		currentAutoSlot		 = 0;
		
		
		sHandler = new SentenceHandler(this.getBaseContext());
		
		autoPlay = true; 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
	
		
		txtMasteredCount = (TextView) findViewById(R.id.txtMastered);
		txtCardSetName = (TextView) findViewById(R.id.txtCardSetName);
		
		txtCardSetName.setText("Set: " + SettingsManager.getInstance().getDatabaseDisplayName());
		Log.e("Msg", "Here");
		
		
		loadButtonArray();  //Sets up all our IDs for the buttons
		clearButtons();
		loadNextCardSet();  //Randomly load next card
		confirmTTSData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	

	///////////////////////////////////////////////////////////////////
	void loadNextCardSet()
	{
		sHandler.RandomSelect();
		
		resetAutoplay();    //Sets array up with -1 for autoplay
		int i;
		int rnd1, rnd2; 
		char char1, char2;
		
		Button buttonTemp; 
		TextView pinyinText;
		if(SettingsManager.getInstance().getLanguageType() == true) {
			currentStringSolved           = sHandler.chineseSimp;
			currentStringLength	          = sHandler.chineseSimp.length();
		}
		else {
			currentStringSolved           = sHandler.chineseTrad;
			currentStringLength	          = sHandler.chineseTrad.length();
		}
		
	
		currentStringSolvedPinyin     = sHandler.pinyin;
		currentCardId 				  = sHandler.sentenceID;
		currentCardViewCount 		  = sHandler.viewCount;
		currentCardMastered           = sHandler.masteredCount; 
		String englishSentence 		  = sHandler.englishSentence;
		pinyinSentenceArray = currentStringSolvedPinyin.split("\\s+");
		pointCounted = false;
		Random myRnd = new Random();
		String str = currentStringSolved; 
		String tempPinyinWord; 
		changeMasteredText(currentCardMastered);
		
		//Shuffles the string before putting it in
		clearButtons(); 
		for(i = 0; i < 20; i++)
		{
			rnd1 = myRnd.nextInt(str.length());
			rnd2 = myRnd.nextInt(str.length());
			
			if(rnd1 != rnd2)
			{
				char1 = str.charAt(rnd1);
				char2 = str.charAt(rnd2); 
				char[] tempArray = str.toCharArray();
				
				tempPinyinWord = pinyinSentenceArray[rnd1];
				pinyinSentenceArray[rnd1] = pinyinSentenceArray[rnd2];
				pinyinSentenceArray[rnd2] = tempPinyinWord; 
					
				tempArray[rnd1] = char2;
				tempArray[rnd2] = char1; 
				
				str = String.valueOf(tempArray); 
			}
			
		}
		currentStringShuffled = str; 
		TextView txtTempEnglishText = (TextView)findViewById(R.id.lblEnglishSentence);
		txtTempEnglishText.setText(englishSentence); 
		
		for(i = 0; i < currentStringShuffled.length(); i++)
		{
			shuffledCards[i].character = currentStringShuffled.charAt(i);
			shuffledCards[i].solvedLocId = -1; 
			shuffledCards[i].pinyin = pinyinSentenceArray[i];
			
			buttonTemp = (Button) findViewById(shuffledCards[i].shuffledLocId);
			buttonTemp.setVisibility(View.VISIBLE);
			buttonTemp.setText("" + shuffledCards[i].character);
			
			buttonTemp = (Button) findViewById(solvedCards[i].solvedLocId);
			buttonTemp.setEnabled(true);
			
			pinyinText = (TextView)findViewById(shuffledCards[i].shuffledPinyinLocId);
			pinyinText.setText(shuffledCards[i].pinyin);
			pinyinText.setVisibility(View.VISIBLE);
		
		}
		
		
	
	}
	
	///////////////////////////////////////////////////////////////////
	
	
	void loadButtonArray() {
	
		shuffledCards = new MovingButton[18];
		solvedCards   = new MovingButton[18];
		
		for(int i = 0; i < 18; i++)
		{
			shuffledCards[i] = new MovingButton(); 
			solvedCards[i] = new MovingButton();
			
		}
		
		shuffledCards[0].shuffledLocId = R.id.btnRow1A;
		shuffledCards[1].shuffledLocId = R.id.btnRow1B;
		shuffledCards[2].shuffledLocId = R.id.btnRow1C;
		shuffledCards[3].shuffledLocId = R.id.btnRow1D;
		shuffledCards[4].shuffledLocId = R.id.btnRow1E;
		shuffledCards[5].shuffledLocId = R.id.btnRow1F;
		
		shuffledCards[6].shuffledLocId = R.id.btnRow2A;
		shuffledCards[7].shuffledLocId = R.id.btnRow2B;
		shuffledCards[8].shuffledLocId = R.id.btnRow2C;
		shuffledCards[9].shuffledLocId = R.id.btnRow2D;
		shuffledCards[10].shuffledLocId = R.id.btnRow2E;
		shuffledCards[11].shuffledLocId = R.id.btnRow2F;
		
		shuffledCards[12].shuffledLocId = R.id.btnRow3A;
		shuffledCards[13].shuffledLocId = R.id.btnRow3B;
		shuffledCards[14].shuffledLocId = R.id.btnRow3C;
		shuffledCards[15].shuffledLocId = R.id.btnRow3D;
		shuffledCards[16].shuffledLocId = R.id.btnRow3E;
		shuffledCards[17].shuffledLocId = R.id.btnRow3F;
		
		solvedCards[0].solvedLocId = R.id.btnRow4A;
		solvedCards[1].solvedLocId = R.id.btnRow4B;
		solvedCards[2].solvedLocId = R.id.btnRow4C;
		solvedCards[3].solvedLocId = R.id.btnRow4D;
		solvedCards[4].solvedLocId = R.id.btnRow4E;
		solvedCards[5].solvedLocId = R.id.btnRow4F;
		
		solvedCards[6].solvedLocId = R.id.btnRow5A;
		solvedCards[7].solvedLocId = R.id.btnRow5B;
		solvedCards[8].solvedLocId = R.id.btnRow5C;
		solvedCards[9].solvedLocId = R.id.btnRow5D;
		solvedCards[10].solvedLocId = R.id.btnRow5E;
		solvedCards[11].solvedLocId = R.id.btnRow5F;
		
		solvedCards[12].solvedLocId = R.id.btnRow6A;
		solvedCards[13].solvedLocId = R.id.btnRow6B;
		solvedCards[14].solvedLocId = R.id.btnRow6C;
		solvedCards[15].solvedLocId = R.id.btnRow6D;
		solvedCards[16].solvedLocId = R.id.btnRow6E;
		solvedCards[17].solvedLocId = R.id.btnRow6F;
		
		
		shuffledCards[0].shuffledPinyinLocId = R.id.pinyin1A;
		shuffledCards[1].shuffledPinyinLocId = R.id.pinyin1B;
		shuffledCards[2].shuffledPinyinLocId = R.id.pinyin1C;
		shuffledCards[3].shuffledPinyinLocId = R.id.pinyin1D;
		shuffledCards[4].shuffledPinyinLocId = R.id.pinyin1E;
		shuffledCards[5].shuffledPinyinLocId = R.id.pinyin1F;
		
		shuffledCards[6].shuffledPinyinLocId = R.id.pinyin2A;
		shuffledCards[7].shuffledPinyinLocId = R.id.pinyin2B;
		shuffledCards[8].shuffledPinyinLocId = R.id.pinyin2C;
		shuffledCards[9].shuffledPinyinLocId = R.id.pinyin2D;
		shuffledCards[10].shuffledPinyinLocId = R.id.pinyin2E;
		shuffledCards[11].shuffledPinyinLocId = R.id.pinyin2F;
		
		shuffledCards[12].shuffledPinyinLocId = R.id.pinyin3A;
		shuffledCards[13].shuffledPinyinLocId = R.id.pinyin3B;
		shuffledCards[14].shuffledPinyinLocId = R.id.pinyin3C;
		shuffledCards[15].shuffledPinyinLocId = R.id.pinyin3D;
		shuffledCards[16].shuffledPinyinLocId = R.id.pinyin3E;
		shuffledCards[17].shuffledPinyinLocId = R.id.pinyin3F;
		
		
		solvedCards[0].solvedPinyinLocId = R.id.pinyin4A;
		solvedCards[1].solvedPinyinLocId = R.id.pinyin4B;
		solvedCards[2].solvedPinyinLocId = R.id.pinyin4C;
		solvedCards[3].solvedPinyinLocId = R.id.pinyin4D;
		solvedCards[4].solvedPinyinLocId = R.id.pinyin4E;
		solvedCards[5].solvedPinyinLocId = R.id.pinyin4F;
		
		solvedCards[6].solvedPinyinLocId = R.id.pinyin5A;
		solvedCards[7].solvedPinyinLocId = R.id.pinyin5B;
		solvedCards[8].solvedPinyinLocId = R.id.pinyin5C;
		solvedCards[9].solvedPinyinLocId = R.id.pinyin5D;
		solvedCards[10].solvedPinyinLocId = R.id.pinyin5E;
		solvedCards[11].solvedPinyinLocId = R.id.pinyin5F;
		
		solvedCards[12].solvedPinyinLocId = R.id.pinyin6A;
		solvedCards[13].solvedPinyinLocId = R.id.pinyin6B;
		solvedCards[14].solvedPinyinLocId = R.id.pinyin6C;
		solvedCards[15].solvedPinyinLocId = R.id.pinyin6D;
		solvedCards[16].solvedPinyinLocId = R.id.pinyin6E;
		solvedCards[17].solvedPinyinLocId = R.id.pinyin6F;
	
	}
	///////////////////////////////////////////////////////////////////
	
	void clearButtons()
	{
		int i;
		Button buttonTemp; 
		TextView txtViewTemp; 
		Log.e("Msg", "Here2");
		for(i = 0; i < 18; i++)
		{
			buttonTemp = (Button) findViewById(shuffledCards[i].shuffledLocId);
			buttonTemp.setVisibility(View.INVISIBLE);
			buttonTemp.setTextColor(Color.rgb(2, 74, 104));
			
		}
		
		for(i = 0; i < 18; i++)
		{
			buttonTemp = (Button) findViewById(solvedCards[i].solvedLocId);
			buttonTemp.setVisibility(View.INVISIBLE);
			buttonTemp.setEnabled(false);
			buttonTemp.setTextColor(Color.rgb(2, 74, 104));
			buttonTemp.setClickable(true);
			
		}
		
		for(i = 0; i < 18; i++)
		{
			txtViewTemp = (TextView) findViewById(shuffledCards[i].shuffledPinyinLocId);
			txtViewTemp.setText(""); 
		}

		for(i = 0; i < 18; i++)
		{
			txtViewTemp = (TextView) findViewById(solvedCards[i].solvedPinyinLocId);
			txtViewTemp.setText("");
		}

		
	}
	///////////////////////////////////////////////////////////////////
	
	public void shuffledOnClick(View view) {
		
		
		//If it's autoplay then move the card down us that.
		if(autoPlay == true)
		{
			lastButtonClickedId = view.getId();
			int i = 0; 
		
			//Find the next autoplay slot
			while(solvedCardsAutoplay[i] != -1)
			{
				i++; 
			}
			currentAutoSlot = i; 
			solvedCardsAutoplay[i] = 1;
			//End find next Autoplay
			
			for(i = 0; i < 18; i++)
			{
				if(shuffledCards[i].shuffledLocId == lastButtonClickedId)
				{
					//Put everything for cards to move down here all SWAP
					Button solvedButtonNext = (Button) findViewById(solvedCards[currentAutoSlot].solvedLocId);
					
					//Switch button off
					Button shuffledClicked = (Button) findViewById(lastButtonClickedId);
					shuffledClicked.setVisibility(View.INVISIBLE);
					
					//Switch button on
					solvedButtonNext.setText(shuffledClicked.getText());	
					solvedButtonNext.setVisibility(View.VISIBLE);
					solvedButtonNext.setEnabled(true);
					//Switch on pinyin
					TextView pinyinShuffledClicked = (TextView)findViewById(shuffledCards[i].shuffledPinyinLocId);
					TextView pinyinSolvedClicked = (TextView)findViewById(solvedCards[currentAutoSlot].solvedPinyinLocId);
					
					pinyinSolvedClicked.setText("" + pinyinShuffledClicked.getText());
					pinyinShuffledClicked.setVisibility(View.INVISIBLE);
					
					shuffledCards[i].solvedLocId = solvedCards[currentAutoSlot].solvedLocId; 
				}
			}
			
			currentAutoSlot++; 
			
			
		}
		//If not autoplaying then setup to move down manually
		else
		{
			/*//If button has been selected
			if(lastButtonClickedId != 0)
			{
				//
				if(lastButtonClickedId != view.getId())
				{
					Button selectedButton = (Button) findViewById(lastButtonClickedId);
					selectedButton.setTextColor(Color.rgb(255, 255, 255));
					lastButtonClickedId = view.getId();
					selectedButton = (Button) findViewById(lastButtonClickedId);
					selectedButton.setTextColor(Color.rgb(98, 98, 122));
				}
				else
				{
					Button selectedButton = (Button) findViewById(lastButtonClickedId);
					selectedButton.setTextColor(Color.rgb(98, 98, 122));
					lastButtonClickedId = 0;
				}
				
			}
			//Nothing selected
			else if(lastButtonClickedId == 0)
			{
				lastButtonClickedId = view.getId();
				Button selectedButton = (Button) findViewById(lastButtonClickedId);
				selectedButton.setTextColor(Color.rgb(98, 98, 122));
			}
			
			*/
		}
		
	}
	///////////////////////////////////////////////////////////////////
		
	public void solvedOnClick(View view) {
		
		int buttonClickedId = view.getId();
		int shuffledClickedPinyinId = -1;
		int solvedMovePinyinId = -1;
		int solvedErasePinyinId = -1;
		
		Button solvedButtonClicked = (Button) findViewById(buttonClickedId);
		
		//If there is a shuffled button clicked then move it down. 
		//if((lastButtonClickedId != 0) && (solvedButtonClicked.getText() == "") && (autoPlay == false))
		//{
		/*	for(int i = 0; i < 18; i++)
			{
				if(shuffledCards[i].shuffledLocId == lastButtonClickedId)
				{
					//Put everything for cards to move down here all SWAP
					shuffledCards[i].solvedLocId = buttonClickedId;
					shuffledClickedPinyinId = shuffledCards[i].shuffledPinyinLocId;
				}
			}
			
			for(int i = 0; i < 18; i++)
			{
				if(solvedCards[i].solvedLocId == buttonClickedId)
				{
					solvedMovePinyinId = solvedCards[i].solvedPinyinLocId;
					solvedCardsAutoplay[i] = 1; 
				}
			}

			//Switch button off
			Button shuffledClicked = (Button) findViewById(lastButtonClickedId);
			shuffledClicked.setVisibility(View.INVISIBLE);
			
			//Switch button on
			solvedButtonClicked.setText("" + shuffledClicked.getText());	
			solvedButtonClicked.setVisibility(View.VISIBLE);
			
			//Switch on pinyin
			TextView pinyinShuffledClicked = (TextView)findViewById(shuffledClickedPinyinId);
			TextView pinyinSolvedClicked = (TextView)findViewById(solvedMovePinyinId);
			
			pinyinSolvedClicked.setText("" + pinyinShuffledClicked.getText());
			pinyinShuffledClicked.setVisibility(View.INVISIBLE);
			lastButtonClickedId = 0; 
		*/
		//}
		
		//If no shuffled button and nothing selected clicked then move it up.
		if(solvedButtonClicked.isEnabled())
		{
			solvedButtonClicked.setEnabled(false);
			solvedButtonClicked.setVisibility(View.INVISIBLE);
			
			//Go through the cards and find the shuffledLocID that matches the solved Id.. 
			for(int i = 0; i < currentStringLength; i++)
			{
				if(shuffledCards[i].solvedLocId == buttonClickedId)
				{
					Button shuffledMoveBack = (Button) findViewById(shuffledCards[i].shuffledLocId);
					shuffledMoveBack.setVisibility(View.VISIBLE);
					shuffledMoveBack.setTextColor(Color.rgb(2, 7, 104));
					
					TextView shuffledPinyinMoveBack = (TextView)findViewById(shuffledCards[i].shuffledPinyinLocId);
					shuffledPinyinMoveBack.setVisibility(View.VISIBLE);
					
					
					for(int j = 0; j < 18; j++)
					{
						if(solvedCards[j].solvedLocId == shuffledCards[i].solvedLocId)
						{
							solvedErasePinyinId = solvedCards[j].solvedPinyinLocId;
							solvedCardsAutoplay[j] = -1; 
						}
						
					}
					
					TextView solvedPinyinErase = (TextView)findViewById(solvedErasePinyinId);
					solvedPinyinErase.setText("");
					
				}
			}
		}
	}
	
	public void solveSentence(View view)
	{
		clearButtons(); 
		char[] tempArray = currentStringSolved.toCharArray();
		String [] pinyinSentenceArray = currentStringSolvedPinyin.split("\\s+");
		
		changeMasteredText(0);
		
		for(int i = 0; i < currentStringLength; i++)
		{
			Button solvedTemp = (Button) findViewById(solvedCards[i].solvedLocId);
			TextView pinyinTemp = (TextView) findViewById(solvedCards[i].solvedPinyinLocId);
			pinyinTemp.setText(pinyinSentenceArray[i]);
 
			
			String temp1 = "" + tempArray[i];
			solvedTemp.setText(temp1); 
			solvedTemp.setClickable(false);  
			solvedTemp.setVisibility(View.VISIBLE);
			
		}
				
		if(pointCounted == false)
		{
			sHandler.incrementViewCount(currentCardId, currentCardViewCount++);
			cardsSeen++;
		}
		
		
		pointCounted = true;
		currentAutoSlot = 0;
		
		Button btn = (Button) findViewById(R.id.btnCheckNext);
		if(btn.getText() == "Check")
		{
			btn.setText("Next"); 
		}
		
		changeMasteredText(0);
				
	}
	
	public void checkSentence(View view) {
		boolean isSolved = true;
		char[] tempArray = currentStringSolved.toCharArray();
		Button buttonTemp = (Button) findViewById(R.id.btnSolve);

		
		for(int i = 0; i < currentStringLength; i++) 
		{
			Button solvedTemp = (Button) findViewById(solvedCards[i].solvedLocId);
			String temp1 = "" + tempArray[i];
			String temp2 = "" + solvedTemp.getText(); 
			solvedTemp.setTextColor(Color.argb(255, 0, 150, 50));
			if(temp1.compareTo(temp2) != 0) 
			{
				isSolved = false;
				solvedTemp.setTextColor(Color.RED);
			}			
		}
	
		if(pointCounted != true)
		{
			if(isSolved)
			{
				cardsCorrect++;
				if(currentCardMastered < 4)
					currentCardMastered++; 
				sHandler.incrementMasterCount(currentCardId, currentCardMastered);
				changeMasteredText(currentCardMastered);
				
			}
			else
			{
				currentCardMastered = 0; 
				sHandler.resetMasterCount(currentCardId);
				changeMasteredText(currentCardMastered);
				
			}
			cardsSeen++;
			currentCardViewCount++;
			sHandler.incrementViewCount(currentCardId, currentCardViewCount);
		}
		pointCounted = true;
		currentAutoSlot = 0;
	}
	
	
	public void checkNextCard(View view)
	{
		
		Button btn = (Button) findViewById(view.getId());
		btn.setPressed(true);
		if(btn.getText() == "Next")
		{
			loadNextCardSet();	
			btn.setText("Check"); 
		}
		else
		{
			checkSentence(view);
			btn.setText("Next"); 
		}
		 
	}
	
	
	//This resets the array for our autoplay (smart setup)
	//-1 means empty and 1 means full!
	public void resetAutoplay()
	{
		solvedCardsAutoplay = new int[18]; 
		currentAutoSlot = 0; 
		for(int i = 0; i < 18; i++)
		{
			solvedCardsAutoplay[i] = -1;
		}

	}
	
	
	/*public void toggleAutoplay(View view)
	{
		lastButtonClickedId = 0; 
		if(autoPlay == false)
		{
			
			autoPlay = true;
		}
		else
		{
			autoPlay = false;
		}
	}*/
	
	public void resetText(View view) {
		
		clearButtons();
		resetAutoplay();
		//For the sentence
		Button buttonTemp;
		TextView pinyinText;
		buttonTemp = (Button) findViewById(R.id.btnReset);
		buttonTemp.setPressed(true);
		for(int i = 0; i < currentStringShuffled.length(); i++)
		{
			shuffledCards[i].character = currentStringShuffled.charAt(i);
			shuffledCards[i].solvedLocId = -1; 
			shuffledCards[i].pinyin = pinyinSentenceArray[i];
			
			buttonTemp = (Button) findViewById(shuffledCards[i].shuffledLocId);
			buttonTemp.setVisibility(View.VISIBLE);
			buttonTemp.setText("" + shuffledCards[i].character);
			
			buttonTemp = (Button) findViewById(solvedCards[i].solvedLocId);
			buttonTemp.setVisibility(View.VISIBLE);
			buttonTemp.setText("");
			
			pinyinText = (TextView)findViewById(shuffledCards[i].shuffledPinyinLocId);
			pinyinText.setText(shuffledCards[i].pinyin);
			pinyinText.setVisibility(View.VISIBLE);
		
		}
		
	}
	
	public void speakSentence() {
		Log.e("Msg", "HEre");
		//speech.speakSentence(currentStringSolved);
	}
	
	public void changeMasteredText(int level)
	{
		txtMasteredCount.setText("Status: Unknown");
		
		switch(level)
		{
			case 0:
				txtMasteredCount.setText("Status: 0 / 4");
			break;
			
			case 1:
				txtMasteredCount.setText("Status: 1 / 4");
			break;
			
			case 2:
				txtMasteredCount.setText("Status: 2 / 4");
			break;
			
			case 3:
				txtMasteredCount.setText("Status: 3 / 4");
			break;
			
			case 4:
				txtMasteredCount.setText("Status: Mastered");
			break;
			
		}
	}
	
	 private void confirmTTSData()  {
		 	Log.e("Msg", "HGErsda12312f");	
		 	Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
	    	startActivityForResult(intent, TTS_DATA_CHECK);
	    	
	    }

	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 Log.e("Msg", "HGErsda2132f");
		 if (requestCode == TTS_DATA_CHECK) {
	    	if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
	    		//Voice data exists		
	    		initializeTTS();
	    	}
	    	else {
	    		Intent installIntent = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
	    		startActivity(installIntent);
	    	}
	    }
	 }
	    
	 private void initializeTTS() {
	   	Log.e("Msg", "HGErsdaf");
		tts = new TextToSpeech(this, new OnInitListener() {
	   		public void onInit(int status) {
	   			if (status == TextToSpeech.SUCCESS) {
	   				isTTSInitialized = true;
	   			}
	   			else {
	   				//Handle initialization error here
	   				isTTSInitialized = false;
	   			}
	   		}
	   	});
	 }
	    
	 public void toSpeak(View view) {
		Log.e("Msg", "HGErsda123123123f");
	   	if(isTTSInitialized) {
	   		if (tts.isLanguageAvailable(Locale.CHINESE) >= 0) 
	   			tts.setLanguage(Locale.CHINESE);
	    		tts.setPitch(0.8f);
	    		tts.setSpeechRate(0.8f);
	    		tts.speak(currentStringSolved, TextToSpeech.QUEUE_ADD, null);
	    }
	 }
	    
	 @Override
	 public void onDestroy() {
	   	if (tts != null) {
	   		tts.stop();
	   		tts.shutdown();
	   	}
	   	super.onDestroy();
	 }
}
