package com.jiangge.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 字符串处理类
 */
@SuppressWarnings("all")
public class StringUtil {
	private static final Logger logger = Logger.getLogger(StringUtil.class);
	
	/**
	 * MD5加密处理
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			/** 获得MD5摘要算法的 MessageDigest 对象 **/
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			/** 使用指定的字节更新摘要 **/
			mdInst.update(btInput);
			/** 获得密文 **/
			byte[] md = mdInst.digest();
			/** 把密文转换成十六进制的字符串形式 **/
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取指定小数位的浮点数,不够小数位数时补零
	 * 
	 * @param floStr
	 * @return
	 */
	public static String paseFloat(String floStr, int location) {
		if (floStr == null)
			return "";
		int index = floStr.indexOf(".");
		// 如果没有小数点.则加一个小数点.
		if (index == -1) {
			floStr = floStr + ".";
		}
		index = floStr.indexOf(".");
		int leave = floStr.length() - index;
		// 如果小于指定位数则在后面补零
		for (; leave <= location; leave++) {
			floStr = floStr + "0";
		}
		return floStr.substring(0, index + location + 1);
	}

	/**
	 * 
	 * @param intStr
	 * @return
	 */
	public static int truncateInt(String intStr) {
		int len = intStr.length();

		int result = 0;
		for (int i = 0; i < len; i++) {
			char c = intStr.charAt(i);
			if (c >= '0' && c <= '9') {
				result = result * 10;
				result += c - '0';
			} else {
				break;
			}
		}

		return result;
	}

	/**
	 * 把字符型数字转换成整型.
	 * 
	 * @param str
	 *            字符型数字
	 * @return int 返回整型值。如果不能转换则返回默认值defaultValue.
	 */
	public static int getInt(String str, int defaultValue) {
		if (str == null)
			return defaultValue;
		if (isInt(str)) {
			return Integer.parseInt(str);
		} else {
			return defaultValue;
		}
	}

	/** 取整数，默认值-1 */
	public static int getInt(String str) {
		return getInt(str, -1);
	}

	/**
	 * 判断一个字符串是否为Int类型
	 */
	public static boolean isInt(String str) {

		if (isNum(str)) {
			if (str.length() < 10) {
				return true;
			} else {
				try {
					Integer.parseInt(str);
					return true;
				} catch (Exception e) {
				}
			}

		}
		return false;
	}

