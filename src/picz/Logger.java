package picz;

import picz.data.Album;
import picz.data.Photo;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Logger {

	@SuppressWarnings("rawtypes")
	private Class clazz = null; 
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static final PrintStream defaultOut = System.out;
	private static final PrintStream errorOut = System.err;
	
	private enum Level {
		INFO, ERROR
	};
	
	public Logger() {
		this.clazz = this.getClass();
	}
	
	public Logger(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;
	}
	
	public String formatTime(long millis) {
		long h = 0;
		long m = 0;
		long s = 0;
		millis = millis/1000;
		h = millis/60/60;
		m = (millis - h*60*60)/60;
		s = millis - h*60*60 - m*60;
		
		StringBuilder result = new StringBuilder();
		if (h > 0) {
			if (h < 10) {
				result.append("0");
			}
			result.append(h).append(":");
		}
		if (m < 10) {
			result.append("0");
		}
		result.append(m).append(":");
		if (s < 10) {
			result.append("0");
		}
		result.append(s);
		
		return result.toString();
	}
	
	private final void log(Level level, String msg, Exception e) {
		Date dt = new Date();
		String t = sdf.format(dt) + " [" + level.name() + "] "
				+ clazz.getCanonicalName() + ": " + msg;
		if (level == Level.ERROR) {
			errorOut.println(t.trim());
		}
		else {
			defaultOut.println(t.trim());
		}
		if (e != null) {
			e.printStackTrace();
		}
	}
	
	public final void info(String msg) {
		log(Level.INFO, msg, null);
	}
	
	public final void error(String msg, Exception e) {
		log(Level.ERROR, msg, e);
	}
	
	public final void error(String msg) {
		log(Level.ERROR, msg, null);
	}
	
	public final void print(List<Album> albums) {
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