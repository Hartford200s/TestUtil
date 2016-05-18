package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import action.BookAction;
import anno.Mark;

public class ScanAnnoUtil {
	
	
	private static ScanAnnoUtil scanAnnoUtil = new ScanAnnoUtil();
	
	private ScanAnnoUtil() {
		
	}
	
	public static ScanAnnoUtil getInstance() {
		return scanAnnoUtil;
	}
	
	public void doScan() throws Exception {
		Properties scanProperties = this.getScanProperties();
		String packageName = scanProperties.getProperty("scanPackageName");
		Iterable<Class> classes = this.getClasses(packageName);
		classes.forEach(c->{
			for (Method method : c.getMethods()) {
				if (method.isAnnotationPresent(Mark.class))
			    {
					String actionURL = method.getAnnotation(Mark.class).actionURL();
					String requestMethod = method.getAnnotation(Mark.class).requestMethod().toString();
					/*
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMethod[] rm = method.getAnnotation(RequestMapping.class).method();
						if (rm.length > 0) {
							String m = rm[0].toString();
							System.out.println(m);
						}
					}
					*/
					new BuildJmeterXmlUtil(scanProperties, method.getName(), actionURL, method.getParameters(), requestMethod).buildXml();
			    }
			}
		});
	}
	
	private Iterable<Class> getClasses(String packageName) throws ClassNotFoundException, IOException
	{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements())
	    {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    List<Class> classes = new ArrayList<Class>();
	    for (File directory : dirs)
	    {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes;
	}
	
	private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
	{
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists())
	    {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files)
	    {
	        if (file.isDirectory())
	        {
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        }
	        else if (file.getName().endsWith(".class"))
	        {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
	
	private Properties getScanProperties() {
		Properties scanProperties = new Properties();
		File f = new File("src/resources/scanMarkAnnotation.properties");
		try {
			scanProperties.load(new FileInputStream(f.getCanonicalPath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scanProperties;
	}
}
