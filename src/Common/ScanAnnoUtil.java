package Common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
		/*
		for (Method method : BookAction.class.getMethods())
		{
		    if (method.isAnnotationPresent(Mark.class))
		    {
		        System.out.println(method.getName());
		    }
		}
		*/
		Iterable<Class> classes = this.getClasses("action");
		classes.forEach(c->{
			for (Method method : c.getMethods()) {
				if (method.isAnnotationPresent(Mark.class))
			    {
					System.out.println("==>"+method.getName());
					for (Parameter parameter : method.getParameters()){
						System.out.println(parameter.getType());
					}
			        
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
	        System.out.println(resource);
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
	
}
