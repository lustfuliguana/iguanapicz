package picz.html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import picz.data.Album;
import picz.data.Stash;

public class IndexPage extends Page {

	private StringBuilder content = new StringBuilder();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	
	public IndexPage(List<Album> albums) throws UnsupportedEncodingException {
		for (Album a : albums) {
			content.append(createItemHtml(a));
		}
	}
	
	private String createItemHtml(Album a) throws UnsupportedEncodingException {
		return Stash.itemTemplate.replaceAll("##ITEM_URL##", "html/"+a.getDir().getName() + "/index.html")
					.replaceAll("##ITEM_IMG_URL##", "img/240/"+a.getDir().getName()+"/"+URLEncoder.encode(a.getLogo().getFile().getName(), "UTF-8"))
					.replaceAll("##ITEM_TITLE_CLASS##", "")
					.replaceAll("##ITEM_TITLE##", a.getTitle())
					.replaceAll("##ITEM_CLASS_GREY##", "item-grey-text")
					.replaceAll("##EXIF_BTN_CLASS##", "hidden")
					.replaceAll("##ITEM_GREY_TEXT##", sdf.format(a.getDate()));
	}
	
	public String createPageHtml(String metaTitle,
			String titleUrl, String title) {
		return super.createPageHtml(metaTitle, titleUrl, title, content.toString())
				.replaceAll("##EXIF_BTN_CLASS##", "hidden");
	}
}