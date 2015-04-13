package picz.html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import picz.data.Exif;
import picz.data.Photo;
import picz.data.Stash;

public class PhotoPage extends Page {

	private StringBuilder content = new StringBuilder();
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm:ss");
	private StringBuilder exifShortHtml = new StringBuilder();
	private StringBuilder exifFullHtml = new StringBuilder();

	public PhotoPage(Photo p) throws UnsupportedEncodingException {
		String prev = null;
		String next = null;
		int index = p.getAlbum().getPhotos().indexOf(p);
		if (index == p.getAlbum().getPhotos().size() - 1) {
			next = URLEncoder.encode(p.getAlbum().getPhotos().get(0).getFile()
					.getName(), "UTF-8");
		} else {
			next = URLEncoder.encode(p.getAlbum().getPhotos().get(index + 1)
					.getFile().getName(), "UTF-8");
		}

		if (index == 0) {
			prev = URLEncoder.encode(
					p.getAlbum().getPhotos()
							.get(p.getAlbum().getPhotos().size() - 1).getFile()
							.getName(), "UTF-8");
		} else {
			prev = URLEncoder.encode(p.getAlbum().getPhotos().get(index - 1)
					.getFile().getName(), "UTF-8");
		}
		String dt = "";
		if (p.getDate() != null) {
			dt = sdf.format(p.getDate());
		}
		content.append(Stash.photoPageTemplate
				.replaceAll("##IMG_DATE##", dt)
				.replaceAll("##PREV_URL##", prev + ".html")
				.replaceAll("##NEXT_URL##", next + ".html")
				.replaceAll(
						"##IMG_URL##",
						"../../img/1024/"
								+ p.getAlbum().getDir().getName()
								+ "/"
								+ URLEncoder.encode(p.getFile().getName(),
										"UTF-8")));

		fillExifShortHtml(p);
		fillExifFullHtml(p);
	}

	private void fillExifFullHtml(Photo p) {
		if (p.getExif() == null) {
			return;
		}
		for (Entry<String, String> entry : p.getExif().getMap().entrySet()) {
			exifFullHtml.append("<tr>").append("<th>").append(entry.getKey())
				.append("</th>").append("<td>")
				.append(entry.getValue()).append("</td>")
				.append("</tr>");
		}
	}
	
	private void fillExifShortHtml(Photo p) {
		if (p.getExif() == null) {
			return;
		}
		if (p.getExif().getCamera() != null) {
			exifShortHtml.append("<tr>").append("<th>").append("Camera")
					.append("</th>").append("<td>")
					.append(p.getExif().getCamera()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getModel() != null) {
			exifShortHtml.append("<tr>").append("<th>").append("Model")
					.append("</th>").append("<td>")
					.append(p.getExif().getModel()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getExposure() != null) {
			p.getExif();
			exifShortHtml.append("<tr>").append("<th>").append(Exif.EXPOSURE)
					.append("</th>").append("<td>")
					.append(p.getExif().getExposure()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getFnumber() != null) {
			p.getExif();
			exifShortHtml.append("<tr>").append("<th>").append(Exif.FNUMBER)
					.append("</th>").append("<td>")
					.append(p.getExif().getFnumber()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getFocalLength() != null) {
			exifShortHtml.append("<tr>").append("<th>")
					.append(Exif.FOCAL_LENGTH).append("</th>").append("<td>")
					.append(p.getExif().getFocalLength()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getFocalLength35() != null) {
			exifShortHtml.append("<tr>").append("<th>")
					.append(Exif.FOCAL_LENGTH_35).append("</th>")
					.append("<td>").append(p.getExif().getFocalLength35())
					.append("</td>").append("</tr>");
		}
		if (p.getExif().getIso() != null) {
			exifShortHtml.append("<tr>").append("<th>").append(Exif.ISO)
					.append("</th>").append("<td>")
					.append(p.getExif().getIso()).append("</td>")
					.append("</tr>");
		}
		if (p.getExif().getRotation() != null) {
			exifShortHtml.append("<tr>").append("<th>").append(Exif.ROTATION)
					.append("</th>").append("<td>")
					.append(p.getExif().getRotation()).append("</td>")
					.append("</tr>");
		}
	}

	public String createPageHtml(String metaTitle, String titleUrl, String title) {
		return super
				.createPageHtml(metaTitle, titleUrl, title, content.toString())
				.replaceAll("##EXIF_BTN_CLASS##", "")
				.replaceAll("##EXIF_SHORT##", Matcher.quoteReplacement(exifShortHtml.toString()))
				.replaceAll("##EXIF_FULL##", Matcher.quoteReplacement(exifFullHtml.toString()));
	}
}