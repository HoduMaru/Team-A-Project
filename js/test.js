$(document).ready(function () {
    var $id = $('.join_cheak_id');
    $pw = $('.cheakpw');
    $pw1 = $('.cheakpw1');
    $name = $('.cheakname');
    $email = $('.cheakemail');
    $phon = $('.cheakenumber');


    $('.step_btn').click(function () {
        var validateChecker = true
        validateChecker = emptyChecker()
        if (validateChecker == false) {//만약, 공란이 발견되면, false값을 반환한다.
            return validateChecker
        }
    });


    function emptyChecker() {
        var $id_val = $('.join_cheak_id').val();
        $pw_val = $('.cheakpw').val();
        $pw1_val = $('.cheakpw1').val();
        $name_val = $('.cheakname').val();
        $email_val = $('.cheakemail').val();
        $phon_val = $('.cheakenumber').val();

        $id.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($id_val.length > 1) {
            $id.removeClass('shake');
        }

        $pw.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($pw_val.length > 1) {
            $pw.removeClass('shake');
        }

        $pw1.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($pw1_val.length > 1) {
            $pw1.removeClass('shake');
        }

        $name.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($name_val.length > 1) {
            $name.removeClass('shake');
        }

        $email.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($email_val.length > 1) {
            $email.removeClass('shake');
        }

        $phon.addClass('shake').stop().delay(600).queue(function () {
            $(this).removeClass('shake');
        });
        if ($phon_val.length > 1) {
            $phon.removeClass('shake');
        }
    }

    //위에 소스 중복을 재거한고, 하나의 함수로 만들어서 모듈화를 만들 것이다. 아래소스가 완성본이다.
    // function calObj(mode) {
    // 	var obj = {
    // 		'idChecker': function ($el) {
    // 			if ($el.val() === "") {
    // 				$el.addClass('shake').stop().delay(600).queue(function () {
    // 					$(this).removeClass('shake');
    // 				});
    // 				if ($el.val().length > 1) {
    // 					$el.removeClass('shake');
    // 				}
    // 			} else {
    // 				$.post($("#myForm").attr("action"),
    // 					$("#myForm:input").serializeArray(),
    // 					function (data) {
    // 						$("div#ack").html(data);
    // 					});
    // 			}
    // 		}
    // 	}
    // 	return obj[mode];
    // }
});

