package tr.com.ykartal.store;

import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import tr.com.ykartal.crawler.IArticleStore;

public interface IArticleGetter extends IArticleStore {
    List<S3ObjectSummary> getArticleList();

    String getArticle(String key);
}
