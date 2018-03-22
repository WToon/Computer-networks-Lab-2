package https;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Response {
	
	
	//TODO use correct format
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    
    private String responseHeaders;
    private byte[] responseContent;
    
    private String date = sdf.format(new Date(System.currentTimeMillis()));
    
    public Response(String statusCode, String fileLocation) throws IOException {
    	
    	responseContent = Files.readAllBytes(Paths.get("C://Users//Gebruiker/git/compnet/Computer Networks/server/webpage.html"));
    	
    	String contentType = "text/html";
    	responseHeaders = "HTTP/1.1 " + statusCode + " OK" + "\r\n" + 
    			"WorkerRunnable: " + Thread.currentThread() + 
    			"Content-Type: " + contentType + "\r\n" + 
    			"Date: " + date + "\r\n" + 
    			"Content-Length: " + responseContent.length + "\n\n";
    }
    
    
    
    
	public String getResponseHeaders() {
		return responseHeaders;
	}
	
	public byte[] getResponseBody() {
		return responseContent;
	}

}
