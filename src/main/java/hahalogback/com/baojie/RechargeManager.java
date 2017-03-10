package hahalogback.com.baojie;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class RechargeManager {

	private static final Logger log = LoggerFactory.getLogger(RechargeManager.class);

	private static RechargeManager manager = new RechargeManager();

	private static final ThreadLocalRandom RANDOM=ThreadLocalRandom.current();
	
	private AbstractApplicationContext applicationContext = null;

	private Notification notification;

	public void registerNotification(Notification notification) {
		this.notification = notification;
	}

	private RechargeManager() {
	}

	public static RechargeManager getInstance() {
		return manager;
	}

	public synchronized void startServer() {
		if (applicationContext != null) {
			throw new IllegalStateException("Server already started");
		}
		log.info("********* start server ***************");
		new Thread("recharge-server-0") {
			public void run() {
				applicationContext = new ClassPathXmlApplicationContext("classpath:hahaContext.xml");
				log.info("********* server starts successfully ***************");
				final Config config = applicationContext.getBean(Config.class);
				final int parkTime = config.getPrintMillis();
				final File logbackFile = new File("./resources/logback.xml");
				if (logbackFile.exists()) {
					LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					try {
						configurator.doConfigure(logbackFile);
					} catch (JoranException e) {
						e.printStackTrace(System.err);
						System.exit(-1);
					}
				}
				final HahaLogBack hahaLogBack = new HahaLogBack(config);
				int button=-1;
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					button=RANDOM.nextInt(4);
					if(button==0){
						hahaLogBack.printInfo(i);
					}else if(button==1) {
						hahaLogBack.printError(i);
					}else if (button==2) {
						hahaLogBack.printDebug(i);
					}else if (button==3) {
						hahaLogBack.printWarn(i);
					}
					LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(parkTime, TimeUnit.MILLISECONDS));
				}
			};
		}.start();

		addHook();
	}

	public synchronized void stop() {
		if (this.applicationContext != null) {
			applicationContext.destroy();
		}
	}

	public boolean isStarted() {
		return applicationContext != null;
	}

	private void addHook() {
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	private void destory() {
		if (applicationContext == null) {
			return;
		}
		applicationContext.destroy();
		applicationContext = null;
	}

	private void removeHook() {
		if (this.shutdownHook != null) {
			try {
				Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
			} catch (IllegalStateException ex) {
				// ignore - VM is already shutting down
			}
		}
	}

	private Thread shutdownHook = new Thread() {
		public void run() {
			try {
				destory();
				removeHook();
				if (notification != null) {
					notification.showdown();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	public static interface Notification {
		void showdown();
	}
}
