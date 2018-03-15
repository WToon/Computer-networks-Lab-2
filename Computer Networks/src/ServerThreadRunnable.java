import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import https.RequestParser;

/**
 * Represents a single thread of the multithreaded server.
 * Each thread will handle the requests of a single client.
 * @author R0596433
 * @version 1.0
 */
public class ServerThreadRunnable implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText   = null;
    
    public ServerThreadRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            RequestParser parser = new RequestParser(input);
            parser.parse();
            
            
            Path path = Paths.get("C:\\Users\\Gebruiker\\git\\compnet\\Computer Networks\\server\\webpage.html");
            
            output.write(("HTTP/1.1 200 OK\n\r"
            		+ "WorkerRunnable: " + this.serverText + "\n\r"
            		+ "Content-Length: "+ Files.readAllBytes(path).length + "\n\r"
            		+ "Content-Type: text/html\r\r"+ Files.readAllLines(path) + "").getBytes());
            output.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}