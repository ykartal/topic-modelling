package tr.com.ykartal.crawler.tojde;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tr.com.ykartal.crawler.IArticleStore;
import tr.com.ykartal.crawler.ICrawler;

/**
 * Get articles and crawl them to text files and collect keywords for each article.
 * 
 * @author Yusuf Kartal
 *
 */
public class TojdeCrawler implements ICrawler {

	private Map<String, Elements> volumeLinksMap = new HashMap<String, Elements>();

	/**
	 * Constructor to crawl articles from anywhere.
	 */
	public TojdeCrawler() {
	}

	@Override
	public final void getArticles(final IArticleStore articleWriter) {
		String articleLinkMap = "";
		try {
			int articleCount = 0;
			for (String filename : volumeLinksMap.keySet()) {
				Elements volumeLinks = volumeLinksMap.get(filename);
				for (int i = 0; i < volumeLinks.size(); i++) {
					Element volumeLink = volumeLinks.get(i);

					if (volumeLink.attr("abs:href").contains("makaleler") && volumeLink.attr("abs:href").contains("published")) {

						// read article and save as text
						try {
							System.out.println(articleCount + ": " + volumeLink.attr("abs:href"));
							String content = pdftoText(volumeLink.attr("abs:href"));
							articleLinkMap += volumeLink.attr("abs:href") + "\t" + filename + "\t" + filename + "\n";
							articleWriter.storeArticle("articles/" + filename, content);
							articleCount++;
							// if (articleCount > 10) {// TODO remove this code of block to
							// // get all articles
							// System.out.println("ok");
							// return;
							// }
						} catch (Exception exc) {
							// continue to next
						}
					}
				}

			}
			System.out.println("Downloaded Article Count: " + articleCount);
			articleWriter.storeArticle("article_links.txt", articleLinkMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <li>Generates filename according to given volume and article to index with year.
	 * <li>Add article subject to file name with underscored instead spaces.
	 * <li>Generated filename will never be longer than 70 chars.
	 * 
	 * @param volume
	 *            to get year and index of article
	 * @param articleSubject
	 *            subject of Article to add end of filename
	 * @return generated filename something like 2015-16-Determinants_of_the_Use_of_Technological_Innovation_in_Distanc
	 */
	private static String generateFileName(final String volume, final String articleSubject) {
		final int lengthOfYearWord = 5;
		final int maxLengthOfFileName = 70;
		String filename = volume.substring(volume.indexOf("Year:") + lengthOfYearWord) + "-"
				+ volume.substring(volume.indexOf(":") + 1, volume.indexOf("Issue:") - 1) + "-"
				+ articleSubject.replaceAll("([^a-zA-Z0-9])", "_");
		if (filename.length() > maxLengthOfFileName) {
			filename = filename.substring(0, maxLengthOfFileName);
		}
		return filename + ".txt";
	}

	/**
	 * Extract text from PDF Document.
	 * 
	 * @param path
	 *            Remote Path of the PDF file
	 * @return content of the pdf file
	 */
	private static String pdftoText(final String path) {
		String parsedText = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		try {
			PDFParser parser = new PDFParser(new URL(path).openStream());
			parser.parse();
			cosDoc = parser.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			parsedText = pdfStripper.getText(pdDoc);
			if (parsedText.contains("Turkish Online Journal of Distance Education")) {
				parsedText = parsedText.substring(parsedText.indexOf('\n', parsedText.indexOf("Volume:")));
			}
		} catch (IOException e) {
			System.out.println("WARNING: Unable to open PDF Parser. " + e.getMessage());
		} catch (Exception e) {
			System.out.println("WARNING: An exception occured in parsing the PDF Document." + e.getMessage());
		} finally {
			try {
				if (cosDoc != null) {
					cosDoc.close();
				}
				if (pdDoc != null) {
					pdDoc.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return parsedText;
	}

	@Override
	public final Set<String> getKeywordsAboutArticles(IArticleStore writer) {
		Set<String> keywords = new HashSet<String>();
		try {
			Document doc = Jsoup.connect("http://tojde.anadolu.edu.tr/past-issues.html").get();
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				if (link.text().contains("Volume")) {
					doc = Jsoup.connect(link.attr("abs:href")).get(); // Volume Page
					Elements articleHtmlLinks = doc.select("a[href]");
					for (int k = 0; k < articleHtmlLinks.size(); k++) {
						Element articleHtmlLink = articleHtmlLinks.get(k);
						if (articleHtmlLink.attr("abs:href").contains("makale_goster")
								&& !articleHtmlLinks.get(k - 1).text().contains("From The Editor")) {
							doc = Jsoup.connect(articleHtmlLink.attr("abs:href")).get();
							Elements keywordList = doc.select("p[align^=left]");
							Elements volumeLinks = doc.select("a[href]");
							String filename = generateFileName(link.text(), doc.select("h4").text());
							volumeLinksMap.put(filename, volumeLinks);
							for (int i = 0; i < keywordList.size(); i++) {
								Element keywordEl = keywordList.get(i);
								String keywordStr = keywordEl.text();
								keywordStr = keywordStr.replaceAll("KEYWORDS: ", "");
								writer.storeArticle("keywords/" + filename, keywordStr);
								keywords.add(keywordStr);
							}
						}

					}
				}
				// if (keywords.size() > 3) { // FIXME remove on production
				// break;
				// }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keywords;
	}
}
