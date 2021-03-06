/**
 * 
 */
package com.orchard.obs.core.services;

import java.util.List;

import com.orchard.obs.Exceptions.serviceExceptions.CartServiceException;
import com.orchard.obs.core.entity.Cart;


/**
 * @author Rushabh
 *
 */
public interface CartService {
	
	public List<Cart> getCartDetails(String dataSourceName, String customerId) throws CartServiceException;
	
	public int updateCartDetails(String dataSourceName, String bookId, int cartQuantity, String customerId) throws CartServiceException;

	public int deleteCartItem(String dataSourceName, String bookId, String customerId) throws CartServiceException;
	
	public int addBookToWishlist(String dataSourceName, String bookId, String customerId) throws CartServiceException;
	
}
