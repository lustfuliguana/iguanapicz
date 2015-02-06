package iguanapicz;

import iguanapicz.data.Album;
import iguanapicz.data.Photo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Logger {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private enum Level {
		INFO, ERROR
	};
	
	private static final void log(Level level, String msg, Exception e) {
		Date dt = new Date();
		System.out.println(sdf.format(dt) + " [" + level.name() + "] " + msg);
		if (e != null) {
			e.printStackTrace();
		}
	}
	
	protected static final void info(String msg) {
		log(Level.INFO, msg, null);
	}
	
	protected static final void error(String msg, Exception e) {
		log(Level.ERROR, msg, e);
	}
	
	protected static final void error(String msg) {
		log(Level.ERROR, msg, null);
	}
	
	protected static final void print(List<Album> albums) {
		if (albums == null) {
			info("List is null");
			return;
		}
		if (albums.isEmpty()) {
			info("List is empty");
			return;
		}
		info("Found " + albums.size() + " albums");
		for (Album a : albums) {
			info(a.getDate() + ": " + a.getTitle() + " has " + a.getPhotos().size() + " photos");
			for (Photo p : a.getPhotos()) {
				info(p.getFile().getName() + " " + p.getWidth() + "x" + p.getHeight() + " " + p.getDate());
			}
		}
	}
}