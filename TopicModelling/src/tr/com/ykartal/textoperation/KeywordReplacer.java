package tr.com.ykartal.textoperation;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * keyword replacer to scan article and replace multi word keywords' spaces with underline.
 * 
 * @author Yusuf KARTAL
 *
 */
public class KeywordReplacer {

    private Set<String> keywords;

    public KeywordReplacer(Set<String> pKeywords) {
        this.keywords = organizeKeywords(pKeywords);
    }

    public String replaceKeywords(String content) {
        content = clean(content);
        for (String keyword : keywords) {
            String newKeyword = keyword.replace(' ', '_');
            content = content.toLowerCase().replace(keyword, newKeyword);
        }
        content = Stemmer.runStemmer(content); // Stemmer runs here to decrease S3 IO operations
        return content;
    }

    private Set<String> organizeKeywords(Set<String> pKeywords) {
        Set<String> uniqeuKeywords = new HashSet<String>();
        for (String keywordContent : pKeywords) {
            keywordContent = clean(keywordContent);
            StringTokenizer stringTokenizer = new StringTokenizer(keywordContent, ";,");
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken().trim();
                uniqeuKeywords.add(token);
            }
        }
        return uniqeuKeywords;
    }

    private String clean(String line) {
        line = line.trim().toLowerCase();
        line = line.replace('.', ' ');
        line = line.replace(':', ' ');
        line = line.replace('ı', 'i');
        line = line.replace('ş', 's');
        line = line.replace('ç', 'c');
        line = line.replace('ğ', 'g');
        line = line.replace('ö', 'o');
        line = line.replace('ü', 'u');
        line = line.replace((char) 194, ' ');//
        line = line.replace((char) 160, ' ');//
        line = line.replace('\n', ' ');
        String temp = line;
        do {
            line = temp;
            temp = temp.replace("  ", " ");
            if (temp.length() != line.length()) {
                continue;
            }
        } while (!line.equals(temp));
        return line;
    }

}
