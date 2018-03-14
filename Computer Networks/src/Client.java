import java.net.*;
import java.util.ArrayList;
import java.io.*;

import https.*;

public class Client {
	
	private static ArrayList<Request> requests = new ArrayList<>();	
	
	private static void sendInitialRequest(Request request) {
		System.out.println(request.getRequest());
		try (Socket socket = new Socket(request.getHostname(), request.getPort())){

			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(request.getRequest());
			
			HttpFactory factory = new HttpFactory(request, input);
			requests = factory.getRequests();
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
		
		sendInitialRequest(new Request(args));

		for (Request request: requests) {
			System.out.println(request.getRequest());
			try (Socket socket = new Socket(request.getHostname(), request.getPort())){

				InputStream input = socket.getInputStream();
				OutputStream output = socket.getOutputStream();
				
				PrintWriter writer = new PrintWriter(output, true);
				writer.println(request.getRequest());
	            
				HttpFactory factory = new HttpFactory(request, input);
				
				// factory.getHeader().getContent();
				
				socket.close();
	            
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

