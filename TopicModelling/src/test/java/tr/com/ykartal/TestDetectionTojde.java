package tr.com.ykartal;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestDetectionTojde {

  @Test
  public void testCompleteTopicModelling() {
    TopicModelling topicModelling = new TopicModelling();
    String[] args = { "tojde", "tojde2016", "15" };
    try {
      topicModelling.detectTopics(args);
      Assert.assertTrue(true);
    } catch (IOException e) {
      Assert.assertTrue("Hata beklenmiyordu", false);
    }
  }

}
