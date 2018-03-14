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
			if (!(url.substring(0,8).equals("https://")) & !(url.substring(0, 4).equals("www."))) {
				System.out.println("Invalid url");
			} else {
				parseUrl(url);
			}
		} catch(Exception e) {
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

		
		// Prune "https://" extension
		int index = -1;
		if (url.substring(0, 8).equals("https://"))
			url  = url.substring(8);
		
		// Split hostname and path
		index = url.indexOf("/");
		if (index != -1) {
			path = url.substring(index);
			hostname= url.substring(0, index);
		}
		
		filename = hostname.substring(hostname.indexOf(".")+1, hostname.lastIndexOf("."));
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
