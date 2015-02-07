package picz.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import picz.Logger;
import picz.data.Album;
import picz.data.Stash;

public class ReadPhotosJob extends Logger {

	private List<Album> albums;
	private List<Thread> threads = new ArrayList<Thread>();
	private int albumIndex = -1;
	
	public ReadPhotosJob(List<Album> albums) {
		this.albums = albums;
	}
	
	public List<Album> read() throws ImageProcessingException, IOException {
		for (int i = 0; i < Stash.THREAD_COUNT; i++) {
			Thread t = new Thread(new ReadPhotosRunnable());
			threads.add(t);
			t.start();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				error(e.getMessage(), e);
			}
		}
		
		return albums;
	}
	
	private synchronized Album getNextAlbum() {
		albumIndex = albumIndex + 1;
		if (albums.size() <= albumIndex) {
			return null;
		}
		return albums.get(albumIndex);
	}
	
	private class ReadPhotosRunnable implements Runnable {

		@Override
		public void run() {
			Album a = getNextAlbum();
			while (a != null) {
				String[] fileNames = a.getDir().list();
				for (String fileName : fileNames) {
					File file = new File(a.getDir().getAbsolutePath() + "/" + fileName);
					try {
						a.addPhoto(file);
					} catch (ImageProcessingException | IOException e) {
						error(e.getMessage(), e);
					}
				}
				a = getNextAlbum();
			}
		}
		
	}
}