package hahalogback.com.baojie;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config implements InitializingBean, DisposableBean {

	private static final int normalmMills = 1000;
	// private static final String printWhat="nothing";

	@Value("${config.printmillis}")
	private Integer printMillis;
	@Value("${config.pwforward}")
	private String whatToPrintFoeward;

	@Value("${config.pwtail}")
	private String whatToPrintTail;

	@Value("${config.debug}")
	private String debug;
	@Value("${config.info}")
	private String info;
	@Value("${config.warn}")
	private String warn;
	@Value("${config.error}")
	private String error;
	
	
	@PostConstruct
	public void check() {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (null == printMillis) {
			throw new NullPointerException();
		}
		if (0 == printMillis) {
			printMillis = normalmMills;
		}
		if (null == whatToPrintFoeward) {
			throw new NullPointerException();
		}
		if (null == whatToPrintTail) {
			throw new NullPointerException();
		}
	}

	public Integer getPrintMillis() {
		return printMillis;
	}

	public void setPrintMillis(Integer printMillis) {
		this.printMillis = printMillis;
	}


	public static int getNormalmmills() {
		return normalmMills;
	}

	public String getWhatToPrintFoeward() {
		return whatToPrintFoeward;
	}

	public void setWhatToPrintFoeward(String whatToPrintFoeward) {
		this.whatToPrintFoeward = whatToPrintFoeward;
	}

	public String getWhatToPrintTail() {
		return whatToPrintTail;
	}

	public void setWhatToPrintTail(String whatToPrintTail) {
		this.whatToPrintTail = whatToPrintTail;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@PreDestroy
	private void destoryMe() {

	}

	@Override
	public void destroy() throws Exception {
		if (null != whatToPrintFoeward) {
			whatToPrintFoeward = null;
		}
		if (null != whatToPrintTail) {
			whatToPrintTail = null;
		}
	}

}
