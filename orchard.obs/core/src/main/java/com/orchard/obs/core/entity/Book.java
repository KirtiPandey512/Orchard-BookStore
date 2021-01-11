/**
 * 
 */
package com.orchard.obs.core.entity;

import java.util.Date;
import java.util.List;

/**
 * @author Rushabh
 *
 */
public class Book {
	private int price, quantity, discount, pageCount;
	private Long isbn[];
	private String id, name, author[], genre, language, publisher, edition, description;
	private List<String> authors;
	private Date publishDate;
	private boolean isPresentInCart, isPresentInWishlist, isBestSeller,isNew;
	
	public Book() {
		super();
	}

	public Book(int price, int quantity, int discount, int pageCount, Long[] isbn, String id, String name,
			String[] author, String genre, String language, String publisher, String edition, String description,
			List<String> authors, Date publishDate, boolean isPresentInCart, boolean isPresentInWishlist,
			boolean isBestSeller, boolean isNew) {
		super();
		this.price = price;
		this.quantity = quantity;
		this.discount = discount;
		this.pageCount = pageCount;
		this.isbn = isbn;
		this.id = id;
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.language = language;
		this.publisher = publisher;
		this.edition = edition;
		this.description = description;
		this.authors = authors;
		this.publishDate = publishDate;
		this.isPresentInCart = isPresentInCart;
		this.isPresentInWishlist = isPresentInWishlist;
		this.isBestSeller = isBestSeller;
		this.isNew = isNew;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public Long[] getIsbn() {
		return isbn;
	}

	public void setIsbn(Long[] isbn) {
		this.isbn = isbn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAuthor() {
		return author;
	}

	public void setAuthor(String[] author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isPresentInCart() {
		return isPresentInCart;
	}

	public void setPresentInCart(boolean isPresentInCart) {
		this.isPresentInCart = isPresentInCart;
	}

	public boolean isPresentInWishlist() {
		return isPresentInWishlist;
	}

	public void setPresentInWishlist(boolean isPresentInWishlist) {
		this.isPresentInWishlist = isPresentInWishlist;
	}

	public boolean isBestSeller() {
		return isBestSeller;
	}

	public void setBestSeller(boolean isBestSeller) {
		this.isBestSeller = isBestSeller;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}
