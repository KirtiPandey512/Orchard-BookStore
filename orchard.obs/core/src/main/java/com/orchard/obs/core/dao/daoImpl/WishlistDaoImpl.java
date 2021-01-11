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
import com.orchard.obs.Exceptions.daoexceptions.WishlistDaoException;
import com.orchard.obs.core.dao.WishlistDao;
import com.orchard.obs.core.entity.Book;
import com.orchard.obs.core.entity.Cart;
import com.orchard.obs.core.util.DBUtil;

/**
 * @author Rushabh
 *
 */
@Component(immediate =  true, service = WishlistDao.class)
public class WishlistDaoImpl implements WishlistDao {

	@Reference
	DBUtil dbUtil;
	
	@Override
	public List<Cart> getWishlistDetails(String dataSourceName, String customerId) throws WishlistDaoException {
		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		List<Cart> wishlistItems = new ArrayList<Cart>();

		try {
			connection = dbUtil.getConnection(dataSourceName);
			statement = connection.createStatement();
			statement1 = connection.createStatement();
			resultSet = statement.executeQuery("SELECT B.BOOKID, B.NAME, P.PUBLISHERNAME, B.PAGECOUNT, B.EDITION, B.LANGUAGE, B.QUANTITY, B.PRICE, B.DISCOUNT, G.GENRENAME FROM WishList W JOIN BOOK B ON W.BOOKID = B.BOOKID INNER JOIN PUBLISHER P ON P.PUBLISHERID = B.PUBLISHERID INNER JOIN GENRE G ON G.GENREID = B.GENREID AND W.CUSTOMER_ID = '" + customerId + "';");

			while (resultSet.next()) {
				Cart cart = new Cart();
				Book book = new Book();
				//Populate Employee object with data from MySQL
				cart.setCustomerId(customerId);
				
				book.setId(resultSet.getString(1));
				book.setName(resultSet.getString(2)); 
				book.setPublisher(resultSet.getString(3));
				book.setPageCount(resultSet.getInt(4));
				book.setEdition(resultSet.getString(5));
				book.setLanguage(resultSet.getString(6));
				book.setQuantity(resultSet.getInt(7));
				book.setPrice(resultSet.getInt(8));
				book.setDiscount(resultSet.getInt(9));
				book.setGenre(resultSet.getString(10));
				resultSet1 = statement1.executeQuery("SELECT * FROM CART WHERE BOOKID = '" + book.getId() + "' AND CUSTOMER_ID = '" + customerId + "';");
				if (resultSet1.next())
					book.setPresentInCart(true);
				
				cart.setBook(book);
				cart.setCartQuantity(1);
				
				//Push the Employee Object to the list
				wishlistItems.add(cart);
			} 	
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new WishlistDaoException(e);
		}
		finally {
			dbUtil.closeResource(statement);
			dbUtil.closeResource(connection);
		}
		return wishlistItems;
	}

	@Override
	public int addBookToCart(String dataSourceName, String bookId, String customerId) throws WishlistDaoException {
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
			throw new WishlistDaoException(e.getCause());
		}
		finally {
			dbUtil.closeResource(resultSet);
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
	}

	@Override
	public int deleteWishlistItem(String dataSourceName, String bookId, String customerId) throws WishlistDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int status = 0;

		try {
			connection = dbUtil.getConnection(dataSourceName);
			preparedStatement = connection.prepareStatement("DELETE FROM WishList WHERE BOOKID = ? AND CUSTOMER_ID = ?;");
			preparedStatement.setString(1, bookId);
			preparedStatement.setString(2, customerId);
			status = preparedStatement.executeUpdate();
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new WishlistDaoException(e);
		}
		finally {
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
		return status;
	}

}
