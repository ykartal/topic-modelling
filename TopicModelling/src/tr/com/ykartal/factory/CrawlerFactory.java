package tr.com.ykartal.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import tr.com.ykartal.crawler.ICrawler;
import tr.com.ykartal.exception.WarningException;

/**
 * Factory class to load selected jar and create a {@link ICrawler} instance.
 * 
 * @author Yusuf KARTAL
 *
 */
public final class CrawlerFactory {

    /**
     * No need to create an instance of {@link CrawlerFactory}.
     */
    private CrawlerFactory() {
    }

    /**
     * Factory class method to load selected jar and create a {@link ICrawler} instance.
     * 
     * @param archiveName
     *            jar file name which is loaded and search for {@link ICrawler} implementations
     * @return {@link ICrawler} implementation
     * @throws WarningException
     *             Warnings about loaded jar and its content
     */
    public static ICrawler getInstance(final String archiveName) throws WarningException {
        URL url = CrawlerFactory.class.getResource("jar/" + archiveName);
        ClassLoader loader = URLClassLoader.newInstance(new URL[] { url }, CrawlerFactory.class.getClassLoader());
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forClassLoader(loader)).addClassLoader(loader));
        Set<Class<? extends ICrawler>> implementingTypes = reflections.getSubTypesOf(ICrawler.class);

        if (implementingTypes.size() > 1) {
            throw new WarningException("Found multiple ICrawler implementation");
        }

        for (Class<? extends ICrawler> clazz : implementingTypes) {
            Class<? extends ICrawler> runClass = clazz.asSubclass(ICrawler.class);
            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
                method.setAccessible(true);
                method.invoke(ClassLoader.getSystemClassLoader(), new Object[] { url });
                Constructor<? extends ICrawler> ctor = runClass.getConstructor();
                return ctor.newInstance();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new WarningException("ICrawler implementation could not be load");
            }
        }

        throw new WarningException("There is no implemetation class of ICrawler");
    }
}
