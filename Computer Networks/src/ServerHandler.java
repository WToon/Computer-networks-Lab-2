/**
 * Handles the server.
 * @author R0596433
 * @version 1.0
 */
public class ServerHandler {

	public static void main(String[] args) {
		Server server = new Server(9000);
		new Thread(server).start();
		
		try {
		    Thread.sleep(20 * 100000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}