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
		this.body = new Body();
		
		this.request = request;
		
		try {
			parseHeaders();
			parseBody();
			
		} catch(Exception e) {
			System.out.println("In Factory - Parsing Error");
		}		
	}
	
	private void parseHeaders() throws IOException {
		PrintWriter outputFile = new PrintWriter("saved/headers");
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
		if (header.getContentType().equals("text/html")) {
			StringBuilder bodyContent = new StringBuilder();
			PrintWriter outputFile = new PrintWriter("saved/" + request.getCleanFileName() + ".html");
			String line; Boolean html = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals("<HTML>")) {
					html = true;
				}
				if (html) {
					bodyContent.append(line);
					bodyContent.append("\n");
					outputFile.println(line);
				}
				if (line.equals("</HTML>")) {
					break;
				}
			}
			body.setText(bodyContent.toString());
			outputFile.close();
		}
		else if (header.getContentType().equals("image/jpg") || header.getContentType().equals("image/png")) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[header.getContentLength()];
			int n = 0;
			while (-1!=(n=input.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			byte[] image = out.toByteArray();
			
			body.setImage(image);
			
			FileOutputStream fos = new FileOutputStream("saved/" + request.getPath());
			fos.write(image);
			fos.close();
		}
	}

	public Header getHeader() {
		return header;
	}

	public Body getBody() {
		return body;
	}
	
	
		

}
