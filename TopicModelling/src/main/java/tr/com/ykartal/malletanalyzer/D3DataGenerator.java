package tr.com.ykartal.malletanalyzer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import tr.com.ykartal.io.FileReader;
import tr.com.ykartal.io.FileWriter;
import tr.com.ykartal.model.Topic;
import tr.com.ykartal.model.Year;

public class D3DataGenerator {

  public static void generate(String projectRoot, String projectName) throws IOException {
    readTopicUrls(projectRoot + "/" + projectName + "/article_links.txt");
    readTopicNames(projectRoot + "/" + projectName + "/keyreplaced_keys.txt");
    readTopicData(projectRoot + "/" + projectName + "/keyreplaced_composition.txt");

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("date");
    for (int id : Topic.topicIdNamesMap.keySet()) {
      stringBuilder.append("\t" + Topic.topicIdNamesMap.get(id));
      // System.out.print("\t" + id);
    }
    stringBuilder.append(System.lineSeparator());

    for (Year year : Year.getYearList()) {
      Map<Integer, Topic> topics = year.getTopiclist();
      String line = year.getYear() + "";
      for (Topic topic : topics.values()) {
        line += "\t" + topic.getPercent();
        Collections.sort(topic.getFiles());
      }
      stringBuilder.append(line);
      stringBuilder.append(System.lineSeparator());
    }
    (new FileWriter(projectRoot + "/" + projectName)).storeArticle("charts/data.tsv",
        stringBuilder.toString());

    Year.exportArticleYearCount(projectRoot + "/" + projectName);
    Year.exportTopicYearCount(projectRoot + "/" + projectName);
    Year.exportTopicsHtmlPages(projectRoot + "/" + projectName);

  }

  private static void readTopicNames(String path) {
    List<String> lines = FileReader.readLines(path);
    for (String line : lines) {
      Topic.crawlLine(line);
    }
  }

  public static void readTopicData(String path) {
    List<String> lines = FileReader.readLines(path);
    lines = lines.subList(1, lines.size());// ilk sat�r anlams�z oldu�u i�in
    // i�leme al�nmaz
    for (String line : lines) {
      Year.crawlLine(line);

    }
  }

  private static void readTopicUrls(String path) {
    List<String> lines = FileReader.readLines(path);
    for (String line : lines) {
      StringTokenizer tokenizer = new StringTokenizer(line, "\t");
      String url = tokenizer.nextToken(); // url
      String file = tokenizer.nextToken() + ".txt"; // file
      String header = tokenizer.nextToken(); // header
      Topic.articleNamesUrlMap.put(file, url);
      Topic.articleNamesHeaderMap.put(file, header);
    }

  }

}
