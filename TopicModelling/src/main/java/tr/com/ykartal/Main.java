package tr.com.ykartal;

import java.io.IOException;

/**
 * Start point of the application.
 * 
 * @author ykartal
 */
public final class Main {

  /**
   * This class will not be have a constructor.
   */
  private Main() {
  }

  /**
   * Start point of app.
   * 
   * @param args
   *          application parameters
   * @throws IOException
   *           will throw out of app
   */
  public static void main(final String[] args) throws IOException {

    if (args.length == 0) {
      System.out.println("Usage:");
      System.out.println();
      System.out.println(
          "To run all operations (download to <input_path>, clean, prepare, stem input data and generate output and charts:");
      System.out.println("<input_path> <project_name> <topic_count>");
      return;
    }

    TopicModelling topicModelling = new TopicModelling();
    topicModelling.detectTopics(args);

  }

}
