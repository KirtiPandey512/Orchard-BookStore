$(function() {
	var customerId = -1;
    var status = 0;
	var name = "customerId" + "=";
    var ca = document.cookie.split(';');

  	for(var i = 0; i < ca.length; i++) {
    	var c = ca[i];
    	while (c.charAt(0) == ' ') {
      		c = c.substring(1);
    	}
    	if (c.indexOf(name) == 0) {
      		customerId = c.substring(name.length, c.length);
            status = 1;
    	}
  	}

    if(status == 1) {
    	$(this).fetchCartData(customerId);
    }
    else {
        $("#cart-modal").html("Login To View Cart!...");
    }
});

	$.fn.fetchCartData = function(customerId) {
		$.ajax({
			url: '/bin/obs/cartservlet',
		 	type: 'POST',
            data: {
            	customerId: customerId,
                action: "getcartdata"
            },
		 	complete: function(xhr, status) {
            	if (status == "success") {
                	cartData = JSON.parse(xhr.responseText);
                    $(this).loadCartData(cartData, customerId);
                }
                else {
                	alert("Error");
                }
		 	}
	  	});
	}

    $.fn.updateCartData = function(index, quantity, customerId) {
		$.ajax({
			url: '/bin/obs/cartservlet',
		 	type: 'POST',
		 	data: {
				bookId: (cartData[index].book).id,
                quantity: quantity,
                customerId: customerId,
                action: "updatecartdata"
		 	},
		 	complete: function(xhr, status) {
            	if (status == "success") {
                	if(xhr.responseText > 0) {
                    	cartData[index].cartQuantity = quantity;
                        $(this).loadCartData(cartData, customerId);
                    }
                }
                else {
                	alert("Error");
                }
		 	}
		});
	}

	$.fn.addToWishlist = function(index, customerId) {
		$.ajax({
			url: '/bin/obs/cartservlet',
			type: 'POST',
			data: {
				bookId: (cartData[index].book).id,
                customerId: customerId,
                action: "addtowishlist"
			},
		 	complete: function(xhr, status) {
            	if (status == "success") {
                	if(xhr.responseText > 0) {
                      	alert("Successfully Saved To Wishlist");
                   	}
               	}
                else {
                	alert("Error");
                }
		 	}
		});
    }

	$.fn.removeCartItem = function(index, customerId) {
		$.ajax({
			url: '/bin/obs/cartservlet',
			type: 'POST',
			data: {
				bookId: (cartData[index].book).id,
                customerId: customerId,
                action: "removecartitem"
			},
		 	complete: function(xhr, status) {
            	if (status == "success") {
                	if(xhr.responseText > 0) {
						cartData.splice(index, 1);
                      	$(this).loadCartData(cartData, customerId);
                   	}
               	}
                else {
                	alert("Error");
                }
		 	}
		});
	}

	$.fn.loadCartData = function(cartData, customerId) {
   		$("#cart-block").empty();
		if(cartData.length != 0) {
        	$(".modal").css("display", "block");
        	var totalPrice = 0, totalDiscount = 0, deliveryCharge = 0, totalAmount = 0, totalAmountSaved = 0;
        	var cartBlock = [
            	'<div id="cart-block-first-block">',
                	'<div id="header">',
                    	'<div id="label">My Cart (' + cartData.length + ')</div>',
                    '</div>',
                '</div>'
			].join("\n");
			$("#cart-block").append(cartBlock);

			$.each(cartData, function(index, item) {
            	book = item.book;
				discountPrice = (((100-book.discount)/100)*book.price*item.cartQuantity);
                actualPrice = (book.price*item.cartQuantity);
               	totalPrice += actualPrice;
                totalDiscount += (book.discount*book.price*item.cartQuantity/100);

				var Item_Block = [
						'<div class="item-block">',
                        	'<div class="item-block-first-div">',
                            	'<div class="item-image-block">',
                                	'<img src="/content/dam/obs/en/images/books/' + book.name + '.jpg">',
                            	'</div>',
                            	'<div class="item-detail-block">',
                                	'<div class="item-name">' + book.name + '</div>',
                                	'<div class="language">' + book.language + '</div>',
                                	'<div class="item-price">',
                                    	'<div class="discount-price">₹' + discountPrice + '</div>',
                                    	'<div class="actual-price"><s>₹' + actualPrice + '</s></div>',
                                    	'<div class="discount">' + book.discount + '% Off</div>',
                                	'</div>',
                            	'</div>',
                            	'<div class="delivery-detail-block">',
                                	'<div class="delivery-detail">Delivery in 3 - 5 days | ₹66</div>',
                                	'<div class="replacement-policy">7 Days Replacement Policy</div>',
                            	'</div>',
                        	'</div>',
                        	'<div class="item-block-second-div">',
                            	'<div class="item-quantity-block">',
                                	'<button class="minus" id="m' + index + '" onclick="$(this).updateCartData(' + index + ',' + (item.cartQuantity-1) + ',\'' + customerId + '\');"> – </button>',
                                	'<input class="item-count" id="q' + index + '" type="number" min="1" onkeypress="return event.keyCode > 48 && event.keyCode < 57" value=' + item.cartQuantity + '>',
                                	'<button class="plus" id="p' + index + '" onclick="$(this).updateCartData(' + index + ',' + (item.cartQuantity+1) + ',\'' + customerId + '\');"> + </button>',
                            '</div>',
                            '<div class="item-save-remove">',
                                '<div class="item-save-later" onclick="$(this).addToWishlist(' + index + ',\'' + customerId + '\')">ADD TO WISHLIST</div>',
                                '<div class="item-remove" onclick="$(this).removeCartItem(' + index + ',\'' + customerId + '\')">REMOVE</div>',
                            '</div>',
                        '</div>',
                    '</div>'
				].join("\n");
                $("#cart-block-first-block").append(Item_Block);

                if(item.cartQuantity > 1) {
               		$("#m"+index).prop("disabled", false);
                }
                else {
                	$("#m"+index).prop("disabled", true);
                }

                if(book.discount == 0) {
                	$(".discount").hide();
                    $(".actual-price").hide();
                }
			});

			var Place_Order_Block = [
            	'<div class="place-order-block">',
                	'<button onclick="$(this).placeOrder();">PLACE ORDER</button>',
                '</div>'
            ].join("\n");
			$("#cart-block-first-block").append(Place_Order_Block);

            var Price_Details_Block = [
            	'<div id="cart-block-second-block">',
        		'<div id="price-details-block">',
               		'<div id="price-details-block-first-div">',
                    	'<label>PRICE DETAILS</label>',
               	    '</div>',
               	    '<div id="price-details-block-second-div">',
                   		'<div id="price-div">',
                        	'<div id="price-div-left">Price (item ' + cartData.length + ')</div>',
                            '<div id="price-div-right">₹' + totalPrice + '</div>',
                        '</div>',
                        '<div id="discount-div">',
                        	'<div id="discount-div-left">Discount</div>',
                            '<div id="discount-div-right">₹' + totalDiscount + '</div>',
                        '</div>',
                        '<div id="delivery-charges-div">',
                        	'<div id="delivery-charges-div-left">Delivery Charge</div>',
                            '<div id="delivery-charges-div-right">₹' + deliveryCharge + '</div>',
                        '</div>',
                        '<div id="total-amount-div">',
                        	'<div id="total-amount-div-left">Amount</div>',
                        	'<div id="total-amount-div-right">₹' + (totalPrice-totalDiscount+deliveryCharge) + '</div>',
                        '</div>',
                        '<div id="saved-amount">',
                        	'<div>You will save ₹' + (totalDiscount-deliveryCharge) + ' on this order</div>',
                        '</div>',
               	    '</div>',
                '</div>',
                '</div>'
        	].join("\n");
            $("#cart-block").append(Price_Details_Block);

            if(totalPrice != 0) {
        	if(totalDiscount == 0 || totalDiscount < deliveryCharge) {
            	$("#discount-div").hide();
            	$("#saved-amount").hide();
        	}

        	if(deliveryCharge == 0) {
            	$("#delivery-charges-div-right").text("FREE");
        	}
        }
        else {
            $("#item-block-second-div").hide();
            $("button").prop("disabled", true);
            $("button").css("background-color", "grey");
        }
		}
    	else
    		$("#cart-block").html("<h3>Cart Is Empty!...</h3>");
	}

    $.fn.placeOrder = function() {
    	window.location = "order.html?" + customerId;
	}