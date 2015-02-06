package iguanapicz;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import iguanapicz.data.Album;
import iguanapicz.data.Stash;
import iguanapicz.jobs.CreateHtmlsJob;
import iguanapicz.jobs.ReadAlbumsJob;
import iguanapicz.jobs.ReadPhotosJob;
import iguanapicz.jobs.ScalePhotoJob;

public class Iguana extends Logger {

	public static void main(String[] args) throws ParseException, ImageProcessingException, IOException {
		Stash.checkDirs();
		
		List<Album>	albums = new ReadAlbumsJob().read();
		albums = new ReadPhotosJob(albums).read();
		new ScalePhotoJob(albums).scale();
		new CreateHtmlsJob(albums).createHtmls();
	}
}