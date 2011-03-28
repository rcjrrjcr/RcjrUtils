package com.rcjrrjcr.bukkitplugins.util;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchHelper {

	private HashSet<String> knownWords;
	private static final Pattern firstWord= Pattern.compile("^(\\S*)\\s");
	public SearchHelper()
	{
		knownWords = new HashSet<String>();
	}
	
	public void addWord(String word)
	{
		Matcher m = firstWord.matcher(word);
		//TODO: Write code here
	}
}
