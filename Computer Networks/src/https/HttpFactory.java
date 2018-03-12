package https;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class HttpFactory {
	
	private InputStream input;
	private Header header;
	private Body body;
	private BufferedReader reader;
	private Request request;
		
	public HttpFactory(Request request, InputStream input) {
		this.input = input;
		this.reader = new BufferedReader(new InputStreamReader(input));
		
		this.header = new Header();
		
		this.request = request;
		
		try {
			parseHeaders();
			parseBody();
		} catch(Exception e) {
			System.out.println("In Factory - Parsing Error");
		}		
	}
	
	
	private void parseHeaders() throws IOException {
		PrintWriter outputFile = new PrintWriter("saved/headers/" + request.getCleanFileName()+"-headers");
		StringBuilder headerContent = new StringBuilder();
		
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) {
				break;
			}
			else if (line.contains("Content-Length: ")) {
				header.setContentLength(Integer.parseInt(line.substring(16)));
			}
			else if (line.contains("Content-Type: ")) {
				header.setContentType(line.substring(14));
			}
			headerContent.append(line + "\n");
			outputFile.println(line);
		}
		header.setHeaderContent(headerContent.toString());
		outputFile.close();
	}
	
	
	private void parseBody() throws IOException {
		// HTML
		if (header.getContentType().equals("text/html")) {
			this.body = new Body("html", request.getHostname());
			StringBuilder bodyContent = new StringBuilder();
			PrintWriter outputFile = new PrintWriter("saved/pages/" + request.getHostname() +".html");
			String line;
			while ((line = reader.readLine()) != null) {
				bodyContent.append(line);
				bodyContent.append("\n");
				outputFile.println(line);

				if (line.equals("</HTML>")) {
					break;
				}
			}
			body.setText(bodyContent.toString());
			outputFile.close();
		}
		// JPEG
		else if (header.getContentType().equals("image/jpeg")) { 
			this.body = new Body("jpg", request.getHostname());
			readAndSaveImage();
		}
		// PNG
		else if (header.getContentType().equals("image/png")) {
			this.body = new Body("png", request.getHostname());
			readAndSaveImage();
		}
	}
	
	// TODO doesn't work
	public void readAndSaveImage() throws IOException {
		System.out.println("saving");
		FileOutputStream out = new FileOutputStream("saved/images/" + request.getPath());
		
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = input.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	 
	    buffer.flush();
	    byte[] image = buffer.toByteArray();
		out.write(image);
		out.close();
	}

	
	public Header getHeader() {
		return header;
	}

	public Body getBody() {
		return body;
	}
	
	
		

}
