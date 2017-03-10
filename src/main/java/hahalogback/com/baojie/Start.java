package hahalogback.com.baojie;

import java.io.IOException;

public class Start {

	public static void main(String[] args) throws IOException {
		RechargeManager serverManager = RechargeManager.getInstance();
		serverManager.registerNotification(new RechargeManager.Notification() {
			@Override
			public void showdown() {
				System.out.println("server stoped");
			}
		});
		serverManager.startServer();
	}
}
