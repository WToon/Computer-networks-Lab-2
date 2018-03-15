package util;

/**
 * The URL class implements a URL parser and formatter.
 * 
 * @author R0596433
 *	
 */
public class URL {
	
	private String URL;
	private String hostname;
	private String path;
	private String filename;

	public URL(String url) {
		// TODO Check URL validity better
		try {
			if (url.equals("localhost")) {
				hostname = "localhost";
				path = "/";
				filename = "localhost";
			} else {
				parseUrl(url);
			}
		} catch(Exception e) {
			System.out.println("Error parsing url");
			URL = null;
		}
	}
	
	/**
	 * Example: 	URL 		= https://www.example.com/path_1/path_2
	 * 				hostname 	= www.example.com
	 * 				path 		= /path_1/path_2
	 * 				filename 	= example
	 */
	private void parseUrl(String url) {
		// defaults
		this.URL = url;
		this.path = "/";
		this.hostname = url;

		
		// Prune possible "https://www." extension
		if (url.lastIndexOf(".") != url.indexOf(".")){
			url = url.substring(url.indexOf(".")+1);
		}
		
		// Split hostname and path
		int index = url.indexOf("/");
		if (index != -1) {
			path = url.substring(index);
			hostname= url.substring(0, index);
		}
		
		filename = hostname.substring(0, hostname.lastIndexOf("."));
	}

	public String getUrl() {
		return URL;
	}

	public String getHostname() {
		return hostname;
	}

	public String getPath() {
		return path;
	}
	
	public String getFilename() {
		return filename;
	}
}
