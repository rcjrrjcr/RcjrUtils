package com.rcjrrjcr.bukkitplugins.util;

import java.util.regex.*;

public class RegexHelper {

	static Pattern rawRgx = Pattern.compile("^RGX=");
	static Pattern asterisk = Pattern.compile("\\**");
	static Pattern qMark = Pattern.compile("\\?*");
	
	public static String parseWildcard(String wildcard)
	{
		if(rawRgx.matcher(wildcard).lookingAt()) return wildcard.substring(4);
		Matcher m = asterisk.matcher(wildcard);
		String proc = m.replaceAll(".*");
		m = qMark.matcher(proc);
		proc = "$" + m.replaceAll(".");
		return proc;
	}
}
