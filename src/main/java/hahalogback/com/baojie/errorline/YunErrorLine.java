package hahalogback.com.baojie.errorline;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YunErrorLine {

	private static final Logger log = LoggerFactory.getLogger(YunErrorLine.class);

	public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

	private static final String FOR_WORD = "\tat ";

	private static final List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));

	private static final String IP;

	private static final String PLAM_NAME="test-plam";
	
	static {
		IP=YunGetLinuxIP.getIps();
	}
	
	public static String getCallTrace(final String basePackge, final Throwable throwable) {
		final List<String> stackList = getStackFrames(throwable);
		final int listSize = stackList.size();
		StringBuilder callTraceString = new StringBuilder(32);
		String tempString = null;
		if (0 == listSize) {
			return "null";
		} else {
			callTraceString.append(IP);
			callTraceString.append("\t");
			callTraceString.append(PLAM_NAME);
			callTraceString.append("\t");
			for (int i = 0; i < listSize; i++) {
				tempString = stackList.get(i).trim();
				if (null != tempString) {
					if (i == 0) {
						callTraceString.append(tempString);
					} else {
						if (tempString.startsWith(basePackge)) {
							callTraceString.append(" <-- ");
							callTraceString.append(tempString);
						} else {
							continue;
						}
					}
				} else {
					continue;
				}
			}
			return callTraceString.toString();
		}
	}
	
	
	public static List<String> getStackFrames(final Throwable throwable) {
		if (null == throwable) {
			return EMPTY_LIST;
		} else {
			return getStackFrames(getStackTrace(throwable));
		}
	}

	public static String getStackTrace(final Throwable throwable) {
		StringWriter sw = null;
		PrintWriter pw = null;
		String errorLine = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw, true);
			throwable.printStackTrace(pw);
			errorLine = sw.getBuffer().toString();
		} catch (Throwable t) {
			errorLine = "";
			log.error(t.toString());
		} finally {
			smoothlyClose(sw, pw);
		}
		return errorLine;
	}

	private static List<String> getStackFrames(final String stackTrace) {
		final String linebreak = LINE_SEPARATOR;
		final StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
		final List<String> list = new ArrayList<String>();
		String innerString = null;
		while (frames.hasMoreTokens()) {
			innerString = handlerChars(frames.nextToken());
			list.add(innerString);
		}
		return list;
	}

	private static String handlerChars(final String string) {
		if (!string.startsWith(FOR_WORD)) {
			return string;
		} else {
			final int ssd = FOR_WORD.length();
			final int idx = string.indexOf(FOR_WORD);
			final String subStr = string.substring(idx + ssd);
			return subStr;
		}
	}

	@SuppressWarnings("restriction")
	private static String getSystemProperty(final String property) {
		try {
			return System.getProperty(property);
		} catch (final SecurityException ex) {
			log.error("Caught a SecurityException reading the system property '" + property
					+ "'; the SystemUtils property value will default to null.");
			return java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction(property));
		}
	}

	private static void smoothlyClose(final StringWriter stringWriter, final PrintWriter printWriter) {
		if (null != stringWriter) {
			try {
				stringWriter.close();
			} catch (IOException e) {
				assert true;
			}
		}
		if (null != printWriter) {
			printWriter.close();
		}
	}

}
