/**
 * 
 */
package com.orchard.obs.core.dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.orchard.obs.Exceptions.daoexceptions.BookDaoException;
import com.orchard.obs.core.dao.BookDao;
import com.orchard.obs.core.entity.Book;
import com.orchard.obs.core.util.DBUtil;

/**
 * @author Rushabh
 *
 */
@Component(immediate =  true, service = BookDao.class)
public class BookDaoImpl implements BookDao {

	@Reference
	DBUtil dbUtil;
	
	@Override
	public Book getBookDetails(String dataSourceName, String bookId, String customerId) throws BookDaoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dbUtil.getConnection(dataSourceName);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT B.BOOKID, B.NAME, P.PUBLISHERNAME, B.PAGECOUNT, B.EDITION, B.LANGUAGE, B.QUANTITY, B.PRICE, B.DISCOUNT, B.DESCRIPTION, G.GENRENAME FROM BOOK B INNER JOIN PUBLISHER P ON P.PUBLISHERID = B.PUBLISHERID INNER JOIN GENRE G ON G.GENREID = B.GENREID AND B.BOOKID = '" + bookId + "';");
			
			Book book = null;
			if (resultSet.next()) {
				book = new Book();
				book.setId(resultSet.getString(1));
				book.setName(resultSet.getString(2)); 
				book.setPublisher(resultSet.getString(3));
				book.setPageCount(resultSet.getInt(4));
				book.setEdition(resultSet.getString(5));
				book.setLanguage(resultSet.getString(6));
				book.setQuantity(resultSet.getInt(7));
				book.setPrice(resultSet.getInt(8));
				book.setDiscount(resultSet.getInt(9));
				book.setDescription(resultSet.getString(10));
				book.setGenre(resultSet.getString(11));
				
				resultSet = statement.executeQuery("SELECT A.AUTHORNAME FROM AUTHOR A INNER JOIN AUTHOR_BOOK AB ON AB.BOOKID = '" + bookId + "' AND AB.AUTHORID = A.AUTHORID;");
				List<String> authors = new ArrayList<String>();
				while (resultSet.next()) {
					authors.add(resultSet.getString(1));
				}
				book.setAuthor(authors.toArray(new String[authors.size()]));
				
				
				resultSet = statement.executeQuery("SELECT ISBN FROM Book_ISBN WHERE BOOKID = '" + bookId + "';");
				List<Long> isbns = new ArrayList<Long>();
				while (resultSet.next()) {
					isbns.add(resultSet.getLong(1));
				}
				book.setIsbn(isbns.toArray(new Long[isbns.size()]));
				
				resultSet = statement.executeQuery("SELECT * FROM CART WHERE BOOKID = '" + bookId + "' AND CUSTOMER_ID = '" + customerId + "';");
				if (resultSet.next())
					book.setPresentInCart(true);
				
				resultSet = statement.executeQuery("SELECT * FROM WISHLIST WHERE BOOKID = '" + bookId + "' AND CUSTOMER_ID = '" + customerId + "';");
				if (resultSet.next())
					book.setPresentInWishlist(true);
			} 	
			return book;
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new BookDaoException(e.getCause());
		}
		finally {
			dbUtil.closeResource(resultSet);
			dbUtil.closeResource(statement);
			dbUtil.closeResource(connection);
		}
	}

	@Override
	public int addBookToCart(String dataSourceName, String bookId, String customerId) throws BookDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dbUtil.getConnection(dataSourceName);
			preparedStatement = connection.prepareStatement("INSERT INTO CART VALUES(?, ?, ?);");
			preparedStatement.setString(1, customerId);
			preparedStatement.setString(2, bookId);
			preparedStatement.setInt(3, 1);
			return preparedStatement.executeUpdate();				
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new BookDaoException(e.getCause());
		}
		finally {
			dbUtil.closeResource(resultSet);
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
	}

}
