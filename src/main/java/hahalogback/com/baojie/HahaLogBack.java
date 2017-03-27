package hahalogback.com.baojie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hahalogback.com.baojie.errorline.YunErrorLine;

public class HahaLogBack {
	private static final Logger log = LoggerFactory.getLogger(HahaLogBack.class);

	private Config config;

	public HahaLogBack(final Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void printInfo(final int num) {
		final String printForward = config.getWhatToPrintFoeward();
		final String printTail = config.getWhatToPrintTail();
		final String line=YunErrorLine.getCallTrace("hahalogback.com", new NullPointerException("info"));
		log.info(printForward + "-" +line+ num + "-" + printTail + ".");
	}
	public void printWarn(final int num) {
		final String printForward = config.getWhatToPrintFoeward();
		final String printTail = config.getWhatToPrintTail();
		final String line=YunErrorLine.getCallTrace("hahalogback.com", new NullPointerException("warn"));
		log.warn(printForward + "-" +line+ num + "-" + printTail + ".");
	}
	public void printError(final int num) {
		final String printForward = config.getWhatToPrintFoeward();
		final String printTail = config.getWhatToPrintTail();
		final String line=YunErrorLine.getCallTrace("hahalogback.com", new NullPointerException("error"));
		log.error(printForward + "-" +line+ num + "-" + printTail + ".");
	}
	public void printDebug(final int num) {
		final String printForward = config.getWhatToPrintFoeward();
		final String printTail = config.getWhatToPrintTail();
		final String line=YunErrorLine.getCallTrace("hahalogback.com", new NullPointerException("debug"));
		log.debug(printForward + "-" +line+ num + "-" + printTail + ".");
	}

}
