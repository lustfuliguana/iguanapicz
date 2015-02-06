package picz.html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import picz.data.Album;
import picz.data.Photo;
import picz.data.Stash;

public class AlbumPage extends Page {

	private StringBuilder content = new StringBuilder();
	
	public AlbumPage(Album a) throws UnsupportedEncodingException {
		for (Photo p : a.getPhotos()) {
			content.append(Stash.itemTemplate.replaceAll("##ITEM_URL##", URLEncoder.encode(p.getFile().getName(), "UTF-8") +".html")
				.replaceAll("##ITEM_IMG_URL##", "../../img/240/"+a.getDir().getName()+"/"+URLEncoder.encode(p.getFile().getName(), "UTF-8"))
				.replaceAll("##ITEM_TITLE_CLASS##", "")
				.replaceAll("##ITEM_TITLE##", "")
				.replaceAll("##ITEM_CLASS_GREY##", "hidden")
				.replaceAll("##ITEM_GREY_TEXT##", ""));
		}
	}
	
	public String createPageHtml(String metaTitle,
			String titleUrl, String title) {
		return super.createPageHtml(metaTitle, titleUrl, title, content.toString());
	}
}