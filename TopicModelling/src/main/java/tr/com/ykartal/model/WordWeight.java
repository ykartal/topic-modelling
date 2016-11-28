package tr.com.ykartal.model;

import java.util.Locale;

public class WordWeight implements Comparable<WordWeight> {

  private String topic;
  private float weight;
  private String word;

  public WordWeight(String topic, float weight, String word) {
    this.topic = topic;
    this.weight = weight;
    this.word = word;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int compareTo(WordWeight arg0) {
    return this.weight < arg0.weight ? 1 : (this.weight == arg0.weight ? 0 : -1);
  }

  @Override
  public String toString() {
    return word + "," + String.format(Locale.US, "%.2f", weight) + "," + topic;
  }

}
