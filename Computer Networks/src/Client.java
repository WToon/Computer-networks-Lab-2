import java.net.*;
import java.util.ArrayList;
import java.io.*;

import https.*;

/**
 * Represents a client.
 * @author R0596433
 * @version 1.2
 */
public class Client {

	private static ArrayList<Request> requests = new ArrayList<>();	
	private static ArrayList<Request> remoteRequests = new ArrayList<>();
	/**
	 * This method requests, processes and saves a web page and the associated embedded objects.
	 * All requests are handled over the same connection.
	 * TODO Move this method into an HttpCommands
	 * @param request The initial webpage-request. (See run configurations)
	 */
	private static void sendRequest(Request request) {
		try (Socket socket = new Socket(request.getHostname(), request.getPort())){

			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			PrintWriter writer = new PrintWriter(output, true);
			writer.println(request.getRequest());

			ResponseParser parser = new ResponseParser(input);
			parser.parse(request);

			for (Request nR: parser.getImageRequests()) {
				requests.add(nR);
			}
			for (Request nR: parser.getRemoteImageRequests()){
				remoteRequests.add(nR);
			}

			for (Request r: requests) {
				writer.println(r.getRequest());
				parser.parse(r);
			}

			requests.clear();

			socket.close();

		} catch (UnknownHostException e) {
			System.out.println("UnknowHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

	/**
	 * Main method for client
	 * @param args The initial client request. (See run configurations)
	 */
	public static void main(String[] args) {
		sendRequest(new Request(args));
		for (Request r: remoteRequests) {
			sendRequest(r);
		}
		//sendRequest(new Request(new String[] {"PUT", "localhost", "9000", "HTTP/1.1"}));
	}
}

