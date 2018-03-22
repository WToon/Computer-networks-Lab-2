package https;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Response {

	private String responseHeaders;
	private byte[] responseContent;

	String date = getServerTime();

	/**
	 * Generate the appropriate response.
	 * @param statusCode	The statuscode of the response.
	 * @param fileLocation	The fileLocation of a needed resource.
	 * @param requestType	The type of the received request.
	 * @throws IOException
	 */
	public Response(String statusCode, String fileLocation, String requestType) throws IOException {

		if (statusCode.equals("404")) {
			responseContent = Files.readAllBytes(Paths.get("C://Users//Gebruiker/git/compnet/Computer Networks/server/response/NotFound.html"));
			responseHeaders = "HTTP/1.1 404 Not Found" + "\r\n" + 
					"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
					"Date: " + date + "\r\n" + 
					"Content-Type: " + "text/html" + "\r\n" + 
					"Content-Length: " + responseContent.length + "\r\n" + "\r\n";

		} else if (statusCode.equals("400")) {
			responseContent = Files.readAllBytes(Paths.get("C://Users//Gebruiker/git/compnet/Computer Networks/server/response/BadRequest.html"));
			responseHeaders = "HTTP/1.1 400 Bad Request" + "\r\n" + 
					"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
					"Date: " + date + "\r\n" + 
					"Content-Type: " + "text/html" + "\r\n" + 
					"Content-Length: " + responseContent.length + "\r\n" + "\r\n";

		} else if (statusCode.equals("304")) {
			byte[] content = Files.readAllBytes(Paths.get(fileLocation));
			responseHeaders = "HTTP/1.1 400 Bad Request" + "\r\n" + 
					"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
					"Date: " + date + "\r\n" + 
					"Content-Type: " + "text/html" + "\r\n" + 
					"Content-Length: " + content.length + "\r\n" + "\r\n";
			
		} else if (statusCode.equals("200")) {
			if (requestType.equals("GET")) {
				responseContent = Files.readAllBytes(Paths.get(fileLocation));
				responseHeaders = "HTTP/1.1 200 Ok" + "\r\n" + 
						"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
						"Date: " + date + "\r\n" + 
						"Content-Type: " + "text/html" + "\r\n" + 
						"Content-Length: " + responseContent.length + "\r\n" + "\r\n";

			} else if (requestType.equals("HEAD")) {
				byte[] content = Files.readAllBytes(Paths.get(fileLocation));
				responseHeaders = "HTTP/1.1 200 Ok" + "\r\n" + 
						"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
						"Date: " + date + "\r\n" + 
						"Content-Type: " + "text/html" + "\r\n" + 
						"Content-Length: " + content.length + "\r\n" + "\r\n";

			} else if (requestType.equals("PUT")) {
				byte[] content = Files.readAllBytes(Paths.get(fileLocation));
				responseHeaders = "HTTP/1.1 200 Ok" + "\r\n" + 
						"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
						"Date: " + date + "\r\n" + 
						"Content-Type: " + "text/html" + "\r\n" + 
						"Content-Length: " + content.length + "\r\n" + "\r\n";    			

			} else if (requestType.equals("POST")) {
				byte[] content = Files.readAllBytes(Paths.get(fileLocation));
				responseHeaders = "HTTP/1.1 200 Ok" + "\r\n" + 
						"WorkerRunnable: " + Thread.currentThread() + "\r\n" +
						"Date: " + date + "\r\n" +     
						"Content-Type: " + "text/html" + "\r\n" + 
						"Content-Length: " + content.length + "\r\n" + "\r\n";
			}
		}
	}

	
	/**
	 * Method returning the current time.
	 * @return Current time formatted correctly.
	 */
	private String getServerTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(calendar.getTime());
	}

	public String getResponseHeaders() {
		return responseHeaders;
	}

	public byte[] getResponseBody() {
		return responseContent;
	}

}
