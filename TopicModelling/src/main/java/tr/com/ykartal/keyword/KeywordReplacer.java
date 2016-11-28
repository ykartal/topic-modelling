package tr.com.ykartal.keyword;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import tr.com.ykartal.io.FileReader;
import tr.com.ykartal.io.FileWriter;

public class KeywordReplacer {

  public static void replaceKeywords(String sourceFolderPath, String keywordFolderPath,
      String destFolderPath) {

    // Read Keywords
    File keywordFolder = new File(keywordFolderPath);
    File[] keywordFiles = keywordFolder.listFiles();
    if (keywordFiles == null) {
      return;
    }
    List<String> keywords = new ArrayList<String>();
    for (File file : keywordFiles) {
      List<String> lines = FileReader.readLines(file.getAbsolutePath());
      for (String line : lines) {
        if (line.startsWith("     ")) {
          line = cleanLine(line);
          if (line.isEmpty() == false) {
            int index = keywords.size() - 1;
            keywords.set(index, keywords.get(index).concat(" " + line));
          }
        } else {
          line = cleanLine(line);
          if (line.isEmpty() == false) {
            StringTokenizer stringTokenizer = new StringTokenizer(line, ";,");
            while (stringTokenizer.hasMoreTokens()) {
              String token = stringTokenizer.nextToken();
              token = cleanLine(token);
              if (token.contains(" ")) {
                keywords.add(token);
              }
            }
          }
        }
      }
    }
    Set<String> uniqeukeywords = new HashSet<String>();
    uniqeukeywords.addAll(keywords);
    String keywordsText = "";
    for (String string : uniqeukeywords) {
      keywordsText += string + "\n";
    }
    (new FileWriter(keywordFolder.getParent())).storeArticle("/keywords.txt", keywordsText);

    File sourceFolder = new File(sourceFolderPath);
    File[] sourceFiles = sourceFolder.listFiles();
    if (sourceFiles == null) {
      return;
    }
    for (File filePath : sourceFiles) {
      String content = FileReader.readContent(filePath.getAbsolutePath());
      for (String keyword : uniqeukeywords) {
        String newKeyword = keyword.replace(' ', '_');
        content = content.toLowerCase().replace(keyword, newKeyword);
      }
      (new FileWriter(destFolderPath)).storeArticle(filePath.getName(), content);
    }

  }

  private static String cleanLine(String line) {
    line = line.replace('.', ' ');
    line = line.replace(':', ' ');
    line = line.replace('ı', 'i');
    line = line.replace('ş', 's');
    line = line.replace('ç', 'c');
    line = line.replace('ğ', 'g');
    line = line.replace('ö', 'o');
    line = line.replace('ü', 'u');
    line = line.replace((char) 194, ' ');// �
    line = line.replace((char) 160, ' ');// �
    String temp = line;
    do {
      int ilk = temp.length();
      temp = temp.replace("  ", " ");
      int son = temp.length();
      if (son == ilk) {
        break;
      }
    } while (!line.equals(temp));
    line = line.trim().toLowerCase();
    return line;
  }
}
