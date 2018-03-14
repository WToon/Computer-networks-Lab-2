package https;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
 * It stores the connection information and content of the respones locally in the 'saved' folder.
 * 
 * @author R0596433
 *
 */
public class Parser {
	
	private InputStream input;
	private Map<String, String> headers;
	private Request request;
	private ArrayList<Request> requests = new ArrayList<>();
	
	
	/**
	 * Create a parser.
	 * @param input		The inputstream containing the server's response.
	 */
	public Parser(InputStream input) {
		this.input = input;
	}
	
	
	/**
	 * Parse the response to the request.
	 * 
	 * @param request	The sent request.
	 */
	public void parse(Request request) {
		this.request = request;
		try {
			parseHTTPHeaders();
			parseHTTPBody();
		} catch(Exception e) {
			System.out.println("Parsing Error");
		}
		
	}
	
	
	/**
	 * Parse the HTTP-Response's headers. The gathered information is kept in 'headers'.
	 * 
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

	    String[] headersArray = sb.toString().split("\r\n");
	    Map<String, String> headers = new HashMap<>();
	    for (int i = 1; i < headersArray.length - 1; i++) {
	        headers.put(headersArray[i].split(": ")[0],
	                headersArray[i].split(": ")[1]);
	    }
	    
	    PrintWriter outputFile = new PrintWriter("saved/headers/" + request.getCleanFileName() + "-header");
	    String[] fileArray = sb.toString().split("\n");
	    for (String i : fileArray) {
	    	outputFile.write(i);
	    	System.out.print(i);
	    }
	    outputFile.close();
	    this.headers = headers;
	}
	
	
	/**
	 * Parse the HTTPBody taking into account it's body content-type, (e.g. html - image...), 
	 * and it's body content-length. These parameters are found at the headers.
	 * 
	 * @throws IOException
	 */
	private void parseHTTPBody() throws IOException {
		
		String type = headers.get("Content-Type");
		
		if (type.equals("text/html")) {
		    PrintWriter outputFile = new PrintWriter("saved/pages/" + request.getCleanFileName() + ".html");
		    byte[] bhtml = new byte[Integer.parseInt(headers.get("Content-Length"))];
		    input.read(bhtml, 0, Integer.parseInt(headers.get("Content-Length")));
		    String html = new String(bhtml, StandardCharsets.UTF_8);
		    System.out.println(html);
		    outputFile.write(html);
		    outputFile.close();
		    generateImageRequests(html);
		}
		
		else if (type.equals("image/png") || type.equals("image/jpeg")) {
			FileOutputStream out = new FileOutputStream("saved/images/" + request.getPath());
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		    byte[] data = new byte[Integer.parseInt(headers.get("Content-Length"))];
		    while (buffer.size() != Integer.parseInt(headers.get("Content-Length"))) {
		    	buffer.write(data, 0, input.read(data, 0, data.length));
		    }
		    buffer.close();
		    byte[] image = buffer.toByteArray();
			out.write(image);
			out.close();
		}
	}

	
	/**
	 * Generate a 'GET' request for each image found in the html-string.
	 * The generated requests are saved in 'requests'.
	 * 
	 * @param html
	 */
	private void generateImageRequests(String html) {
		Document doc = Jsoup.parse(html, request.getHostname());
		Elements images = doc.select("img[src$=.png], img[src$=.jpg]");
		for (Element image : images) {
			requests.add(new Request("GET", "/" + image.attr("src"), request.getHostname()));
		}
	}	
		
	
	/**
	 * 
	 * @return List of requests for embedded objects.
	 */
	public ArrayList<Request> getImageRequests() {
		return requests;
	}

}
