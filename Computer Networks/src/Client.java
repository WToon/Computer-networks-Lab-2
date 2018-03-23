import java.net.*;
import java.util.ArrayList;
import java.io.*;

import https.*;

/**
 * Represents a client.
 * @author R0596433
 */
public class Client {

	private static ArrayList<Request> requests = new ArrayList<>();	
	private static ArrayList<Request> remoteRequests = new ArrayList<>();
	
	
	/**
	 * Processes the given request.
	 * Additional requests are handled over the same connection if possible.
	 * @param request The request to be handled.
	 */
	private static void sendRequest(Request request) {
		System.out.println(request.getRequest());
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
				System.out.println(r.getRequest());
				System.out.println();
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
	 * Main method for client. Different cases show different functionality.
	 * @param args The client request. (See run configurations)
	 */
	public static void main(String[] args) {
		
		String[] demonstrate = new String[] {"Head request","Get request","Put request","Post request","Run config","NotFound request","Bad request"};
		String demo = demonstrate[2];
		
		switch (demo) {
		case "NotFound request":sendRequest(new Request("GET", "orange", "localhost", "9000")); 					break;
		case "Run config": 		sendRequest(new Request(args)); 													break;
		case "Put request": 	sendRequest(new Request("PUT", "server/chat/newfile.txt", "localhost", "9000")); 	break;
		case "Post request": 	sendRequest(new Request("POST", "server/chat/newfile.txt", "localhost", "9000"));	break;
		case "Get request": 	sendRequest(new Request("GET", "/", "localhost", "9000")); 							break;
		case "Head request": 	sendRequest(new Request("HEAD", "/", "localhost", "9000")); 						break;
		}

		for (Request r: remoteRequests) {
			sendRequest(r);	
		}
	}
}

