package iguanapicz.html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import iguanapicz.data.Photo;
import iguanapicz.data.Stash;

public class PhotoPage extends Page {

	private StringBuilder content = new StringBuilder();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	public PhotoPage(Photo p) throws UnsupportedEncodingException {
		String prev = null;
		String next = null;
		int index = p.getAlbum().getPhotos().indexOf(p);
		if (index == p.getAlbum().getPhotos().size()-1) {
			next = URLEncoder.encode(p.getAlbum().getPhotos().get(0).getFile().getName(), "UTF-8");
		}
		else {
			next = URLEncoder.encode(p.getAlbum().getPhotos().get(index + 1).getFile().getName(), "UTF-8");
		}
		
		if (index == 0) {
			prev = URLEncoder.encode(p.getAlbum().getPhotos().get(p.getAlbum().getPhotos().size()-1).getFile().getName(), "UTF-8");
		}
		else {
			prev = URLEncoder.encode(p.getAlbum().getPhotos().get(index - 1).getFile().getName(), "UTF-8");
		}
		String dt = "";
		if (p.getDate() != null) {
			dt = sdf.format(p.getDate());
		}
		content.append(Stash.photoPageTemplate.replaceAll("##IMG_DATE##", dt)
				.replaceAll("##PREV_URL##", prev + ".html")
				.replaceAll("##NEXT_URL##", next + ".html")
				.replaceAll("##IMG_URL##", "../../img/1024/"+p.getAlbum().getDir().getName() + "/" + URLEncoder.encode(p.getFile().getName(), "UTF-8")));
	}
	
	public String createPageHtml(String metaTitle,
			String titleUrl, String title) {
		return super.createPageHtml(metaTitle, titleUrl, title, content.toString());
	}
}