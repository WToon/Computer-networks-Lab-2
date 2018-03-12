package https;

/**
 * Represents an HTTP message header.
 * 
 * @author R0596433
 *
 */
public class Header {
	
	private String contentType;
	private int contentLength;
	
	private String headerContent;
		
	public Header() {
		
	}

	public String getContentType() {
		return contentType;
	}
	public int getContentLength() {
		return contentLength;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	
	public void setHeaderContent(String headerContent) {
		this.headerContent = headerContent;
	}
	public String getContent() {
		System.out.println(this.headerContent);
		return this.headerContent;
	}
	
	
	
	
	
}
