/**
 * 
 */
package tr.com.ykartal.test.analyzer;

import org.junit.Test;

import tr.com.ykartal.servlet.AnalyzerServlet;

/**
 * @author Yusuf KARTAL
 *
 */
public class TestAnalyzer {

    @Test
    public void test() {
        new AnalyzerServlet().analyze("crawler.tojde-0.0.1-jar-with-dependencies.jar");
    }

}
