package tr.com.ykartal.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

  public static String readContent(String path) {
    String result = "";
    BufferedReader br = null;
    try {
      br = new BufferedReader(new java.io.FileReader(path));
      String line;
      while ((line = br.readLine()) != null) {
        line = line.replace('�', 'i');
        line = line.replace('�', 's');
        line = line.replace('�', 'c');
        line = line.replace('�', 'g');
        line = line.replace('�', 'o');
        line = line.replace('�', 'u');
        result += line;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public static List<String> readLines(String path) {
    List<String> lines = new ArrayList<String>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new java.io.FileReader(path));
      String line = "";
      while ((line = br.readLine()) != null) {
        line = line.replace('�', 'i');
        line = line.replace('�', 's');
        line = line.replace('�', 'c');
        line = line.replace('�', 'g');
        line = line.replace('�', 'o');
        line = line.replace('�', 'u');
        lines.add(line);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return lines;
  }

}
