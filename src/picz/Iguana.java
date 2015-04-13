package picz;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import picz.cache.Cache;
import picz.data.Album;
import picz.data.Stash;
import picz.jobs.CreateHtmlsJob;
import picz.jobs.ReadAlbumsJob;
import picz.jobs.ReadPhotosJob;

public class Iguana extends Logger {

	private final static Logger log = new Logger(Iguana.class);
	private final static long startTime = System.currentTimeMillis();
	private final static int version = 3;
	
	public static void main(String[] args) throws ParseException, ImageProcessingException, IOException {
		parseArgs(args); 
		if (!Stash.checkDirs()) {
			return;
		}
		
		List<Album>	albums = new ReadAlbumsJob().read();
		Cache cache = new Cache(albums);
		new ReadPhotosJob(albums, cache).read();
		new CreateHtmlsJob(albums).createHtmls();
		cache.save(albums);
		log.info("Finished in " + log.formatTime(System.currentTimeMillis()-startTime));
	}
	
	private static void parseArgs(String[] args) {
		for (String arg : args) {
			if (arg.trim().startsWith("-t=")) {
				try {
					Stash.THREAD_COUNT = Integer.parseInt(arg.replaceAll("-t=", "").trim());
					log.info("Using " + Stash.THREAD_COUNT + " threads; version="+version);
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
					System.exit(1);
				}
			}
		}
	}
}