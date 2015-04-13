package picz.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import picz.Logger;
import picz.data.Album;
import picz.data.Photo;
import picz.data.Stash;

public class Cache {

	private Logger logger = new Logger(this.getClass());
	
	private List<Record> records = new ArrayList<Record>();
	
	public Cache(List<Album> albums) {
		try {
			loadCache(albums);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void save(List<Album> albums) {
		records.clear();
		createEmptyDirs(albums);
		for (Album a : albums) {
			if (a == null) {
				continue;
			}
			File file = getFile(a);
			if (!file.exists()) {
				try {
					file.createNewFile();
				}
				catch (Exception e) {
					logger.error(e.getMessage(), e);
					continue;
				}
			}
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				for (Photo p : a.getPhotos()) {
					out.writeObject(new Record(p));
				}
				out.close();
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public Record search(String md5) {
		Optional<Record> optional = records.stream()
			.filter(r -> r.getMd5().equals(md5))
			.findFirst();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	private void createEmptyDirs(List<Album> albums) {
		for (Album a : albums) {
			if (a == null) {
				continue;
			}
			File dir = new File(Stash.cacheDir + "/" + a.getDir().getName());
			if (!dir.exists()) {
				dir.mkdirs();
				continue;
			}
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}
	}
	
	private File getFile(Album a) {
		return new File(Stash.cacheDir + "/" + a.getDir().getName() + "/cache.data");
	}
	
	private void loadCache(List<Album> albums) throws IOException, ClassNotFoundException {
		for (Album a : albums) {
			if (a == null) {
				continue;
			}
			File file = getFile(a);
			if (!file.exists()) {
				continue;
			}
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			try {
				Object record = in.readObject();
				while (record != null) {
					records.add((Record)record);
					record = in.readObject();
				}
			}
			catch (Exception e) {
				// end of file
			}
			in.close();
		}
	}
}