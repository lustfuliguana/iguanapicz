package picz.jobs;

import picz.data.Album;
import picz.data.Photo;
import picz.data.Stash;
import picz.html.AlbumPage;
import picz.html.IndexPage;
import picz.html.PhotoPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class CreateHtmlsJob {

	private List<Album> albums;

	public CreateHtmlsJob(List<Album> albums) {
		this.albums = albums;
	}

	public void createHtmls() throws IOException {
		createPhotoPages();
		createIndexPage();
		createAlbumPages();
	}

	private void createAlbumPages() throws UnsupportedEncodingException, IOException {
		for (Album a : albums) {
			write(Stash.webDir.getAbsolutePath() + "/html/" + a.getDir().getName()
					+ "/index.html", new AlbumPage(a).createPageHtml(
					a.getTitle(), "../../index.html", a.getTitle()));
		}
	}

	private void createPhotoPages() throws UnsupportedEncodingException, IOException {
		for (Album a : albums) {
			for (Photo p : a.getPhotos()) {
				write(Stash.webDir.getAbsolutePath() + "/html/"
						+ a.getDir().getName() + "/" + p.getFile().getName() + ".html",
						new PhotoPage(p).createPageHtml(p.getFile().getName()
								+ " - " + a.getTitle(), "index.html",
								a.getTitle()));
			}
		}
	}

	private void createIndexPage() throws IOException {
		write(Stash.webDir.getAbsolutePath() + "/index.html", new IndexPage(
				albums).createPageHtml("Всі альбоми", "javascript: void(0);",
				"Всі альбоми"));
	}

	private void write(String output, String data) throws IOException {
		File file = new File(output);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileWriter writer = new FileWriter(file);
		writer.write(data);
		writer.close();
	}
}