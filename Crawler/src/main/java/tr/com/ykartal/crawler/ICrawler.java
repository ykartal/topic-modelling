package tr.com.ykartal.crawler;

import java.util.Set;

/**
 * 
 * @author Yusuf KARTAL
 *
 */
public interface ICrawler {

	/**
	 * Implement to read articles and send content of articles to store.
	 * 
	 * @param articleStore
	 *            To store whole content of article
	 */
	void getArticles(IArticleStore articleStore);

	/**
	 * Keyword list about Articles. It may be keyword1, keyword key, key keyword2 keyword3, keyword2
	 * 
	 * @return the list of keywords
	 */
	Set<String> getKeywordsAboutArticles(IArticleStore writer);
}
