package tr.com.ykartal.crawler;

/**
 * 
 * @author Yusuf KARTAL
 *
 */
public interface IArticleStore {

	/**
	 * 
	 * Store content with given key.
	 * 
	 * @param key
	 *            define the content with a key
	 * @param content
	 *            Article content to store
	 * 
	 */
	void storeArticle(String key, String content);

}
