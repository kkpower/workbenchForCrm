<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>

		$(function () {

			//如果顶层窗口不是当前窗口
			if(window.top!=window){
				//将顶层窗口设置为当前窗口
				window.top.location=window.location;
			}

			//页面加载完毕后，清空掉原有的用户名的文本框中的信息
			$("#loginAct").val("");

			//在页面加载完毕后，让用户名文本框自动取得焦点
			$("#loginAct").focus();

			//为登录按钮绑定事件，执行登录操作
			$("#submitBtn").click(function () {

				login();

			})

			//为当前窗口绑定事件，绑敲键盘事件，如果敲的是回车键，执行登录操作
			//我们可以通过event参数，取得敲键盘的码值，通过码值来判断敲的是哪个键，如果码值为13，说明是回车键
			$(window).keydown(function (event) {

				//如果码值为13，说明是回车键
				if(event.keyCode==13){

					login();

				}

			})


		})

		/*

			我们自定义的function方法，一定要写在$(function(){})的外面

		 */
		//执行登录操作
		function login() {

			//取得账号密码
			//$.trim(内容)：去除左右空格
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			//判断账号密码是否为空
			if(loginAct=="" || loginPwd==""){

				$("#msg").html("账号密码不能为空");

				//如果账号密码为空，就没有继续向下验证的必要了，我们需要及时终止掉该方法
				return false;

			}

			//验证账号密码是否正确
			//为后台发出ajax请求
			$.ajax({

				url : "settings/user/login.do",
				data : {

					"loginAct" : loginAct,
					"loginPwd" : loginPwd

				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

						data
							{"success":true/false,"msg":?}

					 */

					if(data.success){

						//登录验证成功
						//跳转到工作台的初始页（登录后的欢迎页）
						window.location.href = "workbench/index.jsp";


					}else{

						//登录验证失败
						$("#msg").html(data.msg);

					}

				}

			})

		}

	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<!--

						此处一定要将type改为button，通知表单我就是一个普通的按钮，不给你提交表单
						该按钮我们应该起id属性，为该按钮绑定事件

					-->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录123</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>