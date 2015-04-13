package picz.data;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.imageio.ImageIO;

import picz.Logger;
import picz.cache.Cache;
import picz.cache.Record;
import picz.tools.ScaleTool;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Photo extends Logger {
	
	private final static String horizontal = "horizontal";
	
	private File file;
	private Album album;
	private Date date;
	private int width;
	private int height;
	private Exif exif = null;
	private String md5;
	
	public Photo(File file, Album album, Cache cache) throws IOException, ImageProcessingException {
		this.album = album;
		this.file = file;
		
		if (readFromCache(cache)) {
			return;
		}
		BufferedImage bufferedImage = ImageIO.read(file);
		width = bufferedImage.getWidth();
		height = bufferedImage.getHeight();
		
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
		if (directory != null) {
			date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		}
		exif = new Exif(metadata);
		scale(bufferedImage);
	}
	
	private static String hashFile(File file, String algorithm) {
	    try (FileInputStream inputStream = new FileInputStream(file)) {
	        MessageDigest digest = MessageDigest.getInstance(algorithm);
	 
	        byte[] bytesBuffer = new byte[1024];
	        int bytesRead = -1;
	 
	        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
	            digest.update(bytesBuffer, 0, bytesRead);
	        }
	 
	        byte[] hashedBytes = digest.digest();
	 
	        return convertByteArrayToHexString(hashedBytes);
	    } catch (NoSuchAlgorithmException | IOException ex) {
	    	ex.printStackTrace();
	    	return null;
	    }
	}
	
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
    
	private boolean readFromCache(Cache cache) {
		if (cache == null) {
			return false;
		}
		md5 = hashFile(file, "MD5");
		if (md5 == null) {
			return false;
		}
		Record record = cache.search(md5);
		if (record == null || record.isEmpty()) {
			return false;
		}
		width = record.getWidth();
		height = record.getHeight();
		date = record.getDate();
		exif = record.getExif();
		
		return true;
	}
	
	private void scale(BufferedImage image) {
		if (exif.getRotation() != null && !exif.getRotation().toLowerCase().equals(horizontal)) {
			Graphics2D g = image.createGraphics();
			g.rotate(-Math.PI/2);
			g.dispose();
		}
		try {
			ScaleTool.scale(file, Stash.webDir + "/img/240/"
					+ album.getDir().getName() + "/" + file.getName(),
					240, 240, 100, false, image);
			ScaleTool.scale(file, Stash.webDir + "/img/1024/"
					+ album.getDir().getName() + "/" + file.getName(),
					1024, 768, 100, true, image);
		} catch (IOException e) {
			error(e.getMessage(), e);
		}
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

	public String getMd5() {
		return md5;
	}
}