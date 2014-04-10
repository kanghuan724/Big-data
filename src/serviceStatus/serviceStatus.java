
package serviceStatus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;


public class serviceStatus {

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		URL website = new URL("http://www.mta.info/status/serviceStatus.txt");
		Boolean t = true;
		String SERVICE_DATA = "SERVICE_DATA";
		while (t) {
		

			Date date = new Date();
			String sDate = date.toString().replace(":", "_");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			String sFile = "serviceStatus_" + sDate + ".txt";
			@SuppressWarnings("unused")
			File dir = new File(SERVICE_DATA);
			File file = new File(dir,sFile);
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE); 
			Thread.sleep(40000);
	
		
		}
	}	
	
}
