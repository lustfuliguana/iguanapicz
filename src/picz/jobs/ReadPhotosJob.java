package picz.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import picz.Logger;
import picz.data.Album;

public class ReadPhotosJob extends Logger {

	private List<Album> albums;
	
	public ReadPhotosJob(List<Album> albums) {
		this.albums = albums;
	}
	
	public List<Album> read() throws ImageProcessingException, IOException {
		for (Album a : albums) {
			String[] fileNames = a.getDir().list();
			for (String fileName : fileNames) {
				File file = new File(a.getDir().getAbsolutePath() + "/" + fileName);
				a.addPhoto(file);
			}
		}
		return albums;
	}
}