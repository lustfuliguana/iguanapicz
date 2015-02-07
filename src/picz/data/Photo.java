package picz.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import picz.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Photo extends Logger {
	
	private File file;
	private Album album;
	private Date date;
	private int width;
	private int height;
	
	private Exif exif = null;
	
	public Photo(File file, Album album) throws IOException, ImageProcessingException {
		this.album = album;
		this.file = file;
		
		BufferedImage bufferedImage = ImageIO.read(file);
		width = bufferedImage.getWidth();
		height = bufferedImage.getHeight();
		
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
		if (directory != null) {
			date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		}
		exif = new Exif(metadata);
	}

	public File getFile() {
		return file;
	}

	public Album getAlbum() {
		return album;
	}

	public Date getDate() {
		return date;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Exif getExif() {
		return exif;
	}

	private class Exif {
		private static final String ROTATION = "Rotation";
		private static final String EXPOSURE = "Exposure Time";
		private static final String FNUMBER = "F-Number";
		private static final String ISO = "ISO Speed Ratings";
		private static final String FOCAL_LENGTH = "Focal Length";
		private static final String FOCAL_LENGTH_35 = "Focal Length 35";
		private static final String CAMERA = "Make";
		private static final String MODEL = "Model";
		
		private String rotation = null;
		private String exposure = null;
		private String fnumber = null;
		private String iso = null;
		private String focalLength = null;
		private String focalLength35 = null;
		private String camera = null;
		private String model = null;
		
		private Map<String, String> map = new HashMap<String, String>();
		
		Exif(Metadata metadata) {
			Iterator<Directory> it = metadata.getDirectories().iterator();
			while (it.hasNext()) {
				Directory dir = it.next();
				Collection<Tag> tags = dir.getTags();
				Iterator<Tag> tagsIt = tags.iterator();
				while (tagsIt.hasNext()) {
					Tag tag = tagsIt.next();
					map.put(tag.getTagName(), tag.getDescription());
					
					if (tag.getTagName().equals(ROTATION)) {
						rotation = tag.getDescription();
					}
					else if (tag.getTagName().equals(EXPOSURE)) {
						exposure = tag.getDescription();
					}
					else if (tag.getTagName().equals(FNUMBER)) {
						fnumber = tag.getDescription();
					}
					else if (tag.getTagName().equals(ISO)) {
						iso = tag.getDescription();
					}
					else if (tag.getTagName().equals(FOCAL_LENGTH)) {
						focalLength = tag.getDescription();
					}
					else if (tag.getTagName().equals(FOCAL_LENGTH_35)) {
						focalLength35 = tag.getDescription();
					}
					else if (tag.getTagName().equals(CAMERA)) {
						camera = tag.getDescription();
					}
					else if (tag.getTagName().equals(MODEL)) {
						model = tag.getDescription();
					}
				}
			}
		}

		public String getRotation() {
			return rotation;
		}

		public String getExposure() {
			return exposure;
		}

		public String getFnumber() {
			return fnumber;
		}

		public String getIso() {
			return iso;
		}

		public String getFocalLength() {
			return focalLength;
		}

		public String getFocalLength35() {
			return focalLength35;
		}

		public String getCamera() {
			return camera;
		}

		public String getModel() {
			return model;
		}

		public Map<String, String> getMap() {
			return map;
		}
		
	}
}