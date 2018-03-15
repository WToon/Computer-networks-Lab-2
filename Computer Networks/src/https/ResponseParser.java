package https;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class parses an HTTP response. If the response contains an html 
 * 	- text/html		- image/png		- image/jpeg
 * It stores the connection information and content of the respones locally in the 'saved' folder.
 * 
 * @author R0596433
 *
 */
public class ResponseParser {

	private final InputStream 	input;
	private Map<String, String> headers;
	private Request 			request;
	private ArrayList<Request> 	requests = new ArrayList<>();
	private ArrayList<Request>	remoteRequests = new ArrayList<>();


	/**
	 * HttpResponseParser
	 * @param input		The inputstream containing the server's response.
	 */
	public ResponseParser(InputStream input) {
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

		headers.put("Response-status", headersArray[0].split(" ")[1]);

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

		if (type.contains("text/html")) {
			Charset charset = StandardCharsets.UTF_8;

			if (type.contains("charset")) {
				String contentset = type.split("; charset=")[1];
				switch (contentset) {
				case "UTF-8": charset = StandardCharsets.UTF_8; break;
				case "ISO-8859-1": charset = StandardCharsets.ISO_8859_1; break;
				}
			}

			byte[] bhtml = new byte[Integer.parseInt(headers.get("Content-Length"))];
			input.read(bhtml, 0, Integer.parseInt(headers.get("Content-Length")));
			String html = new String(bhtml, charset);

			generateImageRequests(html);

			PrintWriter outputFile = new PrintWriter("saved/pages/" + request.getCleanFileName() + ".html");
			outputFile.write(html);
			outputFile.close();
		}

		else if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/gif")) {
			FileOutputStream out = new FileOutputStream("saved/images/" + request.getCleanFileName());
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
	 * @param html Html-string to parse.
	 */
	private void generateImageRequests(String html) {
		Document doc = Jsoup.parse(html, request.getHostname());
		Elements images = doc.select("img[src$=.png], img[src$=.jpg], img[src$=.gif]");
		for (Element image : images) {
			String src = image.attr("src");
			if (src.contains("www.") || src.contains("http")){
				Request s = new Request("GET", src);
				remoteRequests.add(s);
			}
			else {
				Request s = new Request("GET", "/" + src, request.getHostname());
				requests.add(s);
			}
		}
	}	


	/**
	 * 
	 * @return List of requests for embedded objects.
	 */
	public ArrayList<Request> getImageRequests() {
		return requests;
	}

	
	/**
	 * 
	 * @return List of requests for embedded objects which are not located on server we are currently connected to.
	 */
	public ArrayList<Request> getRemoteImageRequests() {
		return remoteRequests;
	}
}
