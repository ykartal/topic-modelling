package tr.com.ykartal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import tr.com.ykartal.crawler.IArticleStore;
import tr.com.ykartal.crawler.ICrawler;
import tr.com.ykartal.crawler.tojde.TojdeCrawler;
import tr.com.ykartal.io.FileWriter;
import tr.com.ykartal.keyword.KeywordReplacer;
import tr.com.ykartal.malletanalyzer.D3DataGenerator;
import tr.com.ykartal.malletanalyzer.wordcloud.WordCloudCalculater;
import tr.com.ykartal.stemmer.Stemmer;

public class TopicModelling {

  public void runLDA(String[] args) {
    String projectTextsPath = args[0] + "/" + args[1] + "/keyreplaced";
    runShellScript("./scripts/runImportDir.sh", projectTextsPath);
    System.out.println("Article Directory imported");
    runShellScript("./scripts/runLDA.sh", projectTextsPath, args[2]);
    System.out.println("LDA output generated.");
  }

  public void runShellScript(String... commands) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(commands);

      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();

      try (BufferedReader processOutputReader = new BufferedReader(
          new InputStreamReader(process.getInputStream()));) {
        String readLine;

        while ((readLine = processOutputReader.readLine()) != null) {
          System.out.println(readLine + System.lineSeparator());
        }

        process.waitFor();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void generateCharts(String[] args) throws IOException {
    // generate data graphics
    D3DataGenerator.generate(args[0], args[1]);

    // generate word cloud data
    WordCloudCalculater.generate(args[0], args[1]);

    copyChartsTemplates(args);

    System.out.println("Chart Data Generated.");
  }

  public void copyChartsTemplates(String[] args) {
    try {
      Path source = Paths.get("charts-template");
      Path destination = Paths.get(args[0] + "/" + args[1] + "/charts/");

      List<Path> sources = Files.walk(source).collect(Collectors.toList());
      List<Path> destinations = sources.stream().map(source::relativize).map(destination::resolve)
          .collect(Collectors.toList());

      for (int i = 0; i < sources.size(); i++) {
        try {
          Files.copy(sources.get(i), destinations.get(i), StandardCopyOption.REPLACE_EXISTING);
        } catch (DirectoryNotEmptyException e) {
          // Nothing to do
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void prepare(String[] args) throws IOException {
    String projectTextsPath = args[0] + "/" + args[1];
    // Replace keywords
    String dataFiles = projectTextsPath + "/keyreplaced";
    KeywordReplacer.replaceKeywords(projectTextsPath + "/articles", projectTextsPath + "/keywords",
        dataFiles);
    System.out.println("Keyword replacement done.");
    // Stemming
    File keyFolder = new File(projectTextsPath + "/keyreplaced");
    File keyFolderTemp = new File(projectTextsPath + "_before_stemmer");
    keyFolder.renameTo(keyFolderTemp);
    Stemmer.runStemmer(projectTextsPath + "_before_stemmer", projectTextsPath + "/keyreplaced");
    System.out.println("Stemming done.");
    FileUtils.deleteDirectory(keyFolderTemp);

  }

  public void detectTopics(String[] args) throws IOException {
    ICrawler crawler = new TojdeCrawler();
    String projectTextsPath = args[0] + "/" + args[1];
    IArticleStore fileWriter = new FileWriter(projectTextsPath);
    crawler.getKeywordsAboutArticles(fileWriter);
    System.out.println("Keywords collected");
    crawler.getArticles(fileWriter);
    System.out.println("Articles collected");
    prepare(args);
    runLDA(args);
    generateCharts(args);
  }

}
