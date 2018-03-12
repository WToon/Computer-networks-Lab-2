package https;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Represents an HTTP message body.
 * 
 * @author Gebruiker
 *
 */
public class Body {
	
	private String text, baseUri;
	private byte[] image;
	private int contentType;
	
	public Body(String type, String baseUri) {
		this.baseUri = baseUri;
		if (type == "html") {
			contentType = 1;
		}
		if (type == "jpg") {
			contentType = 2;
		}
		if (type == "png") {
			contentType = 2;
		}
	}
	
	public ArrayList<Request> generateImageRequests() {
		ArrayList<Request> requests = new ArrayList<>();
		
		Document doc = Jsoup.parse(text, baseUri);
		Elements images = doc.select("img[src$=.png], img[src$=.jpg]");
		for (Element image : images) {
			requests.add(new Request("GET", "/" + image.attr("src"), baseUri));
		}

		return requests;
	}
	
	public String getText() {
		return text;
	}

	public byte[] getImage() {
		return image;
	}
	
	public void getContent() {
		switch (contentType) {
			case 1:  System.out.println(getText()); break;
			case 2: System.out.println(getImage()); break;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}	
}

