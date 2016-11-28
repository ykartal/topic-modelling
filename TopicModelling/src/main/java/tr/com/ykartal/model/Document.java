package tr.com.ykartal.model;

public class Document implements Comparable<Document> {

  private String docPath;
  private float percent;
  private String urlAddress;
  private String header;

  public Document(String docPath, float percent, String urlAddress, String header) {
    this.docPath = docPath;
    this.percent = percent;
    this.urlAddress = urlAddress;
    this.setHeader(header);
  }

  public String getDocPath() {
    return docPath;
  }

  public void setDocPath(String docPath) {
    this.docPath = docPath;
  }

  public float getPercent() {
    return percent;
  }

  public void setPercent(float percent) {
    this.percent = percent;
  }

  public String getUrlAddress() {
    return urlAddress;
  }

  public void setUrlAddress(String urlAddress) {
    this.urlAddress = urlAddress;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public int compareTo(Document arg0) {
    return arg0.percent == this.percent ? 0 : (arg0.percent > this.percent ? 1 : -1);
  }

}
