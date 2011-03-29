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
	private final static int[] ZERO_LENGTH_INT_ARRAY = new int[0];
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
			 int dist = damlev(word,s);
			 if(threshold > 0||!(dist < threshold)) weightedWords.put(s,dist);
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
//	/**
//	 * Calculates Damerau-Levenshtein distance
//	 * @param str1 First word
//	 * @param str2 Second word
//	 * @return Damerau-Levenshtein distance
//	 */
//	public static Integer getDistance(String str1, String str2)
//	{
//		int[][] distance = new int[str1.length() + 1][str2.length() + 1];
//		 
//        for (int i = 0; i <= str1.length(); i++)
//                distance[i][0] = i;
//        for (int j = 0; j <= str2.length(); j++)
//                distance[0][j] = j;
//
//        for (int i = 1; i <= str1.length(); i++)
//        {
//                for (int j = 1; j <= str2.length(); j++)
//                {
//                        distance[i][j] = minimum(
//                                        distance[i - 1][j] + 1,
//                                        distance[i][j - 1] + 1,
//                                        distance[i - 1][j - 1]
//                                                        + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
//                                                                        : 1));
//                }
//        }
//        return distance[str1.length()][str2.length()];
//	}
	
	public void clear()
	{
		knownWords.clear();
	}
//	/**
//	 * Utility function to get the minimum of all parameters.
//	 * @param values Integers to compare
//	 * @return Least integer
//	 */
//	private final static int minimum(int... values)
//	{
//		if(values.length==0) return 0;
//		int min = values[0];
//		for(int val : values)
//		{
//			if(min > val)
//			{
//				min = val;
//			}
//		}
//		
//		return min;
//	}
	
	
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
	

	//COPYCODE START: This code was copied from	http://blog.lolyco.com/sean/damerau-levenshtein/. I do not take credit for this code.
	public static int damlev(String s, String t)
	{
		if (s != null && t != null)
			return damlev(s, t, getWorkspace(s.length(), t.length()));
		else
			return damlev(s, t, ZERO_LENGTH_INT_ARRAY);
	}
	public static int damlevlim(String s, String t, int limit)
	{
		if (s != null && t != null)
			return damlevlim(s, t, limit, getWorkspace(s.length(), t.length()));
		else
			return damlevlim(s, t, limit, ZERO_LENGTH_INT_ARRAY);
	}
	public static int[] getWorkspace(int sl, int tl)
	{
		return new int[(sl + 1) * (tl + 1)];
	}
	private static int damlev(String s, String t, int[] workspace)
	{
		int lenS = s.length();
		int lenT = t.length();
		int lenS1 = lenS + 1;
		int lenT1 = lenT + 1;
		if (lenT1 == 1)
			return lenS1 - 1;
		if (lenS1 == 1)
			return lenT1 - 1;
		int[] dl = workspace;
		int dlIndex = 0;
		int sPrevIndex = 0, tPrevIndex = 0, rowBefore = 0, min = 0, cost = 0, tmp = 0;
		int tri = lenS1 + 2;
		// start row with constant
		dlIndex = 0;
		for (tmp = 0; tmp < lenT1; tmp++)
		{
			dl[dlIndex] = tmp;
			dlIndex += lenS1;
		}
		for (int sIndex = 0; sIndex < lenS; sIndex++)
		{
			dlIndex = sIndex + 1;
			dl[dlIndex] = dlIndex; // start column with constant
			for (int tIndex = 0; tIndex < lenT; tIndex++)
			{
				rowBefore = dlIndex;
				dlIndex += lenS1;
				//deletion
				min = dl[rowBefore] + 1;
				// insertion
				tmp = dl[dlIndex - 1] + 1;
				if (tmp < min)
					min = tmp;
				cost = 1;
				if (s.charAt(sIndex) == t.charAt(tIndex))
					cost = 0;
				if (sIndex > 0 && tIndex > 0)
				{
					if (s.charAt(sIndex) == t.charAt(tPrevIndex) && s.charAt(sPrevIndex) == t.charAt(tIndex))
					{
						tmp = dl[rowBefore - tri] + cost;
						// transposition
						if (tmp < min)
							min = tmp;
					}
				}
				// substitution
				tmp = dl[rowBefore - 1] + cost;
				if (tmp < min)
					min = tmp;
				dl[dlIndex] = min;
				/* * /
				System.out.println("sPrevIndex=" + sPrevIndex + ", tPrevIndex=" + tPrevIndex + ", sIndex=" + sIndex + ", tIndex=" + tIndex);
				System.out.println("'" + s + "', '" + t + "'");
				for (int v = 0; v < lenT1; v++)
				{
					for (int w = 0; w < lenS1; w++)
						System.out.print(dl[v * lenS1 + w] + " ");
					System.out.println();
				}
				/**/
				tPrevIndex = tIndex;
			}
			sPrevIndex = sIndex;
		}
		return dl[dlIndex];
	}
	private static int damlevlim(String s, String t, int limit, int[] workspace)
	{
		int lenS = s.length();
		int lenT = t.length();
		if (lenS < lenT)
		{
			if (lenT - lenS >= limit)
				return limit;
		}
		else
			if (lenT < lenS)
				if (lenS - lenT >= limit)
					return limit;
		int lenS1 = lenS + 1;
		int lenT1 = lenT + 1;
		if (lenS1 == 1)
			return (lenT < limit)?lenT:limit;
		if (lenT1 == 1)
			return (lenS < limit)?lenS:limit;
		int[] dl = workspace;
		int dlIndex = 0;
		int sPrevIndex = 0, tPrevIndex = 0, rowBefore = 0, min = 0, tmp = 0, best = 0, cost = 0;
		int tri = lenS1 + 2;
		// start row with constant
		dlIndex = 0;
		for (tmp = 0; tmp < lenT1; tmp++)
		{
			dl[dlIndex] = tmp;
			dlIndex += lenS1;
		}
		for (int sIndex = 0; sIndex < lenS; sIndex++)
		{
			dlIndex = sIndex + 1;
			dl[dlIndex] = dlIndex; // start column with constant
			best = limit;
			for (int tIndex = 0; tIndex < lenT; tIndex++)
			{
				rowBefore = dlIndex;
				dlIndex += lenS1;
				//deletion
				min = dl[rowBefore] + 1;
				// insertion
				tmp = dl[dlIndex - 1] + 1;
				if (tmp < min)
					min = tmp;
				cost = 1;
				if (s.charAt(sIndex) == t.charAt(tIndex))
					cost = 0;
				if (sIndex > 0 && tIndex > 0)
				{
					if (s.charAt(sIndex) == t.charAt(tPrevIndex) && s.charAt(sPrevIndex) == t.charAt(tIndex))
					{
						tmp = dl[rowBefore - tri] + cost;
						// transposition
						if (tmp < min)
							min = tmp;
					}
				}
				// substitution
				tmp = dl[rowBefore - 1] + cost;
				if (tmp < min)
					min = tmp;
				dl[dlIndex] = min;
				if (min < best)
					best = min;
				/** /
				System.out.println("sPrevIndex=" + sPrevIndex + ", tPrevIndex=" + tPrevIndex + ", sIndex=" + sIndex + ", tIndex=" + tIndex);
				System.out.println("'" + s + "', '" + t + "'");
				for (int v = 0; v < lenT1; v++)
				{
					for (int w = 0; w < lenS1; w++)
						System.out.print(dl[v * lenS1 + w] + " ");
					System.out.println();
				}
				/**/
				tPrevIndex = tIndex;
			}
			if (best >= limit)
				return limit;
			sPrevIndex = sIndex;
		}
		if (dl[dlIndex] >= limit)
			return limit;
		else
			return dl[dlIndex];
	}
	//COPYCODE END
}


