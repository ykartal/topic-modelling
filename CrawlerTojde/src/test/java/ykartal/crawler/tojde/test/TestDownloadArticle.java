package ykartal.crawler.tojde.test;

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
            IArticleStore fileWriter = new FileWriter();
            // crawler.getArticlesFromUrlAddress(fileWriter, fileWriter);
            // FIXME check the download folders
        } catch (Exception e) {
            Assert.assertFalse(e.getMessage(), true);
        }
    }

}
