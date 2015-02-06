package iguanapicz.jobs;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import iguanapicz.Logger;
import iguanapicz.data.Album;
import iguanapicz.data.Photo;
import iguanapicz.data.Stash;

public class ScalePhotoJob extends Logger {

	private List<Album> albums;

	public ScalePhotoJob(List<Album> albums) {
		this.albums = albums;
	}

	public void scale() throws IOException {
		for (Album a : albums) {
			for (Photo p : a.getPhotos()) {
				scale(p.getFile(), Stash.webDir + "/img/240/"
						+ a.getDir().getName() + "/" + p.getFile().getName(),
						240, 240, 100, false);
				scale(p.getFile(), Stash.webDir + "/img/1024/"
						+ a.getDir().getName() + "/" + p.getFile().getName(),
						1024, 768, 100, false);
			}
		}
	}

	private void scale(File file, String output, int w, int h, int q,
			boolean preventCrop) throws IOException {
		File out = new File(output);
		if (!out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}
		if (out.exists()) {
			return;
		}

		resizeImageToFixedSize(file, w, h, out, q / 100, preventCrop);
	}

	// scales image to fixes size; crops image if needed (using specs)
	private void resizeImageToFixedSize(File file, int w, int h, File output,
			float q, boolean preventCrop) throws IOException {
		BufferedImage image = ImageIO.read(file);
		int width = image.getWidth();
		int height = image.getHeight();
		int x = 0;
		int y = 0;
		boolean crop = true;

		if (width <= w && height <= h) {
			saveImage(image, output, q);
			return;
		}

		// get subimage if needed
		if (Math.abs(((float) width / (float) height) - ((float) w / (float) h)) < .001) {
			crop = false;
		} else if (((float) width / (float) height) < ((float) w / (float) h)) {
			height = (h * width) / w;
		} else {
			width = (w * height) / h;
			x = (image.getWidth() - width) / 2;
		}

		// must crop?
		if (crop && !preventCrop) {
			if (width > image.getWidth()) {
				width = image.getWidth();
			}
			if (height > image.getHeight()) {
				height = image.getHeight();
			}
			if (x < 0) {
				x = 0;
			}
			if ((x + width) > image.getWidth()) {
				width = image.getWidth() - x;
			}
			image = image.getSubimage(x, y, width, height);
		}

		// scale and save
		if (image.getWidth() <= w && image.getHeight() <= h) {
			saveImage(image, output, q);
		} else {
			if (preventCrop
					&& (float) w / (float) h != (float) image.getWidth()
							/ (float) image.getHeight()) {
				if (image.getWidth() > image.getHeight()) {
					h = (int) ((float) image.getHeight()
							/ (float) image.getWidth() * (float) w);
				} else {
					w = (int) ((float) image.getWidth()
							/ (float) image.getHeight() * (float) h);
				}
			}

			scaleAndSaveImage(image, w, h, output, q);
		}
	}

	// resize image and then saveImage
	private void scaleAndSaveImage(BufferedImage sourceImage, int width,
			int height, File file, float jpegQuality) {
		Image imageObject = sourceImage.getScaledInstance(width, height,
				Image.SCALE_AREA_AVERAGING);
		BufferedImage bufferedImageDest = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = bufferedImageDest.createGraphics();
		graphics2d.drawImage(imageObject, 0, 0, null);
		graphics2d.dispose();

		saveImage(bufferedImageDest, file, jpegQuality);

		bufferedImageDest = null;
		imageObject = null;
	}

	// save image to file
	@SuppressWarnings("rawtypes")
	private void saveImage(BufferedImage bufferedImage, File file,
			float jpegQuality) {
		ImageOutputStream outputStream = null;
		try {
			outputStream = ImageIO.createImageOutputStream(file);

			JPEGImageWriteParam writerParams = new JPEGImageWriteParam(
					new Locale("uk"));
			writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			writerParams.setCompressionQuality(jpegQuality);

			ImageWriter writer = null;
			Iterator iter = (Iterator) ImageIO
					.getImageWritersByFormatName("jpg");
			if (iter.hasNext()) {
				writer = (ImageWriter) iter.next();
			}

			if (writer != null) {
				writer.setOutput(outputStream);
				writer.write(null, new IIOImage(bufferedImage, null, null),
						writerParams);
				writer.dispose();
			}
		} catch (IOException e) {
			error(e.getMessage(), e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					error(e.getMessage(), e);
				}
			}
		}
	}
}