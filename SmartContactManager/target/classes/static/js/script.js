
//toggle btn 
const toggleSidebar = () => {

	//if side bar visible then hide
	//and content marginleft 0
	if ($(".sidebar").is(":visible")) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", ".5%"
		);


	} else {

		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20.5%"
		);

	}
};

const search = () => {
	// console.log("searching........");
	let query = $("#search-input").val();

	if (query == '') {
		$('.search-result').hide();
	} else {
		console.log(query);
		let url = `http://localhost:8080/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			}).then((data) => {
				text = `<div class="list-group">`;
				data.forEach((contact) => {
					text += `<a href="/user/contacts/${contact.cid}" class="list-group-item list-group-item-action">${contact.name}</a>`;

				});
				text += `</div>`;
				$(".search-result").html(text);
				$('.search-result').show();

			});

	}
}

// payment 
const paymentInitiate = () => {
	console.log("payment started");
	let payment = $("#paymentRupees").val();
	if (payment == null || payment == '') {
		console.log("not want to donate its ok!!");
	} else {
		console.log("payment:" + payment);
	}

	//ajax get response from server side
	$.ajax({
		url: '/user/create_order',
		data: JSON.stringify({ payment: payment, info: 'order_req' }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function (response) {
			console.log(response);
			if (response.status == 'created') {
				let options = {
					key: "rzp_test_t6zL8kbFOseA5k",
					amount: response.amount,
					currency: "INR",
					name: "Smart Contact Manager",
					order_id: response.id,
					description: "Donation",
					handler: function (response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");
						
					},
					prefill: {
						name: "Aditya GUpta",
						email: "adityacse207@gmail.com",
						contact: "9794369680"
					},
					notes: {
						address: "gajpur,273413"
					},
					theme: {
						color: "#000000"
					}

				};
				var rzp = new Razorpay(options);
				rzp.on('payment.failed', function (response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					swal({
							title: "Sorry! Your payment was not successful.",
							text: response.amount,
							icon: "error",
							button: "retry",
						});
				});
				rzp.open();
			}
		},
		error: function (error) {
			swal({
					title: "Sorry! Your payment was not successful.",
					text: response.amount,
					icon: "error",
					button: "retry",
				});
		}
	})
};

//save payment details on server side
function updatePaymentOnServer(payment_id, order_id,status)
{
	$.ajax({
		url: '/user/update_order',
		data: JSON.stringify(
			{ 
				payment_id: payment_id,
				order_id:order_id,
				status:status, 
		}),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success:function(response){
			swal({
					title: "Thanks for your contributions!",
					icon: "success",
					button: "OK",
				});
		},
		error:function(error){
			swal({
					title: "Thanks for your contributions!",
					icon: "success",
					button: "OK",
				});
		}
	});

}
















