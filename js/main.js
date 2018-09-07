function calObj(mode) {
	var obj = {
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


