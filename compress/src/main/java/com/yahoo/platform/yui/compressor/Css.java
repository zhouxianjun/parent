package com.yahoo.platform.yui.compressor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * YUI CSS重写辅助类
 * @author Gary
 *
 */
public class Css {
	CssCompressor yuicss = null;

	public Css(Reader in) throws IOException {
		yuicss = new CssCompressor(in);
	}

	public void compress(Writer out, int linebreakpos) throws IOException{
		yuicss.compress(out, linebreakpos);
	}
	public String compress(String src, int linebreakpos) {
		String css = src;
		int startIndex = 0;
		int endIndex = 0;
		int i = 0;
		int max = 0;
		ArrayList preservedTokens = new ArrayList(0);
		ArrayList comments = new ArrayList(0);
		int totallen = css.length();
		css = yuicss.extractDataUrls(css, preservedTokens);
		StringBuffer sb = new StringBuffer(css);
		while ((startIndex = sb.indexOf("/*", startIndex)) >= 0) {
			endIndex = sb.indexOf("*/", startIndex + 2);
			if (endIndex < 0) {
				endIndex = totallen;
			}
			String token = sb.substring(startIndex + 2, endIndex);
			comments.add(token);
			sb.replace(
					startIndex + 2,
					endIndex,
					"___YUICSSMIN_PRESERVE_CANDIDATE_COMMENT_"
							+ (comments.size() - 1) + "___");
			startIndex += 2;
		}
		css = sb.toString();

		sb = new StringBuffer();
		Pattern p = Pattern
				.compile("(\"([^\\\\\"]|\\\\.|\\\\)*\")|('([^\\\\']|\\\\.|\\\\)*')");
		Matcher m = p.matcher(css);
		while (m.find()) {
			String token = m.group();
			char quote = token.charAt(0);
			token = token.substring(1, token.length() - 1);

			if (token.indexOf("___YUICSSMIN_PRESERVE_CANDIDATE_COMMENT_") >= 0) {
				i = 0;
				for (max = comments.size(); i < max; i++) {
					token = token.replace(
							"___YUICSSMIN_PRESERVE_CANDIDATE_COMMENT_" + i
									+ "___", comments.get(i).toString());
				}

			}

			token = token.replaceAll(
					"(?i)progid:DXImageTransform.Microsoft.Alpha\\(Opacity=",
					"alpha(opacity=");

			preservedTokens.add(token);
			String preserver = quote + "___YUICSSMIN_PRESERVED_TOKEN_"
					+ (preservedTokens.size() - 1) + "___" + quote;
			m.appendReplacement(sb, preserver);
		}
		m.appendTail(sb);
		css = sb.toString();

		i = 0;
		for (max = comments.size(); i < max; i++) {
			String token = comments.get(i).toString();
			String placeholder = "___YUICSSMIN_PRESERVE_CANDIDATE_COMMENT_" + i
					+ "___";

			if (token.startsWith("!")) {
				preservedTokens.add(token);
				css = css.replace(placeholder, "___YUICSSMIN_PRESERVED_TOKEN_"
						+ (preservedTokens.size() - 1) + "___");
			} else if (token.endsWith("\\")) {
				preservedTokens.add("\\");
				css = css.replace(placeholder, "___YUICSSMIN_PRESERVED_TOKEN_"
						+ (preservedTokens.size() - 1) + "___");
				i += 1;
				preservedTokens.add("");
				css = css.replace("___YUICSSMIN_PRESERVE_CANDIDATE_COMMENT_"
						+ i + "___", "___YUICSSMIN_PRESERVED_TOKEN_"
						+ (preservedTokens.size() - 1) + "___");
			} else {
				if (token.length() == 0) {
					startIndex = css.indexOf(placeholder);
					if ((startIndex > 2) && (css.charAt(startIndex - 3) == '>')) {
						preservedTokens.add("");
						css = css.replace(placeholder,
								"___YUICSSMIN_PRESERVED_TOKEN_"
										+ (preservedTokens.size() - 1) + "___");
					}

				}

				css = css.replace("/*" + placeholder + "*/", "");
			}

		}

		css = css.replaceAll("\\s+", " ");

		sb = new StringBuffer();
		p = Pattern.compile("(^|\\})(([^\\{:])+:)+([^\\{]*\\{)");
		m = p.matcher(css);
		while (m.find()) {
			String s = m.group();
			s = s.replaceAll(":", "___YUICSSMIN_PSEUDOCLASSCOLON___");
			s = s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
			m.appendReplacement(sb, s);
		}
		m.appendTail(sb);
		css = sb.toString();

		css = css.replaceAll("\\s+([!{};:>+\\(\\)\\],])", "$1");

		css = css.replaceAll("___YUICSSMIN_PSEUDOCLASSCOLON___", ":");

		css = css.replaceAll(":first\\-(line|letter)(\\{|,)", ":first-$1 $2");

		css = css.replaceAll("\\*/ ", "*/");

		css = css.replaceAll("^(.*)(@charset \"[^\"]*\";)", "$2$1");
		css = css.replaceAll("^(\\s*@charset [^;]+;\\s*)+", "$1");

		css = css.replaceAll("\\band\\(", "and (");

		css = css.replaceAll("([!{}:;>+\\(\\[,])\\s+", "$1");

		css = css.replaceAll(";+}", "}");

		css = css.replaceAll("([\\s:])(0)(px|em|%|in|cm|mm|pc|pt|ex)", "$1$2");

		css = css.replaceAll(":0 0 0 0(;|})", ":0$1");
		css = css.replaceAll(":0 0 0(;|})", ":0$1");
		css = css.replaceAll(":0 0(;|})", ":0$1");

		sb = new StringBuffer();
		p = Pattern
				.compile("(?i)(background-position|transform-origin|webkit-transform-origin|moz-transform-origin|o-transform-origin|ms-transform-origin):0(;|})");
		m = p.matcher(css);
		while (m.find()) {
			m.appendReplacement(sb,
					m.group(1).toLowerCase() + ":0 0" + m.group(2));
		}
		m.appendTail(sb);
		css = sb.toString();

		css = css.replaceAll("(:|\\s)0+\\.(\\d+)", "$1.$2");

		p = Pattern.compile("rgb\\s*\\(\\s*([0-9,\\s]+)\\s*\\)");
		m = p.matcher(css);
		sb = new StringBuffer();
		while (m.find()) {
			String[] rgbcolors = m.group(1).split(",");
			StringBuffer hexcolor = new StringBuffer("#");
			for (i = 0; i < rgbcolors.length; i++) {
				int val = Integer.parseInt(rgbcolors[i]);
				if (val < 16) {
					hexcolor.append("0");
				}
				hexcolor.append(Integer.toHexString(val));
			}
			m.appendReplacement(sb, hexcolor.toString());
		}
		m.appendTail(sb);
		css = sb.toString();

		p = Pattern
				.compile("(\\=\\s*?[\"']?)?#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])(:?\\}|[^0-9a-fA-F{][^{]*?\\})");

		m = p.matcher(css);
		sb = new StringBuffer();
		int index = 0;

		while (m.find(index)) {
			sb.append(css.substring(index, m.start()));

			boolean isFilter = (m.group(1) != null) && (!"".equals(m.group(1)));

			if (isFilter) {
				sb.append(m.group(1) + "#" + m.group(2) + m.group(3)
						+ m.group(4) + m.group(5) + m.group(6) + m.group(7));
			} else if ((m.group(2).equalsIgnoreCase(m.group(3)))
					&& (m.group(4).equalsIgnoreCase(m.group(5)))
					&& (m.group(6).equalsIgnoreCase(m.group(7)))) {
				sb.append("#"
						+ new StringBuffer().append(m.group(3))
								.append(m.group(5)).append(m.group(7))
								.toString().toLowerCase());
			} else {
				sb.append("#"
						+ new StringBuffer().append(m.group(2))
								.append(m.group(3)).append(m.group(4))
								.append(m.group(5)).append(m.group(6))
								.append(m.group(7)).toString().toLowerCase());
			}

			index = m.end(7);
		}

		sb.append(css.substring(index));
		css = sb.toString();

		sb = new StringBuffer();
		p = Pattern
				.compile("(?i)(border|border-top|border-right|border-bottom|border-right|outline|background):none(;|})");
		m = p.matcher(css);
		while (m.find()) {
			m.appendReplacement(sb,
					m.group(1).toLowerCase() + ":0" + m.group(2));
		}
		m.appendTail(sb);
		css = sb.toString();

		css = css.replaceAll(
				"(?i)progid:DXImageTransform.Microsoft.Alpha\\(Opacity=",
				"alpha(opacity=");

		css = css.replaceAll("[^\\}\\{/;]+\\{\\}", "");

		if (linebreakpos >= 0) {
			i = 0;
			int linestartpos = 0;
			sb = new StringBuffer(css);
			while (i < sb.length()) {
				char c = sb.charAt(i++);
				if ((c == '}') && (i - linestartpos > linebreakpos)) {
					sb.insert(i, '\n');
					linestartpos = i;
				}
			}

			css = sb.toString();
		}

		css = css.replaceAll(";;+", ";");

		i = 0;
		for (max = preservedTokens.size(); i < max; i++) {
			css = css.replace("___YUICSSMIN_PRESERVED_TOKEN_" + i + "___",
					preservedTokens.get(i).toString());
		}

		css = css.trim();
		return css;
	}
}
