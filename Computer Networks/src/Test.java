
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import https.Request;

public class Test {

	public static void main(String[] args) {
		
		try {
			byte[] content = Files.readAllBytes(Paths.get("server/webpage.html"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
