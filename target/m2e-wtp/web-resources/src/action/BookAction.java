package action;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import Common.Util;
import model.Book;
import service.BookService;

@Controller("BookAction")
@Scope("prototype")
public class BookAction extends ActionSupport implements ServletRequestAware{
	
	@Resource(name = "BookService")
	private BookService bookService;
	
	private List<Book> bookList;
	private Book book;
	private File uploadImg;
	private String uploadImgFileName;
	protected HttpServletRequest request;
	private String uploadPath;
	private Map<String, String> resultMap = new HashMap<String, String>(3);
	private String bookName;
	private BigDecimal bookPrice;
	private Long id;
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public String queryBook() {
		try {
			bookList = bookService.queryList(bookName, bookPrice);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SUCCESS;
		}
	}
	
	public String doCreate() {
		try {
			if (uploadImg != null) {
				String path = request.getServletContext().getRealPath(Util.uploadPath);
				File folder = new File(path);
				if (!folder.exists() || !folder.isDirectory()) {
					folder.mkdir();
				}
				String fileName = String.valueOf(System.currentTimeMillis());
				FileUtils.copyFile(uploadImg, new File(folder,fileName));
				String imgUrl = Util.uploadPath + "\\" + fileName;
				book.setBookImage(imgUrl);
			}
			bookService.insert(book);
			addActionMessage("新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String checkBookName() {
		try {
			if (bookName != null) {
				bookName = bookName.trim();
			}
			List<Book> list = bookService.searchBook(bookName);
			if (list != null && !list.isEmpty()) {
				this.resultMap.put("Result", "exist");
			} else {
				this.resultMap.put("Result", "not exist");
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SUCCESS;
		}
	}
	
	public String goUpdate() {
		try {
			book = bookService.get(id);
			return SUCCESS;
		} catch (Exception e){
			e.printStackTrace();
			return SUCCESS;
		}
	}
	
	public String doUpdate(){
		try {
			Book dbBook = bookService.get(book.getId());
			dbBook.setBookName(book.getBookName());
			dbBook.setBookPrice(book.getBookPrice());
			if (uploadImg != null && StringUtils.isNotBlank(uploadImgFileName)) {
				String path = request.getServletContext().getRealPath(Util.uploadPath);
				File folder = new File(path);
				if (!folder.exists() || !folder.isDirectory()) {
					folder.mkdir();
				}
				String fileName = String.valueOf(System.currentTimeMillis());
				FileUtils.copyFile(uploadImg, new File(folder,fileName));
				String imgUrl = Util.uploadPath + "\\" + fileName;
				dbBook.setBookImage(imgUrl);
			}
			
			bookService.update(dbBook);
			addActionMessage("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String doDelete() {
		try {
			bookService.delete(id);
			addActionMessage("刪除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public File getUploadImg() {
		return uploadImg;
	}

	public void setUploadImg(File uploadImg) {
		this.uploadImg = uploadImg;
	}

	public String getUploadImgFileName() {
		return uploadImgFileName;
	}

	public void setUploadImgFileName(String uploadImgFileName) {
		this.uploadImgFileName = uploadImgFileName;
	}

	public Map<String, String> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
