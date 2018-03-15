package https;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Response {
	
	String[] statusCodes = {"200", "404", "400", "500", "304"};
	String[] headers = {"Content-Type", "Content-length", "Status-Code", "Date"};
	
    Date date = new Date(System.currentTimeMillis());
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    

}
