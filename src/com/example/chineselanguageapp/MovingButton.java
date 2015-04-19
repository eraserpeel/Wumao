package com.example.chineselanguageapp;

public class MovingButton {
	public int shuffledLocId;
	public int solvedLocId;
	public int shuffledPinyinLocId;
	public int solvedPinyinLocId; 
	
	public char character; 
	public String pinyin;
	
	public MovingButton() 
	{
		shuffledLocId = -1;
		solvedLocId = -1;
		character = ' ';
		String pinyin ="";
	}
}
