package service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import dao.BookDAO;
import model.Book;

@Service("BookService")
@Scope("prototype")
public class BookServiceImp implements BookService{
	
	@Resource(name="BookDAO")
	private BookDAO bookDAO;
	
	@Override
	public List<Book> queryList(String bookName, BigDecimal bookPrice) {
		return bookDAO.list(bookName, bookPrice);
	}

	@Override
	public void insert(Book book) {
		book.setCreateTime(Calendar.getInstance());
		bookDAO.insert(book);
	}
	
	@Override
	public List<Book> searchBook(String bookName) {
		return bookDAO.searchBook(bookName);
	}
	
	@Override
	public Book get(Long id){
		return bookDAO.get(id);
	}
	
	@Override
	public void update(Book book){
		bookDAO.update(book);
	}
	
	@Override
	public void delete(Long id) {
		bookDAO.delete(id);
	}
}
