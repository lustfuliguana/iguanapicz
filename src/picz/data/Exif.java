package picz.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

@SuppressWarnings("serial")
public class Exif implements Serializable {
	public static final String ROTATION = "Rotation";
	public static final String EXPOSURE = "Exposure Time";
	public static final String FNUMBER = "F-Number";
	public static final String ISO = "ISO Speed Ratings";
	public static final String FOCAL_LENGTH = "Focal Length";
	public static final String FOCAL_LENGTH_35 = "Focal Length 35";
	public static final String CAMERA = "Make";
	public static final String MODEL = "Model";
	
	private String rotation = null;
	private String exposure = null;
	private String fnumber = null;
	private String iso = null;
	private String focalLength = null;
	private String focalLength35 = null;
	private String camera = null;
	private String model = null;
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public Exif() {
		
	}
	
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