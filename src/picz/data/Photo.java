package picz.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Photo {

	private File file;
	private Album album;
	private Date date;
	private int width;
	private int height;
	
	
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
}