package tr.com.ykartal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import tr.com.ykartal.crawler.IArticleStore;

public class FileWriter implements IArticleStore {

  private String path;

  public FileWriter(String path) {
    this.path = path;
  }

  @Override
  public void storeArticle(String outputFilePath, String pContent) {
    File file = new File(path + "/" + outputFilePath);
    file = new File(file.getAbsolutePath());
    makeDir(file.getParentFile());
    try {
      PrintWriter writer = new PrintWriter(path + "/" + outputFilePath, "UTF-8");
      String content = pContent.replace('ı', 'i');
      content = content.replace('ş', 's');
      content = content.replace('ç', 'c');
      content = content.replace('ğ', 'g');
      content = content.replace('ö', 'o');
      content = content.replace('ü', 'u');
      writer.print(content);
      writer.flush();
      writer.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static boolean makeDir(File parentFile) {
    if (parentFile.exists()) {
      return true;
    }
    if (parentFile.mkdir()) {
      return true;
    } else {
      makeDir(parentFile.getParentFile());
      return makeDir(parentFile);
    }
  }

}
