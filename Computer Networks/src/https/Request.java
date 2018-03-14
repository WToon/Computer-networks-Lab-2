package https;

import util.URL;


/**
 * This class creates instances of client-to-server requests
 * @author R0596433
 *
 */
public class Request {
	
	private String command, port="80", protocol="HTTP/1.1", path, hostname;
	private URL url;
	private String request;
	
	public Request(String[] args) {
		this.command = args[0];
		this.url = new URL(args[1]);
		this.path = this.url.getPath();
		this.hostname = this.url.getHostname();
		this.port = args[2];
		this.protocol = args[3];
		formatRequest();
	}
	
	public Request(String command, String path, String baseUri) {
		this.command = command;
		this.path = path;
		this.hostname = baseUri;
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
	 */
	private void formatRequest() {
		request = command + " " + path + " " + protocol + "\r\n" +
				  "Host: " + hostname + ":" + port + "\r\n";
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
		return path;
	}
	
	
	/**
	 * Creates a clean filename. This is mainly used when saving files locally.
	 * @return
	 */
	public String getCleanFileName() {
		if (path.equals("/"))
			return (hostname.substring(hostname.indexOf(".")+1, hostname.lastIndexOf(".")));
		else
			return(path.substring(1,path.indexOf(".")));
	}
}
