package picz.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import picz.Logger;
import picz.data.Album;
import picz.cache.Cache;
import picz.data.Stash;

public class ReadPhotosJob extends Logger {

	private List<Album> albums;
	private List<Thread> threads = new ArrayList<Thread>();
	private Cache cache;
	
	private int fileIndex = -1;
	String[] fileNames = null;
	private int albumIndex = -1;
	
	public ReadPhotosJob(List<Album> albums, Cache cache) {
		this.albums = albums;
		this.cache = cache;
	}

	public List<Album> read() throws ImageProcessingException, IOException {
		for (albumIndex = 0; albumIndex < albums.size(); albumIndex++) {
			threads.clear();
			fileNames = albums.get(albumIndex).getDir().list();
			if (fileNames == null || fileNames.length == 0) {
				continue;
			}
			fileIndex = -1;
			for (int j = 0; j < Stash.THREAD_COUNT; j++) {
				Thread t = new Thread(new ReadPhotoRunnable(j+1));
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
		}

		return albums;
	}

	private synchronized String getNextFileName() {
		int index = fileIndex + 1;
		if (fileNames == null || fileNames.length <= index) {
			return null;
		}
		fileIndex = index;
		return fileNames[fileIndex];
	}

	private synchronized int getFileIndex() {
		return fileIndex;
	}
	
	private class ReadPhotoRunnable implements Runnable {

		private int threadIndex = -1;
		
		ReadPhotoRunnable(int index) {
			this.threadIndex = index;
		}
		
		@Override
		public void run() {
			String fileName = getNextFileName();
			while (fileName != null) {
				info("Thread #" + threadIndex + " Read " + fileName+ "\t" + (getFileIndex() + 1) + "/" + fileNames.length
						+ " \t(" + (albumIndex+1) + "/" + albums.size() + ")"
						+ ": \t" + albums.get(albumIndex).getTitle() + "/" + fileName);
				File file = new File(albums.get(albumIndex).getDir().getAbsolutePath() + "/"
						+ fileName);
				albums.get(albumIndex).addPhoto(file, cache);
				fileName = getNextFileName();
			}
		}

	}
}