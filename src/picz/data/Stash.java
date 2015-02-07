package picz.data;

import picz.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Stash {

	private final static Logger log = new Logger(Stash.class);
	public static int THREAD_COUNT = 1;
	public static final File binDir = new File(System.getProperty("user.dir"));
	private static final File tplsDir = new File(binDir.getAbsolutePath() + "/templates");
	public static final File webDir = new File(binDir.getParent() + "/web");
	public static final File albumsDir = new File(binDir.getParent() + "/albums");
	
	public static final String pageTemplate = readTemplate("body.tmpl");
	public static final String itemTemplate = readTemplate("item.tmpl");
	public static final String photoPageTemplate = readTemplate("photo.tmpl");
	
	public static boolean checkDirs() {
		if (!binDir.exists()) {
			log.error(binDir.getAbsolutePath() + " does not exist");
			return false;
		}
		if (!tplsDir.isDirectory()) {
			log.error(tplsDir.getAbsolutePath() + " does not exist");
			return false;
		}
		if (!webDir.isDirectory()) {
			log.error(webDir.getAbsolutePath() + " does not exist");
			return false;
		}
		if (!albumsDir.isDirectory()) {
			log.error(albumsDir.getAbsolutePath() + " does not exist");
			return false;
		}
		if (!binDir.isDirectory()) {
			log.error(binDir.getAbsolutePath() + " is not a directory");
			return false;
		}
		if (!tplsDir.isDirectory()) {
			log.error(tplsDir.getAbsolutePath() + " is not a directory");
			return false;
		}
		if (!webDir.isDirectory()) {
			log.error(webDir.getAbsolutePath() + " is not a directory");
			return false;
		}
		if (!albumsDir.isDirectory()) {
			log.error(albumsDir.getAbsolutePath() + " is not a directory");
			return false;
		}
		if (!tplsDir.canRead()) {
			log.error(tplsDir.getAbsolutePath() + " cannot read");
			return false;
		}
		if (!webDir.canRead()) {
			log.error(webDir.getAbsolutePath() + " cannot read");
			return false;
		}
		if (!albumsDir.canRead()) {
			log.error(albumsDir.getAbsolutePath() + " cannot read");
			return false;
		}
		if (!webDir.canWrite()) {
			log.error(webDir.getAbsolutePath() + " cannot write");
			return false;
		}
		return true;
	}
	
	private static String readTemplate(String filename) {
		try {
			FileReader reader = new FileReader(tplsDir.getAbsolutePath() + "/" + filename);
			BufferedReader breader = new BufferedReader(reader);
			String line = breader.readLine();
			StringBuilder builder = new StringBuilder();
			while (line != null) {
				builder.append(line).append("\n");
				line = breader.readLine();
			}
			reader.close();
			breader.close();
			return builder.toString();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			System.exit(1);
		}
		return null;
	}
}