package tr.com.ykartal.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import tr.com.ykartal.textoperation.KeywordReplacer;

/**
 * An example to FileWriter which implements IFileWriter.
 * 
 * @author Yusuf KARTAL
 *
 */
public class CloudStore implements IArticleGetter {

    /**
     * S3 storage to store articles.
     */
    private static AmazonS3 s3 = new AmazonS3Client();

    /**
     * Unique bucket name for each article archive.
     */
    private String bucketName;

    /**
     * keyword replacer to scan article and replace multi word keywords' spaces with underline.
     */
    private KeywordReplacer keywordReplacer;

    /**
     * 
     * CloudStore stores article ona S3 Storage Server.
     * 
     * @param grupName
     *            article archive name to group them in a bucket
     * @param keywords
     *            keyword set to scan article and replace multi word keywords' spaces with underline
     */
    public CloudStore(final String grupName, final Set<String> keywords) {
        bucketName = createBucketName(grupName);
        keywordReplacer = new KeywordReplacer(keywords);
        Region region = Region.getRegion(Regions.AP_NORTHEAST_1);
        s3.setRegion(region);
        s3.createBucket(bucketName);
    }

    private static String createBucketName(String grupName) {
        String name = createBucketNamePrefix(grupName);
        name += "-" + UUID.randomUUID(); // +37
        return name; // name must be max 63
    }

    private static String createBucketNamePrefix(String grupName) {
        String name = grupName.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
        name = name.substring(0, Math.min(name.length(), 26));
        return name;
    }

    public static boolean isBucketNameExists(String grupName) {
        for (Bucket bucket : s3.listBuckets()) {
            if (bucket.getName().startsWith(createBucketNamePrefix(grupName))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void storeArticle(final String key, final String pContent) {
        try {
            File file = new File(key);
            file = new File(file.getAbsolutePath());
            makeDir(file.getParentFile());
            PrintWriter writer = new PrintWriter(key, "UTF-8");
            String content = keywordReplacer.replaceKeywords(pContent); // replace keywords
            writer.print(content);
            writer.flush();
            writer.close();
            s3.putObject(new PutObjectRequest(bucketName, key, file));
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final List<S3ObjectSummary> getArticleList() {
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        return objectListing.getObjectSummaries();
    }

    /**
     * Creates given file's all parent folders.
     * 
     * @param folder
     *            file whose folder will create
     * @return true if folders created false otherwise
     */
    private boolean makeDir(final File folder) {
        if (folder.exists()) {
            return true;
        }
        if (folder.mkdir()) {
            return true;
        } else {
            makeDir(folder.getParentFile());
            return makeDir(folder);
        }
    }

    @Override
    public final String getArticle(final String key) {
        S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
        try {
            return getTextFromInputStream(object.getObjectContent());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read InputStream to get String.
     * 
     * @param input
     *            {@link InputStream} to read
     * @return {@link InputStream} content as String
     * @throws IOException
     *             exception about any read error
     */
    private String getTextFromInputStream(final InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}
