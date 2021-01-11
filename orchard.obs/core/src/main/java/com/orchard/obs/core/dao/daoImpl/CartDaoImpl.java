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
import com.orchard.obs.Exceptions.daoexceptions.CartDaoException;
import com.orchard.obs.core.dao.CartDao;
import com.orchard.obs.core.entity.Book;
import com.orchard.obs.core.entity.Cart;
import com.orchard.obs.core.util.DBUtil;

/**
 * @author Rushabh
 *
 */
@Component(immediate =  true, service = CartDao.class)
public class CartDaoImpl implements CartDao {

	@Reference
	DBUtil dbUtil;

	@Override
	public List<Cart> getCartDetails(String dataSourceName, String customerId) throws CartDaoException {
		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		List<Cart> cartItems = new ArrayList<Cart>();

		try {
			connection = dbUtil.getConnection(dataSourceName);
			statement = connection.createStatement();
			statement1 = connection.createStatement();
			resultSet = statement.executeQuery("SELECT B.BOOKID, B.NAME, P.PUBLISHERNAME, B.PAGECOUNT, B.EDITION, B.LANGUAGE, B.QUANTITY, B.PRICE, B.DISCOUNT, G.GENRENAME, C.QUANTITY FROM CART C JOIN BOOK B ON C.BOOKID = B.BOOKID INNER JOIN PUBLISHER P ON P.PUBLISHERID = B.PUBLISHERID INNER JOIN GENRE G ON G.GENREID = B.GENREID AND C.CUSTOMER_ID = '" + customerId + "';");

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
				resultSet1 = statement1.executeQuery("SELECT * FROM WISHLIST WHERE BOOKID = '" + book.getId() + "' AND CUSTOMER_ID = '" + customerId + "';");
				if (resultSet1.next())
					book.setPresentInWishlist(true);
				
				cart.setBook(book);
				cart.setCartQuantity(resultSet.getInt(11));
				
				//Push the Employee Object to the list
				cartItems.add(cart);
			} 	
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new CartDaoException(e);
		}

		finally {
			dbUtil.closeResource(resultSet);
			dbUtil.closeResource(statement);
			dbUtil.closeResource(connection);
		}
		return cartItems;
	}

	@Override
	public int updateCartDetails(String dataSourceName, String bookId, int cartQuantity, String customerId)
			throws CartDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int status = 0;
		try {
			connection = dbUtil.getConnection(dataSourceName);
			preparedStatement = connection.prepareStatement("UPDATE CART SET QUANTITY = ? WHERE BOOKID = ? AND CUSTOMER_ID = '" + customerId + "';");
			preparedStatement.setInt(1, cartQuantity);
			preparedStatement.setString(2, bookId);
			status = preparedStatement.executeUpdate();
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new CartDaoException(e);
		}
		finally {
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
		return status;
	}

	@Override
	public int deleteCartItem(String dataSourceName, String bookId, String customerId) throws CartDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int status = 0;

		try {
			connection = dbUtil.getConnection(dataSourceName);
			preparedStatement = connection.prepareStatement("DELETE FROM CART WHERE BOOKID = ? AND CUSTOMER_ID = ?;");
			preparedStatement.setString(1, bookId);
			preparedStatement.setString(2, customerId);
			status = preparedStatement.executeUpdate();
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new CartDaoException(e);
		}
		finally {
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
		return status;
	}

	@Override
	public int addBookToWishlist(String dataSourceName, String bookId, String customerId) throws CartDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dbUtil.getConnection(dataSourceName);
			preparedStatement = connection.prepareStatement("INSERT INTO WISHLIST VALUES(?, ?, ?);");
			preparedStatement.setString(1, customerId + bookId);
			preparedStatement.setString(2, bookId);
			preparedStatement.setString(3, customerId);
			return preparedStatement.executeUpdate();				
		} catch (DataSourceNotFoundException | SQLException e) {
			throw new CartDaoException(e.getCause());
		}
		finally {
			dbUtil.closeResource(resultSet);
			dbUtil.closeResource(preparedStatement);
			dbUtil.closeResource(connection);
		}
	}

}
