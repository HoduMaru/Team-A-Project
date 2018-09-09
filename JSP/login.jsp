<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>NETFLIX</title>
<meta charset='utf-8'>
<meta name='viewport' content='width=device-width Initial-scale=1.0, minimum-scale=1.0, user-scalable=yes'>
<!--DESIGN CSS-->
<link rel='stylesheet' href='css/module.css'>
<link rel='stylesheet' href='css/D_module.css'>
<link rel='stylesheet' href='css/style.css'>
</head>
<body>
        <div class="bg">
            <div class="l_wrapper">
                <h1 class="logo">
                    <a href="index.html" class="tit_28">MEMBERS LOGIN</a>
                </h1>
                <form action="">
                    <section id="myForm" class="int">
                        <input type="text" placeholder="아이디" class="in_e_redicalRed in_e_sel_menu"/>
                        <input type="text" placeholder="비밀번호" class="in_e_redicalRed in_e_sel_menu"/>
                        <button class="btn_bg_redicalRed btn_layer" href="#0">ENTRAR</button>
                        <div class="login_bottom clearfix">
                            <div class="login_left">
                                <a href="id_lost.html" class="btn_findid para_14">아이디찾기</a>
                                <a href="password_lost.html" class="btn_findpw para_14">비밀번호찾기</a>
                            </div>
                            <div class="login_right">
                                <a href="#0" class="icon"></a>
                                <a href="agreement.html" class="btn_join para_14">회원가입</a>
                            </div>
                        </div>
                    </section>
                </form>
            </div>
        </div>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.12.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="./js/main.js"></script>
    <script>
		$(document).ready(function(){
			$('.btn_layer').click(function(){
				$('#myForm input').each(function(index){
					calObj('idChecker')($('#myForm input:eq('+index+')'));
				});
			});
		});
	</script>
</body>
</html>