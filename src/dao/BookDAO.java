package dao;

import java.math.BigDecimal;
import java.util.List;

import model.Book;

public interface BookDAO {
	
	public List<Book> list(String bookName, BigDecimal bookPrice);
	
	public void insert(Book book);
	
	public List<Book> searchBook(String bookName);
	
	public Book get(Long id);
	
	public void update(Book book);
	
	public void delete(Long id);
}
