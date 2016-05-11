package dao;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.Book;

@Transactional
@Repository("BookDAO")
@Scope("prototype")
public class BookDAOImp implements BookDAO{
	
	@Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
	
	public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Book> list(String bookName, BigDecimal bookPrice) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Book WHERE 1 = 1 ");
		if (StringUtils.isNotBlank(bookName)) {
			hql.append(" AND bookName like :bookName ");
		}
		if (bookPrice != null) {
			hql.append(" AND bookPrice = :bookPrice ");
		}
		Query query = getSession().createQuery(hql.toString());
		if (StringUtils.isNotBlank(bookName)) {
			query.setString("bookName", "%" + bookName + "%");
		}
		if (bookPrice != null) {
			query.setBigDecimal("bookPrice", bookPrice);
		}
		return query.list();
	}

	@Override
	public void insert(Book book) {
		getSession().save(book);
	}
	
	@Override
	public List<Book> searchBook(String bookName) {
		String hql = "FROM Book WHERE bookName = :bookName ";
		Query query = getSession().createQuery(hql);
		query.setParameter("bookName", bookName);
		return query.list();
	}
	
	@Override
	public Book get(Long id) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Book where id=:id");
        query.setLong("id", id);
        return (Book) query.list().get(0);
	}
	
	@Override
	public void update(Book book) {
		sessionFactory.getCurrentSession().update(book);
	}
	
	@Override
	public void delete(Long id) {
		Book book = get(id);
		sessionFactory.getCurrentSession().delete(book);
	}
}
