package https;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
	
	private final InputStream input;
	private Boolean contentAssociated = false;
	
	public RequestParser(InputStream input) {
		this.input = input;
	}
	
	public void parse() {
		try {
			parseRequestHeaders();
			if (contentAssociated) {
				parseRequestBody();
			}
			generateResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method parses the request for 'command', 'path', 'host', 'protocol', etc...
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
	}
	
	/**
	 * 
	 */
	private void parseRequestBody() {
		
	}
	
	/**
	 * 
	 */
	private Response generateResponse() {
		return new Response();
	}
}
