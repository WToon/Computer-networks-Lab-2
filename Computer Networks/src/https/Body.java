package https;

/**
 * Represents an HTTP message body.
 * 
 * @author Gebruiker
 *
 */
public class Body {
	
	private String text;
	private byte[] image;
	private int contentType;
	
	public Body() {

	}

	public String getText() {
		return text;
	}

	public byte[] getImage() {
		return image;
	}

	public void setText(String text) {
		this.text = text;
		contentType = 1;
	}

	public void setImage(byte[] image) {
		this.image = image;
		contentType = 2;
	}
	
	public void getContent() {
		switch (contentType) {
			case 1:  System.out.println(getText()); break;
			case 2: System.out.println(getImage()); break;
		}
	}
	
	
	
}

