import java.net.*;
import java.util.ArrayList;
import java.io.*;

import https.*;

public class Client {

	private static ArrayList<Request> requests = new ArrayList<>();	

	/**
	 * This method requests, processes and saves a web page and the associated embedded objects.
	 * All requests are handled over the same connection.
	 * TODO Implement support for embedded objects on remote servers.
	 * @param request The initial webpage-request.
	 */
	private static void requestWebPage(Request request) {
		try (Socket socket = new Socket(request.getHostname(), request.getPort())){
			
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			PrintWriter writer = new PrintWriter(output, true);
			writer.println(request.getRequest());

			HttpParser parser = new HttpParser(input);
			parser.parse(request);

			for (Request nR: parser.getRequests()) {
				requests.add(nR);
			}				
			
			for (Request r: requests) {
				writer.println(r.getRequest());
				parser.parse(r);
				System.out.println();
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

	public static void main(String[] args) {
		requestWebPage(new Request(args));
	}
}

