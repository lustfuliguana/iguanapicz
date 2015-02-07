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

	public static void main(String[] args) throws ParseException, ImageProcessingException, IOException {
		parseArgs(args); 
		Stash.checkDirs();
		
		List<Album>	albums = new ReadAlbumsJob().read();
		albums = new ReadPhotosJob(albums).read();
		new ScalePhotoJob(albums).scale();
		new CreateHtmlsJob(albums).createHtmls();
	}
	
	private static void parseArgs(String[] args) {
		for (String arg : args) {
			if (arg.trim().startsWith("-t=")) {
				try {
					Stash.THREAD_COUNT = Integer.parseInt(arg.replaceAll("-t=", "").trim());
				}
				catch (Exception e) {
					error(e.getMessage(), e);
				}
			}
		}
	}
}