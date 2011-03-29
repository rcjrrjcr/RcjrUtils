package com.rcjrrjcr.bukkitplugins.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
/**
 * Utility class managing searching-related functions
 * @author rcjrrjcr
 */
public class SearchHelper {

	private HashSet<String> knownWords;
//	private static final Pattern splitWords= Pattern.compile("\\b(\\w+)\\b");
	public SearchHelper()
	{
		knownWords = new HashSet<String>();
	}
	/**
	 * Adds word to list of known words. If input contains spaces, it will be split into many words,
	 * <br/> each word being delimited by a word boundary, will be added.
	 * @param word New word
	 */
	public void addWord(String input)
	{
//		Matcher m = splitWords.matcher(input);
//		while(m.find())
//		{
//			knownWords.add(m.group());
//		}
		if(input!=null)knownWords.add(input);
	}
	
	public List<String> getMisses(String word)
	{
		 return getMisses(word,0);
	}
	/**
	 * Get possible correct spellings of a misspelled word.
	 * @param word Misspelled word
	 * @param threshold Maximum Damerau-Levenshtein distance
	 * @return List of possible near misses
	 */
	public List<String> getMisses(String word, final int threshold)
	{
		 if(knownWords.contains(word))
		 {
			 List<String> words = new LinkedList<String>();
			 words.add(word);
			 return words;
		 }
		 Map<String,Integer> weightedWords = new HashMap<String,Integer>(knownWords.size());
		 for(String s : knownWords)
		 {
			 int dist = getDistance(word,s);
			 if(threshold <= 0||!(dist < threshold)) weightedWords.put(s,dist);
		 }
		 LinkedHashMap<String,Integer> sortedWords = sortMapByValue(weightedWords, new Comparator<Integer>()
		 {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0.compareTo(arg1);
			}
			 
		 });
		 return new ArrayList<String>(sortedWords.keySet());
	}
	/**
	 * Searches and returns for words in knownWords which contain the input word.
	 * @param word Input word
	 * @param threshold Maximum difference between the length of the input word and the current word to be checked.
	 * @return List of words that contain the input word and are not longer than the length of the input word + <i>threshold</i>.
	 */
	public List<String> search(String word, final int threshold)
	{
//		List<String> nearMisses = getMisses(word, threshold);
		List<String> results = new LinkedList<String>();
		for(String known : knownWords)
		{
			if(known.length() < word.length()) continue;
			if(threshold > 0 && known.length() - word.length() > threshold) continue;
			if(known.contains(word)) results.add(word);
		}
		Collections.sort(results, new Comparator<String>(){

			@Override
			public int compare(String arg0, String arg1) {
				return new Integer(arg0.length()).compareTo(new Integer(arg1.length()));
			}
			
		});
//		results.addAll(nearMisses);
		return results;
		
	}
	/**
	 * Calculates Damerau-Levenshtein distance
	 * @param word First word
	 * @param ref Second word
	 * @return Damerau-Levenshtein distance
	 */
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
	
	public void clear()
	{
		knownWords.clear();
	}
	/**
	 * Utility function to get the minimum of all parameters.
	 * @param values Integers to compare
	 * @return Least integer
	 */
	private final static int minimum(int... values)
	{
		if(values.length==0) return 0;
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
	
	
//	private final static <E extends Comparable<E>> E minimum( E... values)
//	{
//		if(values.length==0) return null;
//		E min = values[0];
//		for(E val : values)
//		{
//			if(min.compareTo(val)==1)
//			{
//				min = val;
//			}
//		}
//		
//		return min;
//	}
	/**
	 * Utility function to sort maps by their values.
	 * @param <K> Class of key
	 * @param <V> Class of values
	 * @param unsortedMap Map to be sorted
	 * @param comparator Comparator to use on the values
	 * @return A LinkedHashMap, sorted by values. 
	 */
	public final static <K,V> LinkedHashMap<K,V> sortMapByValue(Map<K,V> unsortedMap, final Comparator<V> comparator)
	{
		if(unsortedMap.size() <= 1) return new LinkedHashMap<K,V>(unsortedMap);
		List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(unsortedMap.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return comparator.compare(o1.getValue(), o2.getValue());
			}
		}
		);
		LinkedHashMap<K,V> sortedMap = new LinkedHashMap<K, V>(unsortedMap.size());
		for(Map.Entry<K, V> entry : entryList)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}


