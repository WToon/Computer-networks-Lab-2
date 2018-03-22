import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import https.RequestParser;


/**
 * Represents a thread of the multithreaded server.
 * Each client is given a personal thread.
 * @author R0596433
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
        	InputStream input  = clientSocket.getInputStream();
        	OutputStream output = clientSocket.getOutputStream();
        	while (true) {

            	// Parse the received request and generate an appropriate response.
            	if (input.available() > 0) {
                	RequestParser parser = new RequestParser(input);
                	parser.parse();
                	byte[] empty = new byte[input.available()];
                	input.read(empty, 0, empty.length);
                	
                	// Send the response to the client
                	output.write((parser.getResponse().getResponseHeaders()).getBytes());
                	if (parser.getResponse().getResponseBody() != null) {
                		output.write(parser.getResponse().getResponseBody());            			
                	}
            	}
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}