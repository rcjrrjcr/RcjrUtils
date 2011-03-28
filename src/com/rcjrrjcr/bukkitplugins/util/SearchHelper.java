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
	
	
	
	private Integer getDistance(String word, String ref)
	{
		//TODO: Optimi[zs]e code to find the Damerau-Levenshtein distance
		final int wordLength = word.length();
		final int refLength = ref.length();
		int mat[][] = new int[wordLength+1][refLength+1];
		
		for(int i = 0; i<= wordLength; i++)
		{
			mat[i][0] = i;
		}
		for(int j = 0; j<= refLength; j++)
		{
			mat[0][j] = j;
		}
		int cost;
		
		for(int j = 1; j <= refLength; j++)
		{
			for(int i = 1; i <= wordLength; i++)
			{
				cost = word.codePointAt(i)==ref.codePointAt(j) ? 0 : 1;
				mat[i][j] = minimum(mat[i-1][j],mat[i][j-1],mat[i-1][j-1]) + 1;
				
				if(i > 1 && j > 1 &&(word.codePointAt(i) == ref.codePointAt(j-1)) && (word.codePointAt(i-1) == ref.codePointAt(j)))
				{
					mat[i][j] = minimum(mat[i][j],mat[i-2][j-2]+cost);
				}
			}			
		}
		
		return mat[wordLength][refLength];
	}
	
	private final static int minimum(int... values)
	{
		int min = values[0];
		for(int val : values)
		{
			if(min > val)
			{
				min = val;
			}
		}
		
		return min;
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
