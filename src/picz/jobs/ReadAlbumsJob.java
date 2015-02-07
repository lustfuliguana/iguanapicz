package picz.jobs;

import picz.Logger;
import picz.data.Album;
import picz.data.Stash;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReadAlbumsJob extends Logger {

	public List<Album> read() throws ParseException {
		String[] albumNames = Stash.albumsDir.list();
		List<Album> albums = new ArrayList<Album>();
		for (String albumName : albumNames) {
			File file = new File(Stash.albumsDir.getAbsolutePath() + "/" + albumName);
			if (!file.isDirectory()) {
				info(file.getAbsolutePath() + " is not a direcroty");
				continue;
			}
			if (!file.canRead()) {
				error(file.getAbsolutePath() + " cannot read");
				return null;
			}
			albums.add(new Album(file));
		}
		
		return sort(albums);
	}
	
	private List<Album> sort(List<Album> albums) {
		for (int i = 0; i < albums.size()-1; i++) {
			for (int j = i+1; j < albums.size(); j++) {
				if (albums.get(i).getDate().before(albums.get(j).getDate())) {
					Album album = albums.get(i);
					albums.set(i, albums.get(j));
					albums.set(j, album);
				}
			}
		}
		return albums;
	}
}