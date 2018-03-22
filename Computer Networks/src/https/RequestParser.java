package https;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;

/**
 * SERVERSIDE PARSER
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
		} catch (Exception e) {
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

		// Read the inputstream up until the headers.
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
	    
	    // Map all headers.
	    String[] headersArray = sb.toString().split("\r\n");
	    Map<String, String> headers = new HashMap<>();
	    for (int i = 1; i < headersArray.length - 1; i++) {
	        headers.put(headersArray[i].split(": ")[0],
	                headersArray[i].split(": ")[1]);
	    }	    
	    headers.put("Request-Type", headersArray[0].split(" ")[0]);
	    headers.put("Content-File",headersArray[0].split(" ")[1]);
	    this.headers = headers;

	    // Check if request content is included.
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
	 * @throws ParseException 
	 */
	private void handleRequest() throws IOException, ParseException {
		
		// Verify validity of the request (statuscode 404-Bad request)
		if (! headers.containsKey("Host")) {
			response = new Response("400", null, null);
			return;	
		}

		String fileLocation = headers.get("Content-File");
		
		Boolean ims = false;
		long time = 0;
		
		if (headers.containsKey("If-Modified-Since")) {
			String _date_ = headers.get("If-Modified-Since");
			DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			Date date = format.parse(_date_);
			time = date.getTime();
			ims = true;
		}



		switch (headers.get("Request-Type")) {
		
		case "GET":	
			if (fileLocation.equals("/")) {
				fileLocation = "server/webpage.html";
			}

			File f = new File(fileLocation);
			if (ims){
				if (f.lastModified() > time) {
					response = new Response("304", fileLocation, null);
				}
			} else if (f.exists()) {
				response = new Response("200", fileLocation, "GET");
			} else {
				response = new Response("404", null, null);
			}
		break;

		case "HEAD": 
			if (fileLocation.equals("/")) {
				fileLocation = "server/webpage.html";
			}
			
			File g = new File(fileLocation);
			
			if (g.exists()) {
				response = new Response("200", fileLocation, "HEAD");
			} else {
				response = new Response("404", null, null);
			}
			break;
			
		case "PUT": 
			try{
				handlePRequests(StandardOpenOption.WRITE);
			} catch (IOException e) {
				response = new Response("500", null, null);
				break;
			}
			response = new Response("200", fileLocation, "PUT");
			break;
			
		case "POST": 
			try {
				handlePRequests(StandardOpenOption.APPEND);	
			} catch (IOException e) {
				response = new Response("500", null, null);
				return;
			}
			response = new Response("200", fileLocation, "POST");
			break;
		}
	}
	
	/**
	 * Handle put and post requests.
	 * @param action PUTrequest -> write; POSTrequest -> append;
	 * @throws IOException
	 */
	private void handlePRequests(StandardOpenOption action) throws IOException {
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
	
	public Response getResponse() {
		return this.response;
	}
}
