package iguanapicz.data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.drew.imaging.ImageProcessingException;

import iguanapicz.Logger;

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
	
	public void addPhoto(File file) throws ImageProcessingException, IOException {
		photos.add(new Photo(file, this));
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
		
		if (logo == null) {
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