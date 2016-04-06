package tr.com.ykartal.servlet;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tr.com.ykartal.crawler.ICrawler;
import tr.com.ykartal.exception.WarningException;
import tr.com.ykartal.factory.CrawlerFactory;
import tr.com.ykartal.store.CloudStore;
import tr.com.ykartal.store.IArticleGetter;

/**
 * Servlet implementation class AnalyzerServlet.
 */
@WebServlet(urlPatterns = { "/AnalyzerServlet" })
public class AnalyzerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Used to create threads for background jobs.
     */
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected final void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String archiveName = req.getParameter("archive");
        // Start a thread to get articles and analyze on background
        EXECUTOR.submit(() -> this.analyze(archiveName));
    }

    public void analyze(String archiveName) {
        if (!CloudStore.isBucketNameExists(archiveName)) {
            try {
                ICrawler crawler = CrawlerFactory.getInstance(archiveName);
                Set<String> keywords = crawler.getKeywordsAboutArticles();
                IArticleGetter articleStore = new CloudStore(archiveName, keywords);
                crawler.getArticles(articleStore);
            } catch (WarningException e) {
                e.printStackTrace();
            }
        } else {
            // TODO articles already taken
        }
    }

    @Override
    public void destroy() {
        // executor.shutdownNow();
        // list of not completed task could get
        super.destroy();
    }

}
