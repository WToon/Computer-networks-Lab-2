import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import https.RequestParser;


/**
 * Represents a single thread of the multithreaded server.
 * Each thread will handle the requests of a single client.
 * @author R0596433
 * @version 1.0
 */
public class ServerThreadRunnable implements Runnable {

    protected Socket clientSocket = null;
    
    public ServerThreadRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    /**
     * Execute the thread, handling the request
     */
    public void run() {
        try {
        	while (true) {
            	InputStream input  = clientSocket.getInputStream();
            	OutputStream output = clientSocket.getOutputStream();

            	// Parse the received request and generate an appropriate response.
            	if (input.available() > 0) {
                	RequestParser parser = new RequestParser(input);
                	parser.parse();
                	
                	// Send the response to the client
                	output.write((parser.getResponse().getResponseHeaders()).getBytes());
                	output.write(parser.getResponse().getResponseBody());            		
            	}
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}