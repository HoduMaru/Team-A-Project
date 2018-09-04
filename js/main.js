function calObj(mode) {
	var obj = {
		"check1": function ($el){
			var result = $el.hasClass('btn_active');
			if (result) {
				$el.removeClass('btn_active');
				$('.btn1').removeClass('btn_active');
				$('.btn2').removeClass('btn_active');
			} else {
				$el.addClass('btn_active');
				$('.btn1').addClass('btn_active');
				$('.btn2').addClass('btn_active');
			}
		},
		"check2": function ($el) {
			var result = $el.hasClass('btn_active');
			if (result) {
				$el.removeClass('btn_active');
			} else {
				$el.addClass('btn_active');
			}
		},
		"check3": function ($el) {
			var result = $el.hasClass('btn_active');
			if (result) {
				$el.removeClass('btn_active');
			} else {
				$el.addClass('btn_active');
			}
		},
		"idChecker": function ($el, email, password) {
			if ($el.val() === "") {
				$el.addClass('shake').stop().delay(600).queue(function () {
					$(this).removeClass('shake');
				});
				if ($el.val().length > 1) {
					$el.removeClass('shake');
				}
			} else {
				$.post($("#myForm").attr("action"),
    					$("#myForm:input").serializeArray(),
    					function (data) {
    						$("div#ack").html(data);
    					});
			}
		}
	}
	return obj[mode];
}

$(document).ready(function() {
	// 약관동의 페이지 작업
	$('.agreement_btn').click(function(e) {
		// $(this).toggleClass('btn_active');
		calObj('check1')($(this));
		e.preventDefault();
	});
	// 약관동의 페이지 작업
	$('.btn1').click(function(e) {
		calObj('check2')($(this));
		e.preventDefault();
	});

	$('.btn2').click(function (e) {
		calObj('check3')($(this));
		e.preventDefault();
	});



});

