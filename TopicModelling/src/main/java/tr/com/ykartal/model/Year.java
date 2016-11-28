package tr.com.ykartal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import tr.com.ykartal.io.FileWriter;

public class Year {

  private final int year;
  private Map<Integer, Topic> topiclist = new TreeMap<Integer, Topic>();
  private static List<Year> yearList = new ArrayList<Year>();
  public static Map<Integer, Integer> yearArticleCountMap = new TreeMap<Integer, Integer>();

  private Year(int year) {
    this.year = year;
  }

  public static Year crawlLine(String line) {
    Year year = takeYearFrom(line);
    year.crawlTopicsFrom(line);
    return year;

  }

  public int getYear() {
    return year;
  }

  private void crawlTopicsFrom(String pData) {
    int lastIndex = (pData.indexOf(".txt") < 0
        ? (pData.indexOf(".html") < 0 ? pData.indexOf(".htm") + 4 : pData.indexOf(".html") + 5)
        : pData.indexOf(".txt") + 4);
    String data = pData.substring(lastIndex);
    StringTokenizer tokenizer = new StringTokenizer(data, "\t");
    int topicId = 0;
    while (tokenizer.hasMoreElements()) {
      String token = (String) tokenizer.nextElement();
      float topicPercent = Float.parseFloat(token);
      Topic topic = getTopic(topicId);
      String filePath = (pData.split("\t"))[1].substring(6);
      topic.addPercent(topicPercent, filePath);
      topicId++;
    }
  }

  private Topic getTopic(int topicId) {
    Topic topic = topiclist.get(topicId);
    if (topic == null) {
      topic = new Topic(topicId);
      getTopiclist().put(topicId, topic);
    }
    return topic;
  }

  private static Year takeYearFrom(String data) {
    int lastIndex = data.lastIndexOf("/");
    String year = data.substring(lastIndex + 1, lastIndex + 5);

    Year foundYear = null;
    int yearInt = Integer.parseInt(year);
    for (Year eachYear : getYearList()) {
      if (eachYear.getYear() == yearInt) {
        foundYear = eachYear;
        yearArticleCountMap.put(yearInt, yearArticleCountMap.get(yearInt) + 1);
        break;
      }
    }
    if (foundYear == null) {
      yearArticleCountMap.put(yearInt, 1);
      foundYear = new Year(yearInt);
      getYearList().add(foundYear);
    }
    yearList.sort((o1, o2) -> o1.getYear() - o2.getYear());
    return foundYear;

  }

  public static List<Year> getYearList() {
    return yearList;
  }

  public Map<Integer, Topic> getTopiclist() {
    return topiclist;
  }

  public static void exportArticleYearCount(String sourcePath) {
    String content = "date\tarticle_count\n";
    for (Year year : yearList) {
      content += year.getYear() + "\t" + Year.yearArticleCountMap.get(year.getYear()) + "\n";
    }
    (new FileWriter(sourcePath)).storeArticle("charts/article-year-charts/data.tsv", content);
  }

  public static void exportTopicYearCount(String sourcePath) {
    String content = "date\ttopic_count\n";

    for (Year year : yearList) {
      int count = 0;
      for (Topic topic : year.getTopiclist().values()) {
        if (topic.getPercent() > 0) {
          count++;
        }
      }
      content += year.getYear() + "\t" + count + "\n";
    }
    (new FileWriter(sourcePath)).storeArticle("charts/topic-charts/data.tsv", content);

  }

  public static void exportTopicsHtmlPages(String path) {
    for (Year year : yearList) {
      for (Topic topic : year.getTopiclist().values()) {
        String filename = "topic" + topic.getTopicId() + "/" + year.getYear() + ".html";
        String content = topic.getTopic() + "<br />";
        for (Document document : topic.getFiles()) {
          content += "<a href=\"" + document.getUrlAddress() + "\">" + document.getHeader()
              + "</a>(" + document.getPercent() + ")<br />";
        }
        (new FileWriter(path)).storeArticle("charts/html/" + filename, content);
      }
    }
  }

}
