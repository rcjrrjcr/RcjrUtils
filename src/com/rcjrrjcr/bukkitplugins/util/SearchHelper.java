package com.rcjrrjcr.bukkitplugins.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
		String[] words = firstWord.split(" ");
		for(String w : words)
		{
			knownWords.add(w);
		}
	}
	
	public List<String> getMatches(String word)
	{
		 if(knownWords.contains(word))
		 {
			 List<String> words = new LinkedList<String>();
			 words.add(word);
			 return words;
		 }
		 List<WeightedString> weightedWords = new ArrayList<WeightedString>(knownWords.size());
		 for(String s : knownWords)
		 {
			 weightedWords.add(new WeightedString(getDistance(word,s),s));
		 }
		 Collections.sort(weightedWords);
		 List<String> wordList = new LinkedList<String>();
		 for(WeightedString string : weightedWords)
		 {
			 wordList.add(string.getString());
		 }
		 return wordList;
	}
	
	
	
	private Integer getDistance(String word, String reference)
	{
		//TODO: Write code to find distance between two words
		return 1;
	}
}

class WeightedString implements Comparable<WeightedString>
{
	private int val;
	private String string;
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public WeightedString(int val, String string) {
		this.val = val;
		this.string = string;
	}
	@Override
	public int compareTo(WeightedString o) {
		return new Integer(this.val).compareTo(new Integer(o.val));
	}
}