	/**
	 * 判断一个字符串是否空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/** 判断指定的字符串是否是空串 */
	public static boolean isBlank(String str) {
		if (isEmpty(str))
			return true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/** 针对字符串为NULL的处理 */
	public static String notNull(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 判断2个字符串是否相等
	 */
	public static boolean isequals(String str1, String str2) {
		return str1.equals(str2);
	}

	/**
	 * 在长数字前补零
	 * 
	 * @param num
	 *            数字
	 * @param length
	 *            输出位数
	 */
	public static String addzero(long num, int length) {
		String str = "";
		if (num < Math.pow(10, length - 1)) {
			for (int i = 0; i < (length - (num + "").length()); i++) {
				str += "0";
			}
		}
		str = str + num;
		return str;
	}

	/**
	 * 在数字前补零
	 * 
	 * @param num
	 *            数字
	 * @param length
	 *            输出位数
	 */
	public static String addzero(int num, int length) {
		String str = "";
		if (num < Math.pow(10, length - 1)) {
			for (int i = 0; i < (length - (num + "").length()); i++) {
				str += "0";
			}
		}
		str = str + num;
		return str;
	}

	/**
	 * 使HTML的标签失去作用*
	 * 
	 * @param input
	 *            被操作的字符串
	 * @return String
	 */
	public static final String escapeHTMLTagOld(String input) {
		if (input == null)
			return "";
		input = input.trim().replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("\r\n", "<br>");
		input = input.replaceAll("'", "&#39;");
		input = input.replaceAll("\"", "&quot;");
		input = input.replaceAll("\\\\", "&#92;");
		return input;

	}

	/**
	 * 对表单提交的数据进行处理，有专门针对textarea的数据进行处理的逻辑,对空格进行保留. 如果不是textarea提交的数据(根据是否有"<br>
	 * "和"&nbsp;"进行判断)
	 * 
	 * @param input
	 */
	public static final String escapeHTMLTag(String input) {
		if (input == null)
			return "";

		if (input.indexOf("<br>") == -1 && input.indexOf("&nbsp;") == -1) {
			return escapeHTMLTagOld(input);
		}

		int len = input.length();
		StringBuilder strBuilder = new StringBuilder();
		int pos = 0;
		while (pos < len) {
			char c = input.charAt(pos);

			switch (c) {
			case '<': {
				if ((pos + 4) < len
						&& "<br>".equals(input.substring(pos, pos + 4))) {
					strBuilder.append("<br>");
					pos += 4;

				} else {
					strBuilder.append("&lt;");
					pos++;
				}
				break;
			}
			case '>':
				strBuilder.append("&gt;");
				pos++;
				break;
			case '&': {
				if ((pos + 6) < len
						&& "&nbsp;".equals(input.substring(pos, pos + 6))) {
					strBuilder.append("&nbsp;");
					pos += 6;

				} else {
					strBuilder.append("&amp;");
					pos++;
				}
				break;

			}
			case '\'':
				strBuilder.append("&#39;");
				pos++;
				break;
			case '\"':
				strBuilder.append("&quot;");
				pos++;
				break;
			case '\\':
				strBuilder.append("&#92;");
				pos++;
				break;
			default:
				strBuilder.append(c);
				pos++;
				break;
			}

		}

		return strBuilder.toString();

	}

	/**
	 * 还原html标签
	 * 
	 * @param input
	 * @return
	 */
	public static final String unEscapeHTMLTag(String input) {
		if (input == null)
			return "";
		input = input.trim().replaceAll("&amp;", "&");
		input = input.replaceAll("&lt;", "<");
		input = input.replaceAll("&gt;", ">");
		input = input.replaceAll("<br>", "\n");
		input = input.replaceAll("&#39;", "'");
		input = input.replaceAll("&quot;", "\"");
		input = input.replaceAll("&#92;", "\\\\");
		return input;
	}

	/**
	 * 把数组合成字符串
	 * 
	 * @param str
	 * @param seperator
	 * @return
	 */
	public static String toString(String[] str, String seperator) {
		if (str == null || str.length == 0)
			return "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0, n = str.length; i < n; i++) {
			if (i != 0)
				buf.append(seperator);
			buf.append(str[i]);
		}
		return buf.toString();
	}

	/**
	 * 把字符串分隔成数组
	 * 
	 * @param str
	 *            字符 如： 1/2/3/4/5
	 * @param seperator
	 *            分隔符号 如: /
	 * @return String[] 字符串树组 如: {1,2,3,4,5}
	 */
	public static String[] split(String str, String seperator) {
		StringTokenizer token = new StringTokenizer(str, seperator);
		int count = token.countTokens();
		String[] ret = new String[count];
		for (int i = 0; i < count; i++) {
			ret[i] = token.nextToken();
		}
		return ret;
	}

	/**
	 * 按指定的分隔符分隔数据，有N个分隔符则返回一个N+1的数组
	 * 
	 * @param str
	 * @param seperator
	 * @return
	 */
	public static String[] splitHaveEmpty(String str, String seperator) {
		// 分隔符前后增加一个空白字符
		str = str.replaceAll(seperator, " " + seperator + " ");
		return str.split(seperator);
	}

	/**
	 * @param len
	 *            需要显示的长度(<font color="red">注意：长度是以byte为单位的，一个汉字是2个byte</font>)
	 * @param symbol
	 *            用于表示省略的信息的字符，如“...”,“>>>”等。
	 * @return 返回处理后的字符串
	 */
	public static String getSub(String str, int len, String symbol) {
		if (str == null)
			return "";
		try {
			int counterOfDoubleByte = 0;
			byte[] b = str.getBytes("gbk");
			if (b.length <= len)
				return str;
			for (int i = 0; i < len; i++) {
				if (b[i] < 0)
					counterOfDoubleByte++; // 通过判断字符的类型来进行截取
			}
			if (counterOfDoubleByte % 2 == 0)
				str = new String(b, 0, len, "gbk") + symbol;
			else
				str = new String(b, 0, len - 1, "gbk") + symbol;
		} catch (UnsupportedEncodingException e) {
			logger.error(e, e);
		}
		return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	/**
	 * 按字节获取字符串的长度,一个汉字占二个字节
	 * 
	 * @param str
	 * @return
	 */
	public static int getLen(String str) {
		try {
			byte[] b = str.getBytes("gbk");
			return b.length;
		} catch (UnsupportedEncodingException e) {
			logger.error(e, e);
		}
		return 0;
	}

	public static String getSub(String str, int len) {
		return getSub(str, len, "");
	}

	/** 只取某一字符串的前几个字符，后面以...表示 */
	public static String getAbc(String str, int len) {
		return getAbc(str, len, "...");
	}

	/** 截取多少长度前的一断字符串 */
	public static String getAbc(String str, int len, String symbol) {
		if (str == null)
			return null;
		if (len < 0)
			return "";
		if (str.length() <= len) {
			return str;
		} else {
			return str.substring(0, len).concat(symbol);
		}
	}

	/**
	 * 截取某字符串中两个字符串之间的一段字符串 eg:StringUtil.subBetween("yabczyabcz", "y", "z") =
	 * "abc"
	 */
	public static String subBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * 截取某字符串中最后出现指定字符串之后的一段字符串 StringUtil.subAfterLast("abcba", "b") = "a"
	 */
	public static String subAfterLast(String str, String separator) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (separator == null || separator.length() == 0) {
			return "";
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == (str.length() - separator.length())) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取某字符串中最后出现指定字符串之前的一段字符串 StringUtil.subBeforeLast("abcba", "b") = "abc"
	 */
	public static String subBeforeLast(String str, String separator) {
		if (str == null || separator == null || str.length() == 0
				|| separator.length() == 0) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取某字符串中指定字符串之后的一段字符串 StringUtil.subAfter("abcba", "b") = "cba"
	 */
	public static String subAfter(String str, String separator) {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (separator == null) {
			return "";
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取某字符串中指定字符串之前的一段字符串 StringUtil.subBefore("abcbd", "b") = "a"
	 */
	public static String subBefore(String str, String separator) {
		if (str == null || separator == null || str.length() == 0) {
			return str;
		}
		if (separator.length() == 0) {
			return "";
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/** 判断两个字符串中是否含有相同的元素 */
	public static boolean containsNone(String str, String invalidChars) {
		if (str == null || invalidChars == null) {
			return true;
		}
		return containsNone(str, invalidChars.toCharArray());
	}

	/** 判断字符串中是否含有字符数组中的元素 */
	public static boolean containsNone(String str, char[] invalidChars) {
		if (str == null || invalidChars == null) {
			return true;
		}
		int strSize = str.length();
		int validSize = invalidChars.length;
		for (int i = 0; i < strSize; i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < validSize; j++) {
				if (invalidChars[j] == ch) {
					return false;
				}
			}
		}
		return true;
	}

	/** 判断字符串中是否包含指定字符串 */
	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return (str.indexOf(searchStr) >= 0);
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return boolean
	 */
	static java.util.regex.Pattern emailer;

	public static boolean isEmail(String email) {
		if (emailer == null) {
			String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			emailer = java.util.regex.Pattern.compile(check);
		}
		java.util.regex.Matcher matcher = emailer.matcher(email);
		return matcher.matches();
	}

	/**
	 * 转换html特殊字符为html码
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlSpecialChars(String str) {
		try {
			if (str.trim() == null) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
			char ch = ' ';
			for (int i = 0; i < str.length(); i++) {
				ch = str.charAt(i);
				if (ch == '&') {
					sb.append("&amp;");
				} else if (ch == '<') {
					sb.append("&lt;");
				} else if (ch == '>') {
					sb.append("&gt;");
				} else if (ch == '"') {
					sb.append("&quot;");
				} else if (ch == '\'') {
					sb.append("&#039;");
				} else if (ch == '(') {
					sb.append("&#040;");
				} else if (ch == ')') {
					sb.append("&#041;");
				} else if (ch == '@') {
					sb.append("&#064;");
				} else {
					sb.append(ch);
				}
			}
			if (sb.toString().replaceAll("&nbsp;", "").replaceAll("　", "")
					.trim().length() == 0) {
				return "";
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * @param word
	 *            : 输入的字符串
	 * @return 是否输入的是字符
	 */
	public boolean CharIsLetter(String word) {
		boolean sign = true; // 初始化标志为为'true'
		for (int i = 0; i < word.length(); i++) { // 遍历输入字符串的每一个字符
			if (!Character.isLetter(word.charAt(i))) { // 判断该字符是否为英文字符
				sign = false; // 若有一位不是英文字符，则将标志位修改为'false'
			}
		}
		return sign;// 返回标志位结果
	}

	/**
	 * 生成小标题的正则表达式替换,mark是大标题的镭点标识 小标题的锚点标识按mark加出现的序号的方式生成，如第一个出现的小标题为：
	 * mark1,第个出现的为mark2，如此类推。 小标题示例:
	 * <ol>
	 * <li><span class="menuId">3.1</span><a href="#">历史著名运动员</a></li>
	 * <li><span class="menuId">3.2</span><a href="#">2004年奥运会著名运动员</a></li>
	 * <li><span class="menuId">3.3</span><a href="#">其他运动员</a></li>
	 * <li><span class="menuId">3.4</span><a href="#">其他著名人物</a></li>
	 * </ol>
	 * 返回一个字符串数组，序号1为解析后的数据，序号2为小题标数据
	 * 
	 * @param input
	 *            需要解析的原始数据
	 * @param mark
	 *            大标题的锚点标识
	 * @param bigProIndex
	 *            大标题的索引序号
	 * @return
	 */
	public static String[] findSpecData(String input, String mark,
			String bigProIndex) {
		// 用来存放处理解析后的文本数据
		StringBuffer sb = new StringBuffer();
		// 用来生成小项的锚点
		StringBuffer smallPro = new StringBuffer("<ol>").append("\n");
		// 用来存放小项的标号
		int index = 1;

		String regex = "(<div class=s_title>)(.*?)(</div>)";
		Matcher testM = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
				.matcher(input);
		while (testM.find()) {
			testM.appendReplacement(sb, "<div class=\"s_title\"><a name=\""
					+ mark + index + "\"></a>$2$3");
			// 小标题名称
			String smallName = testM.group(2);
			smallPro.append("<li><span class=\"menuId\" >").append(bigProIndex)
					.append(".").append(index).append("</span><a href=\"#")
					.append(mark).append(index).append("\">").append(smallName)
					.append("</a></li>").append("\n");
			// 索引号自加
			index++;
		}
		// 如果存在小标题，
		if (index != 1) {
			// 组装小标题
			smallPro.append("</ol>");
			// 生成带小标题锚点的数据
			testM.appendTail(sb);
			return new String[] { sb.toString(), smallPro.toString() };
		}
		return null;
	}

	/**
	 * 字符串截取
	 * 
	 * @param str
	 *            要处理的字符串
	 * @param beginIndex
	 *            开始位置
	 * @param endIndex
	 *            结束位置
	 * @return
	 */
	public String substr(String str, int beginIndex, int endIndex) {
		if (isBlank(str)) {
			return "";
		}
		if (endIndex == -1) {
			return str.substring(beginIndex);
		}

		if (endIndex > str.length()) {
			endIndex = str.length();
		}
		return str.substring(beginIndex, endIndex);
	}

	/**
	 * 随机生成几个不同的数
	 * 
	 * @param lenth
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static int[] random5(int lenth, int num) {

		Random rd = new Random();

		HashSet set = new HashSet();
		while (true) {
			int i = rd.nextInt(lenth);
			set.add(new Integer(i));
			if (set.size() == num) {
				break;
			}
		}
		Iterator iter = set.iterator();
		int jj[] = new int[num];
		int i = 0;
		while (iter.hasNext()) {

			jj[i] = ((Integer) iter.next()).intValue();
			++i;
		}
		return jj;
	}

	/**
	 * 字符串截取
	 * 
	 * @param str
	 *            要处理的字符串
	 * @param beginIndex
	 *            开始位置
	 * @param endIndex
	 *            结束位置
	 * @param endMark
	 *            在结束处加...符
	 * @return
	 */
	public String substr(String str, int beginIndex, int endIndex,
			String endMark) {
		if (isBlank(str)) {
			return "";
		}
		if (endIndex == -1) {
			return str.substring(beginIndex);
		}

		if (endIndex > str.length()) {
			endIndex = str.length();
		}
		String restr = str.substring(beginIndex, endIndex);
		if (endIndex < str.length()) {
			restr = restr + endMark;
		}
		return restr;
	}

	/**
	 * 去掉超链接和
	 * <P>
	 * </P>
	 * ,文章摘要使用
	 * 
	 * @param str
	 * @return
	 */
	public static String filtHref(String str) {
		if (str == null)
			return "";
		String regex = "<[a|A] href=\".*?>(.*?)</[a|A]>";
		java.util.regex.Pattern pattern = null;
		pattern = java.util.regex.Pattern.compile(regex);
		java.util.regex.Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String ss = matcher.group(1);
			str = str.replaceAll("<[a|A] href=\".*?>" + ss + "</[a|A]>", ss);
		}

		regex = "<[p|P] [^>]*?>(.*?)</[p|P]>";
		pattern = java.util.regex.Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			String ss = matcher.group(1);
			str = str.replaceAll("<[p|P] [^>]*?>" + ss + "</[p|P]>", ss);
		}
		return str;
	}

	/**
	 * 给超链接加上:target="_blank"
	 * 
	 * @param str
	 * @return
	 */
	public static String addHrefBlank(String str) {
		if (str == null)
			return "";
		String regex = "<[a|A] href=\"([^>]*?)>.*?</[a|A]>";
		java.util.regex.Pattern pattern = null;
		pattern = java.util.regex.Pattern.compile(regex);
		java.util.regex.Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String ss = matcher.group(1);
			if (ss.indexOf("_blank") == -1) {
				str = str.replaceAll(ss, ss + "  target=\"_blank\"");
			}
		}
		return str;
	}

	// 获取26个字母
	public static char[] getEnChar() {
		char[] cs = new char[26];
		char c = 'A' - 1;
		for (int i = 0; c++ < 'Z'; i++) {
			cs[i] = c;
		}
		return cs;
	}

	// 判断是否在26个字母里面
	public static boolean isInChar(String c) {
		boolean in = false;
		char[] ch = getEnChar();
		for (int i = 0; i < ch.length; i++) {
			if (c.equals(ch[i] + "")) {
				in = true;
				break;
			}
		}
		return in;
	}

	// 转化成大写
	public String toUpperCase(String str) {
		if (isBlank(str)) {
			return "";
		}
		return str.toUpperCase();
	}

	// 转化成大写
	public String toLowerCase(String str) {
		if (isBlank(str)) {
			return "";
		}
		return str.toLowerCase();
	}

	/**
	 * 根据大图获得小图地址
	 * 
	 * @param imgurl
	 * @return
	 */
	public static String getSmallImg(String imgurl) {
		int len = imgurl.lastIndexOf("/");
		if (len > 1)
			return imgurl.substring(0, len + 1) + "t_"
					+ imgurl.substring(len + 1, imgurl.length());
		else
			return imgurl;
	}

	// /**
	// * 去掉html代码
	// * @param html
	// * @return
	// */
	// public static String trimHtml(String html){
	// Parser parser = Parser.createParser(html,"GBK");
	// if(parser!=null){
	// StringBean sb = new StringBean();
	// try {
	// parser.visitAllNodesWith(sb);
	// html = sb.getStrings();
	// } catch (Exception e) {
	// logger.error(e, e);
	// }
	// }
	// return html;
	// }
	/**
	 * 把字符串切成每个字符
	 * 
	 * @param str
	 * @return
	 */
	public static char[] toArray(String str) {
		return str.toCharArray();
	}

	/**
	 * b代替a
	 * 
	 * @param str
	 * @param a
	 * @param b
	 * @return
	 */
	public static String replaceStr(String str, String a, String b) {
		if (isBlank(str)) {
			return "";
		}
		return str.replaceAll(a, b);
	}

	/**
	 * 获取要过滤的URL
	 */
	public static boolean getURLFromFile(String str, String filename) {
		String text;
		String out = "";
		File file = new File(filename); // 读文件
		if (file.exists()) {
			try {
				long filesize = file.length();
				if (filesize == 0)
					return true;
				BufferedReader input = new BufferedReader(new FileReader(file));
				StringBuffer buffer = new StringBuffer();

				while ((text = input.readLine()) != null) {
					buffer.append(text);
				}
				out = buffer.toString();
				return filterURL(out, str);
			} catch (FileNotFoundException e) {
				logger.error(e, e);
			} catch (IOException e) {
				logger.error(e, e);
			}
		} else {
			logger.error("File is not exist");
			return true;
		}
		return false;
	}

	/**
	 * Description: 基本功能：过滤URL
	 * 
	 * @param str
	 * @return
	 */
	public static boolean filterURL(String str, String regexpURL) {
		Pattern pattern = Pattern.compile(regexpURL);
		Matcher matcher = pattern.matcher(str);
		boolean result = matcher.find();
		return result;
	}

	/**
	 * 判断字符串是否正常(不为null，不为空)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFine(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否一个数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		if (str == null)
			return false;
		return str.matches("\\d+");
	}

	/**
	 * 获得字符串去掉空格、回车、tab之后的长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getLenWithoutBlank(String str) {
		return str.replace("\n", "").replace("\t", "").replace(" ", "")
				.replace("　", "").length();
	}

	public static String parserToWord(String str) {
		String result = "";
		if (str != null) {
			result = str.replaceAll("&lt;br&gt;", "&#10;");
			result = result.replaceAll("<br>", "&#10;");
			result = result.replaceAll(" ", "&ensp;");
			result = result.replaceAll("\r", "");
			result = result.replaceAll("\n", "");
		}
		return result;
	}

	public static String parserToHTMLForTextArea(String str) {
		String result = escapeHTMLTag(str);
		result = result.replaceAll(" ", "&ensp;");
		return result;
	}

	/**
	 * 把double由科学计数法转为正常计数法
	 * 
	 * @param str
	 * @return
	 */
	public static String doubleToString(double dnum) {
		NumberFormat numformat = NumberFormat.getNumberInstance();
		numformat.setGroupingUsed(false);
		numformat.setMinimumFractionDigits(2);
		numformat.setMaximumFractionDigits(2);
		String valueN = numformat.format(dnum);
		return valueN;
	}

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 格式化两位小数
	 * @param old
	 * @return
	 */
	public static String formatTwoPoint(String old){
		String newstr = "";
		if(StringUtils.isNotEmpty(old)){
			double d = Double.parseDouble(old);
			newstr = String .format("%.2f",d);
		}
		return newstr;
	}
	
	/**
	 * 格式化四位小数
	 * @param old
	 * @return
	 */
	public static String formatFourPoint(String old){
		String newstr = "";
		if(StringUtils.isNotEmpty(old)){
			double d = Double.parseDouble(old);
			d = d*100;
			newstr = String .format("%.2f",d);
		}
		return newstr;
	}

	public static void main(String[] args) {
		double d = 10.249130249023438;
		String result = String .format("%.2f",d);
		System.out.println(result);
	}
}
