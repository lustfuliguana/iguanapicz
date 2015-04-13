package picz.cache;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import picz.data.Exif;
import picz.data.Photo;

public class Record implements Externalizable {

	private String md5 = "";
	private String albumFileName = "";
	private String fileName = "";
	private int width = 0;
	private int height = 0;
	private Exif exif = new Exif();
	private Date date = new Date();
	private boolean empty = true;
	
	public Record() {
	}
	
	public Record(Photo p) {
		md5 = p.getMd5();
		albumFileName = p.getAlbum().getDir().getName();
		fileName = p.getFile().getName();
		width = p.getWidth();
		height = p.getHeight();
		exif = p.getExif();
		date = p.getDate();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(md5);
		out.writeObject(albumFileName);
		out.writeObject(fileName);
		out.writeInt(width);
		out.writeInt(height);
		out.writeObject(exif);
		out.writeObject(date);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		md5 = (String) in.readObject();
		albumFileName = (String) in.readObject();
		fileName = (String) in.readObject();
		width = in.readInt();
		height = in.readInt();
		exif = (Exif) in.readObject();
		date = (Date) in.readObject();
		if (!md5.trim().isEmpty()) {
			empty = false;
		}
	}

	public String getMd5() {
		return md5;
	}

	public String getAlbumFileName() {
		return albumFileName;
	}

	public String getFileName() {
		return fileName;
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

	public Date getDate() {
		return date;
	}

	public boolean isEmpty() {
		return empty;
	}
}