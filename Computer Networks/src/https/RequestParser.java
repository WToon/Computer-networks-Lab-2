package https;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * SERVERSIDE PARSER!
 * This parser interprets requests and generates appriopriate responses.
 * @author R0596433
 */
public class RequestParser {
	
	private final InputStream 	input;
	private Boolean 			contentAssociated = false;
	private Map<String, String> headers;
	private byte[]				content;
	private Response 			response;
	
	
	public RequestParser(InputStream input) {
		this.input = input;
	}
	
	/**
	 * Parse the request and handle it appropriatly.
	 */
	public void parse() {
		try {
			parseRequestHeaders();
			if (contentAssociated) {
				parseRequestBody();
			}
			handleRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * This method parses the request for 'command', 'path', 'host', 'protocol', etc...
	 * It fills the instance map 'headers' with following key-value pairs:
	 * 	- ["Request-Type": 'HEAD-GET-PUT-POST' ; "Host": 'hostname' ; "Content-Le...]
	 * @throws IOException
	 */
	private void parseRequestHeaders() throws IOException {

	    int charRead;
	    StringBuffer sb = new StringBuffer();
	    while (true) {
	        sb.append((char) (charRead = input.read()));
	        if ((char) charRead == '\r') {
	            sb.append((char) input.read());
	            charRead = input.read();
	            if (charRead == '\r') {
	                sb.append((char) input.read());
	                break;
	            } else {
	                sb.append((char) charRead);
	            }
	        }
	    }
	    
	    String[] headersArray = sb.toString().split("\r\n");
	    Map<String, String> headers = new HashMap<>();
	    for (int i = 1; i < headersArray.length - 1; i++) {
	        headers.put(headersArray[i].split(": ")[0],
	                headersArray[i].split(": ")[1]);
	    }
	    
	    headers.put("Request-Type", headersArray[0].split(" ")[0]);
	    headers.put("Content-File",headersArray[0].split(" ")[1]);
	    this.headers = headers;
	    
	    if (headers.get("Request-Type").equals("PUT") || headers.get("Request-Type").equals("POST")) {
	    	contentAssociated = true;
	    }
	}
	
	/**
	 * Read the body associated with the request.
	 * @throws IOException 
	 */
	private void parseRequestBody() throws IOException {
		content = new byte[Integer.parseInt(headers.get("Content-Length"))];
		input.read(content, 0, Integer.parseInt(headers.get("Content-Length")));
	}
	
	
	/**
	 * Handle requests and generate appropriate responses.
	 * @throws IOException 
	 */
	private void handleRequest() throws IOException {
		
		Boolean fileUpdate = false;
		StandardOpenOption action = null;
		
		// Differentiate between request types.
		switch (headers.get("Request-Type")) {
		case "GET":
			String fileLocation = headers.get("Content-File");
			if (fileLocation.equals("/")) {
				fileLocation = "C://Users//Gebruiker/git/compnet/Computer Networks/server/webpage.html";
			}
			
			File f = new File(fileLocation);
			
			if (f.exists()) {
				response = new Response("200", fileLocation);
			} else {
				System.out.println("file not found 404");
				System.exit(0);
			}
			
			break;
		case "HEAD": break;
		
		case "PUT": 
			action = StandardOpenOption.WRITE;
			fileUpdate = true;
			break;
		
		case "POST": 
			action = StandardOpenOption.APPEND; 
			fileUpdate = true; 
			break;
		}
		
		// Handle put and post writing.
		if (fileUpdate) {
			while (true) {
				try {
					System.out.println(headers.get("Content-File"));
					Files.write(Paths.get(headers.get("Content-File")), content, action);
					break;
				} catch (Exception e) {
					File f = new File(headers.get("Content-File"));
					f.createNewFile();
				}
			}
		}
	}
	
	public Response getResponse() {
		return this.response;
	}
}
