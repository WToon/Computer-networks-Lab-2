import java.net.*;

import java.io.*;

import https.*;

public class Client {
	
	private static void sendRequest(Request request, OutputStream output) {
		PrintWriter writer = new PrintWriter(output, true);
		writer.println(request.getRequest());
	}

	public static void main(String[] args) {
		
		Request request = new Request(args);
		
		// Connect
		try (Socket socket = new Socket(request.getHostname(), request.getPort())){

			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			
            sendRequest(request, output);
            
			HttpFactory factory = new HttpFactory(request, input);

			factory.getHeader().getContent();
			factory.getBody().getContent();
            
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
