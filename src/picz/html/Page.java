package picz.html;

import picz.data.Stash;

public abstract class Page {

	abstract protected String createPageHtml(String metaTitle,
			String titleUrl, String title);
	
	protected String createPageHtml(String metaTitle,
			String titleUrl, String title, String content) {
		return Stash.pageTemplate.replaceAll("##META_TITLE##", metaTitle)
				.replaceAll("##TITLE_URL##", titleUrl)
				.replaceAll("##TITLE##", title)
				.replaceAll("##CONTENT##", content);
	}
}