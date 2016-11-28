package tr.com.ykartal.malletanalyzer.wordcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tr.com.ykartal.io.FileReader;
import tr.com.ykartal.io.FileWriter;
import tr.com.ykartal.model.WordWeight;

public class WordCloudCalculater {

  public static Map<String, List<WordWeight>> generate(String sourcePath, String projectName) {
    List<String> lines = FileReader
        .readLines(sourcePath + "/" + projectName + "/keyreplaced-wordweights.txt");
    Map<String, List<WordWeight>> topics = new TreeMap<String, List<WordWeight>>();
    for (String line : lines) {
      String[] arr = line.split("\\t");
      WordWeight wordWeight = new WordWeight(arr[0], Float.parseFloat(arr[2]), arr[1]);
      List<WordWeight> list = topics.get(arr[0]);
      if (list == null) {
        list = new ArrayList<WordWeight>();
        topics.put(arr[0], list);
      }
      list.add(wordWeight);
    }

    for (String key : topics.keySet()) {
      List<WordWeight> list = topics.get(key);
      Collections.sort(list);
      String content = "text,size,topic\n";
      for (int i = 0; i < 25 && i < list.size(); i++) {
        WordWeight wordWeight = list.get(i);
        content += (wordWeight.toString()) + "\n";
      }
      (new FileWriter(sourcePath + "/" + projectName))
          .storeArticle("/charts/wordcloud/top/" + key + ".txt", content);
    }
    return topics;
  }
}
