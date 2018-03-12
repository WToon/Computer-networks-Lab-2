package https;

import util.URL;

public class Request {
	
	private String command, port, protocol, path, hostname;
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
	
	public Request(String command, String path, String url) {
		this.command = command;

		this.port = "80";
		this.protocol = "HTTP/1.1";
		formatRequest();
	}
	
	private void formatRequest() {
		request = command + " " + path + " " + protocol + "\n" +
				  "Host: " + hostname + ":" + port + "\n";
	}

	public String getRequest() {
		System.out.println(request);
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

	public URL getUrl() {
		return url;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getCleanFileName() {
		return hostname.substring(hostname.indexOf(".")+1, hostname.lastIndexOf("."));
	}
}
