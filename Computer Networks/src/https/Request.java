package https;

import java.io.File;
import java.util.Scanner;

import util.URL;


/**
 * This class creates instances of client-to-server requests
 * @author R0596433
 *
 */
public class Request {
	
	private URL 	url;
	private String 	command;
	private String	port 	 ="80";
	private String 	protocol ="HTTP/1.1";
	private String 	path;
	private String 	hostname;
	private String 	request;
	
	public Request(String[] args) {
		this.command = args[0];
		this.url = new URL(args[1]);
		this.path = this.url.getPath();
		this.hostname = this.url.getHostname();
		this.port = args[2];
		this.protocol = args[3];
		formatRequest();
	}
	
	public Request(String command, String path, String baseUri, String port) {
		this.command = command;
		this.path = path;
		this.hostname = baseUri;
		this.port = port;
		formatRequest();
	}
	
	public Request(String command, String url) {
		this.command = command;
		this.url = new URL(url);
		this.path = this.url.getPath();
		this.hostname = this.url.getHostname();
		formatRequest();
	}
	
	
	/**
	 * Format the request into a server-side readable form.
	 * Types of requests supported:
	 * 	- HEAD	- GET	- PUT	- POST
	 */
	private void formatRequest() {	
		if (command.equals("GET") || command.equals("HEAD")) {
			request = command + " " + path + " " + protocol + "\r\n" + 
					"Host: " + hostname + ":" + port + "\r\n";
		}
		
		else if (command.equals("PUT")) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Type your message (PUT):\n");
			String scanned = scanner.nextLine();
			scanner.close();
			
			int contentLength = scanned.length();
			
			request = command + " " + path + " " + protocol + "\r\n" +
					"Host: " + hostname + ":" + port + "\r\n" + 
					"Content-Type: text/txt" + "\r\n" + 
					"Content-Length: " + contentLength + "\r\n" +  "\r\n" +
					scanned;
		}
		else if (command.equals("POST")) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Type your message (POST):\n");
			String scanned = scanner.nextLine();
			scanner.close();
			int contentLength = scanned.length();
			
			request = command + " " + path + " " + protocol + "\r\n" +
					"Host: " + hostname + ":" + port + "\r\n" + 
					"Content-Type: text/txt" + "\r\n" + 
					"Content-Length: " + contentLength + "\r\n" + "\r\n" +
					scanned;
		}
	}

	public String getRequest() {
		return request;
	}

	public String getCommand() {
		return command;
	}

	public int getPort() {
		return Integer.parseInt(port);
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHostname() {
		return hostname;
	}
	
	public String getPath() {
		makeNecessaryDirs();
		return "C:/Users/Gebruiker/git/compnet/Computer Networks/saved" + path;
	}
	
	private void makeNecessaryDirs() {
		if (path.contains("%20")) {
			path = path.substring(0, path.indexOf("%20")) + " " + path.substring(path.indexOf("%20")+3);
		}
		File location = new File("C:/Users/Gebruiker/git/compnet/Computer Networks/saved" + path.substring(0, path.lastIndexOf("/")));
		if ( ! location.exists()) {
			location.mkdirs();
		}
	}
	
	
	/**
	 * Creates a clean filename. This is mainly used when saving files locally.
	 * @return
	 */
	public String getCleanFileName() {
		return path.substring(path.lastIndexOf("/")+1);
	}
}
