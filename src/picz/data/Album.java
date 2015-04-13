package picz.data;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import picz.Logger;
import picz.cache.Cache;

public class Album extends Logger {

	private String title;
	private File dir;
	private Date date;
	private List<Photo> photos = new ArrayList<Photo>();
	private boolean sorted = false;
	private Photo logo;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	
	public Album(File dir) throws ParseException {
		this.dir = dir;
		String dirName = dir.getName();
		date = sdf.parse(dirName.substring(0, dirName.indexOf('_')));
		title = dirName.substring(dir.getName().indexOf('_')+1);
	}
	
	public void addPhoto(File file, Cache cache) {
		Photo photo = null;
		try {
			photo = new Photo(file, this, cache);
		} catch (Exception e) {
			error("Failed to create photo object", e);
		}
		if (photo == null) {
			return;
		}
		
		addPhoto(photo);
	}
	
	private synchronized void addPhoto(Photo photo) {
		photos.add(photo);
	}
	
	public List<Photo> getPhotos() {
		if (sorted) {
			return photos;
		}
		sorted = true;
		
		for (int i = 0; i < photos.size()-1; i++) {
			if (photos.get(i).getDate() == null) {
				continue;
			}
			for (int j = i+1; j < photos.size(); j++) {
				if (logo == null) {
					if (photos.get(i).getFile().getName().startsWith("00_")) {
						logo = photos.get(i);
					}
				}
				if (photos.get(j).getDate() == null) {
					continue;
				}
				if (photos.get(i).getDate().after(photos.get(j).getDate())) {
					Photo photo = photos.get(i);
					photos.set(i, photos.get(j));
					photos.set(j, photo);
				}
			}
		}
		
		if (logo == null && !photos.isEmpty()) {
			logo = photos.get(0);
		}
		
		return photos;
	}

	public Photo getLogo() {
		return logo;
	}

	public Date getDate() {
		return date;
	}

	public File getDir() {
		return dir;
	}

	public String getTitle() {
		return title;
	}
}