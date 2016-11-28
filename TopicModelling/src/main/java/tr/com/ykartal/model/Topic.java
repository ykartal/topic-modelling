package tr.com.ykartal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Topic {

  public static Map<Integer, String> topicIdNamesMap = new TreeMap<Integer, String>();
  public static Map<String, String> articleNamesUrlMap = new TreeMap<String, String>();
  public static Map<String, String> articleNamesHeaderMap = new TreeMap<String, String>();
  private int topicId;
  private String topic;
  private float totalPercent;
  private int count = 0;
  private final List<Document> files = new ArrayList<Document>();

  public Topic(int topicId) {
    this.topicId = topicId;
    this.topic = topicIdNamesMap.get(topicId);
  }

  public int getTopicId() {
    return topicId;
  }

  public void setTopicId(int topicId) {
    this.topicId = topicId;
    this.topic = topicIdNamesMap.get(topicId);
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public float getPercent() {
    if (count == 0) {
      return 0;
    }
    return totalPercent * 100 / count;
  }

  public void addPercent(float percent, String filePath) {
    if (percent > 0.05) {
      this.totalPercent += percent;
      String filename = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
      getFiles().add(new Document(filePath, percent, articleNamesUrlMap.get(filename),
          articleNamesHeaderMap.get(filename)));
    }
    count++;
  }

  public static void crawlLine(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, "\t");
    while (tokenizer.hasMoreElements()) {
      String id = (String) tokenizer.nextElement(); // id
      tokenizer.nextElement(); // �uanda �nemli de�il
      String topic = (String) tokenizer.nextElement();
      topic = topic.replaceAll("\t", ""); // tab varsa temizliyorum.
      topicIdNamesMap.put(Integer.parseInt(id), topic);
    }
  }

  public List<Document> getFiles() {
    return files;
  }

}
