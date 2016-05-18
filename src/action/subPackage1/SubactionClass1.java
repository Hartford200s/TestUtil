package action.subPackage1;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestMethod;

import anno.Mark;
import model.Book;

public class SubactionClass1 {
	
	@Mark(actionURL = "goSearch.action", requestMethod = RequestMethod.GET)
	public void goSearchTest(){}
	
	@Mark(actionURL = "queryBook.action", requestMethod = RequestMethod.POST)
	public void queryBookTest(String bookName, BigDecimal bookPrice){}
	
	@Mark(actionURL = "doCreate.action", requestMethod = RequestMethod.POST)
	//public void doCreateTest(String bookName, BigDecimal bookPrice){}
	public void doCreateTest(Book book){}
}
