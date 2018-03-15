import https.Request;

public class Test {

	public static void main(String[] args) {
		String[] r = new String[] {"GET", "localhost", "80", "HTTP/1.1"}; 
		Request request = new Request(r);
		System.out.println(request.getRequest());
	}
}
