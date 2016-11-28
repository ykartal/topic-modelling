package ykartal.crawler.tojde.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import tr.com.ykartal.crawler.IArticleStore;
import tr.com.ykartal.crawler.ICrawler;
import tr.com.ykartal.crawler.tojde.TojdeCrawler;

/**
 * Test to article crawl.
 * 
 * @author ykartal
 *
 */
public class TestDownloadArticle {

	/**
	 * Tests crawling article.
	 */
	@Test
	public final void testDownloadArticle() {
		try {
			ICrawler crawler = new TojdeCrawler();
			IArticleStore fileWriter = new FileWriter("output");
			Set<String> keywords = crawler.getKeywordsAboutArticles(fileWriter);

			crawler.getArticles(fileWriter);
			// FIXME check the download folders
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	public static void saveKeywords(Set<String> obj, String path) throws Exception {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
			for (String s : obj) {
				pw.println(s);
			}
			pw.flush();
		} finally {
			pw.close();
		}
	}

}
