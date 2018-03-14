package https;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class parses an HTTP response. It supports several content types such as:
 * 	- text/html		- image/png		- image/jpeg
 * It stores the connection information and content of the respones locally in 'saved'.
 * @author R0596433
 *
 */
public class HttpFactory {
	
	private InputStream input;
	private Map<String, String> headers;
	private Request request;
	private ArrayList<Request> requests = new ArrayList<>();
	
	/**
	 * Create a HttpFactoryObject.
	 * @param request
	 * @param input
	 */
	public HttpFactory(Request request, InputStream input) {
		this.input = input;
		this.request = request;
		try {
			parseHTTPHeaders();
			parseHTTPBody();
		} catch(Exception e) {
			System.out.println("In Factory - Parsing Error");
		}
	}
	
	/**
	 * Parse the HTTP-Response's headers. 
	 * Headers are mapped into this.headers.
	 * @throws IOException
	 */
	private void parseHTTPHeaders() throws IOException {
		
	    int charRead;
	    StringBuffer sb = new StringBuffer();
	    while (true) {
	        sb.append((char) (charRead = input.read()));
	        if ((char) charRead == '\r') {           		// if we've got a '\r'
	            sb.append((char) input.read()); 			// 	then write '\n'
	            charRead = input.read();        			// 	read the next char;
	            if (charRead == '\r') {                  	// if it's another '\r'
	                sb.append((char) input.read());			// 	write the '\n'
	                break;
	            } else {
	                sb.append((char) charRead);
	            }
	        }
	    }

	    //TODO map(connection status response - needed for serverside)
	    String[] headersArray = sb.toString().split("\r\n");
	    Map<String, String> headers = new HashMap<>();
	    for (int i = 1; i < headersArray.length - 1; i++) {
	        headers.put(headersArray[i].split(": ")[0],
	                headersArray[i].split(": ")[1]);
	    }
	    
	    PrintWriter outputFile = new PrintWriter("saved/headers/" + request.getCleanFileName()+"-headers");
	    String[] fileArray = sb.toString().split("\n");
	    for (String i : fileArray) {
	    	outputFile.write(i);
	    	System.out.print(i);
	    }
	    outputFile.close();
	    this.headers = headers;
	}
	
	/**
	 * Parse the HTTPBody taking into account it's body content-type.
	 * @throws IOException
	 */
	private void parseHTTPBody() throws IOException {
		String type = headers.get("Content-Type");
		
		if (type.equals("text/html")) {
		    int charRead = 0;
		    StringBuffer sb = new StringBuffer();
		    PrintWriter outputFile = new PrintWriter("saved/pages/" + request.getCleanFileName() + ".html");
		    while (charRead != -1) {
		        sb.append((char) (charRead = input.read()));
		        if ((char) charRead == '\r') {           		// if we've got a '\r'
		            sb.append((char) input.read()); 			// 	then write '\n'
		            charRead = input.read();        			// 	read the next char;
		            if (charRead == '\r') {                  	// if it's another '\r'
		                sb.append((char) input.read());			// 	write the '\n'
		                break;
		            } else {
		                sb.append((char) charRead);
		            }
		        }
		    }
	        String[] body = sb.toString().split("\n");
		    for (int i = 1; i < body.length - 1; i++) {
		    	outputFile.println(body[i]);
		    }
		    outputFile.close();
		    generateImageRequests(sb.toString());
		}
		
		else if (type.equals("image/png") || type.equals("image/jpeg")) {
			readImageBody();
		}
	}

	/**
	 * Generate a 'GET' request for each image found in the html-string.
	 * The generated requests are located in this.requests.
	 * @param html
	 */
	private void generateImageRequests(String html) {
		Document doc = Jsoup.parse(html, request.getHostname());
		Elements images = doc.select("img[src$=.png], img[src$=.jpg]");
		for (Element image : images) {
			System.out.println(image.attr("src"));
			requests.add(new Request("GET", "/" + image.attr("src"), request.getHostname()));
		}
	}	
	
	
	/**
	 * Interpret and save the bodycontent as an image.
	 * @throws IOException
	 */
	private void readImageBody() throws IOException {
		
		FileOutputStream out = new FileOutputStream("saved/images/" + request.getPath());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = input.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	        System.out.println(buffer.size());
	    }
	 
	    buffer.flush();
	    byte[] image = buffer.toByteArray();
		out.write(image);
		out.close();
	}
	
	/**
	 * 
	 * @return List of requests for embedded objects.
	 */
	public ArrayList<Request> getRequests() {
		return requests;
	}

}
