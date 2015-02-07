package picz;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import picz.data.Album;
import picz.data.Stash;
import picz.jobs.CreateHtmlsJob;
import picz.jobs.ReadAlbumsJob;
import picz.jobs.ReadPhotosJob;
import picz.jobs.ScalePhotoJob;

public class Iguana extends Logger {

	private final static Logger log = new Logger(Iguana.class);
	private final static long startTime = System.currentTimeMillis();
	
	public static void main(String[] args) throws ParseException, ImageProcessingException, IOException {
		log.info("Started");
		parseArgs(args); 
		Stash.checkDirs();
		
		List<Album>	albums = new ReadAlbumsJob().read();
		albums = new ReadPhotosJob(albums).read();
		new ScalePhotoJob(albums).scale();
		new CreateHtmlsJob(albums).createHtmls();
		log.info("Finished in " + log.formatTime(System.currentTimeMillis()-startTime));
	}
	
	private static void parseArgs(String[] args) {
		for (String arg : args) {
			if (arg.trim().startsWith("-t=")) {
				try {
					Stash.THREAD_COUNT = Integer.parseInt(arg.replaceAll("-t=", "").trim());
					log.info("Using " + Stash.THREAD_COUNT + " threads");
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
					System.exit(1);
				}
			}
		}
	}
}