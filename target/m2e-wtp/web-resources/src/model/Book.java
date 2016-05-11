package model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Book {
	
	
	private long id;
	private String bookName;
	private BigDecimal bookPrice;
	private String bookImage;
	private Calendar createTime;
	
	public boolean hasImage(){
		return bookImage != null && !bookImage.trim().isEmpty() && !bookImage.equalsIgnoreCase("null");
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public BigDecimal getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(BigDecimal bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookImage() {
		return bookImage;
	}
	public void setBookImage(String bookImage) {
		this.bookImage = bookImage;
	}
	public Calendar getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}
}
