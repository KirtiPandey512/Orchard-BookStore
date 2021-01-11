$(document).ready(function(){
	deleteCookie('customObject');
	getFilteredBook('genre','all');
});
function setCookie(name, filter) {
	var customObject = {};
	customObject.first = name;
	customObject.second = filter;

	var jsonString = JSON.stringify(customObject);
	document.cookie = "customObject=" + jsonString;
}
function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0)
			return c.substring(nameEQ.length, c.length);
	}
	return null;
}
function deleteCookie(name){
	document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
}
function getFilteredBook(name, filter) {
	
	var customerId = null;
	var namee = "customerId" + "=";
	var ca = document.cookie.split(';');

		for(var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
  			c = c.substring(1);
		}
		if (c.indexOf(namee) == 0) {
  			customerId = c.substring(namee.length, c.length);
		}
		}
	
	if (filter.localeCompare("all")==0){
		url = "/bin/FilterBookServlet?first=" + name + "&second=" + filter+"&customerId="+customerId;
		deleteCookie('customObject');
	}
	if (filter.localeCompare("all")!=0) {
		var json = readCookie('customObject');
		var customObject = JSON.parse(json);
		setCookie(name, filter);
		if ((customObject) === null) {
			url = "/bin/FilterBookServlet?first=" + name + "&second=" + filter+"&customerId="+customerId;
		} else if ((customObject.first).localeCompare("genre")
				&& (name.localeCompare("genre"))) {
			url = "/bin/FilterBookServlet?first=" + name + "&second=" + filter+"&customerId="+customerId;
		} else if ((customObject.first).localeCompare("publisher")
				&& (name.localeCompare("publisher"))) {
			url = "/bin/FilterBookServlet?first=" + name + "&second=" + filter+"&customerId="+customerId;
		} else if (name.localeCompare("genre")) {
			url = "/bin/FilterBookServlet?first=" + customObject.second
					+ "&second=" + filter+"&customerId="+customerId;
		} else if (name.localeCompare("publisher")) {
			url = "/bin/FilterBookServlet?first=" + filter + "&second="
					+ customObject.second+"&customerId="+customerId;
		}
	}
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			data = JSON.parse(this.responseText);
			generateTiles(data);
		}
	};
	xhttp.open("GET", url, true);
	xhttp.send();
}
function generateTiles(data) {
	var html = '<section id="products">';
	for (x in data) {

		html += [
				'<section class="product-tile">',
				'<div class="product-block">',
				'<div class="image-block">',
				'<div class="product-image">',
				'<img src="/content/dam/obs/en/images/books/',data[x].name,'.jpg">',
				'</div>',
				'<div class="hover-block">',
				'<button class="add-to-cart"',"id=",data[x].id,'onclick="addToCartFromPLP(',data[x].id,')">'].join("\n");
        if(data[x].isPresentInCart){
            html+="GO TO CART";}
        else{

                html+="ADD TO CART";
            }

				html += ['</button><a href="/content/obs/in/en/menu/bookworm/productdetail.html?bookId=',data[x].id,'"><button class="view-detail">VIEW DETAILS</button></a>',
				'</div>'].join("\n");
        if(data[x].isBestSeller){
				html+='<div class="bestseller-label"><img src="https://www.sapnaonline.com/static/images/sapna/bestseller.svg"></div>';
				 }
				html+=[ '<div class="discount-label">', '<label>',data[x].discount,'%</label>',
				'</div>', '</div>', '<div class="product-detail">',
				'<div class="book-name"><b>', data[x].name, '</b></div>',
				'<div class="hover-product-detail">',
				'<label class="creator"><i>by', data[x].authors,
				'</i></label>', '<br>', '<label><b>Language</b></label>',
				'<label class="language"><i>', data[x].language,
				'</i></label>', '</div>', '<div class="price">&#8377;',
				data[x].price, '/-</div>', '</div>'].join("\n");
		if(data[x].isNew){
				html+='<div class="new-product-label"><label>New</label></div>';
		}
				html+='</div></section>' ;
	}
	html += '</section>';
	document.getElementById("ab").innerHTML = html;
}
function addToCartFromPLP(bookId){
    var customerId = null;
		var name = "customerId" + "=";
    	var ca = document.cookie.split(';');

  		for(var i = 0; i < ca.length; i++) {
    		var c = ca[i];
    		while (c.charAt(0) == ' ') {
      			c = c.substring(1);
    		}
    		if (c.indexOf(name) == 0) {
      			customerId = c.substring(name.length, c.length);
    		}
  		}
    if(customerId != null) {
        if(document.getElementById(bookId).textContent=="ADD TO CART"){
	url = "/bin/obs/bookservlet?bookId=" + bookId + "&action=addtocart&customerId=" +customerId;

    var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
				document.getElementById(bookId).textContent="GO TO CART";

		}
	};
	xhttp.open("POST", url, true);
            xhttp.send();}else{
                window.location="/content/obs/in/en/menu/mycart.html";
            }}
    else{
        window.location="/content/obs/in/en/menu/bookworm/login.html";
    }
}