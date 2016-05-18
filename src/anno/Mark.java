package anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mark {
	
	/**
	 * 程式進入URL
	 * ex:127.0.0.1:8080/Test/query.action
	 * actionURL = "query.action"
	 * 
	 * ex:127.0.0.1:8080/Test/student/query.action
	 * actionURL = "/student/query.action"
	 * @return
	 */
	String actionURL();
	
	/**
	 * 傳遞參數方式
	 * 目前只要不是POST方式傳遞,皆為GET
	 * @return
	 */
	RequestMethod  requestMethod();
	
}
