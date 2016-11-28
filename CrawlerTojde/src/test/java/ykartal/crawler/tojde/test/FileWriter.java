package ykartal.crawler.tojde.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import tr.com.ykartal.crawler.IArticleStore;

/**
 * An example to FileWriter which implements IFileWriter.
 * 
 * @author Yusuf KARTAL
 *
 */
public class FileWriter implements IArticleStore {

	private String path;

	public FileWriter(String path) {
		this.path = path;
	}

	@Override
	public final void storeArticle(final String outputFilePath, final String pContent) {
		File file = new File(path + outputFilePath);
		file = new File(file.getAbsolutePath());
		makeDir(file.getParentFile());
		try {
			PrintWriter writer = new PrintWriter(path + outputFilePath, "UTF-8");
			String content = pContent.replace('ı', 'i');
			content = content.replace('ş', 's');
			content = content.replace('ç', 'c');
			content = content.replace('ğ', 'g');
			content = content.replace('ö', 'o');
			content = content.replace('ü', 'u');
			writer.print(content);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates given file's all parent folders.
	 * 
	 * @param folder
	 *            file whose folder will create
	 * @return true if folders created false otherwise
	 */
	private boolean makeDir(final File folder) {
		if (folder.exists()) {
			return true;
		}
		if (folder.mkdir()) {
			return true;
		} else {
			makeDir(folder.getParentFile());
			return makeDir(folder);
		}
	}

}
