<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

	/*String id = request.getParameter("id");
	String fullname = request.getParameter("fullname");
	String owner = request.getParameter("owner");
	String appellation = request.getParameter("appellation");
	String company = request.getParameter("company");*/


%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为搜索市场活动的文本框绑定事件，执行搜索市场活动列表的操作
		$("#aname").keydown(function (event) {

			if(event.keyCode==13){

				//alert("搜索并展现市场活动列表");

				$.ajax({

					url : "workbench/clue/getActivityListByName.do",
					data : {

						"aname" : $.trim($("#aname").val())

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*

							data
								List<Activity> aList
								[{市场活动1},{2},{3}]

						 */

						var html = "";

						$.each(data,function (i,n) {

							html += '<tr>';
							html += '<td><input type="radio" value="'+n.id+'" name="xz"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';

						})

						$("#activitySearchBody").html(html);

					}

				})

				return false;

			}

		})

		//为提交按钮绑定事件，填充交易表单中市场活动相关信息
		$("#submitActivityBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			//取得选中的单选中的id值
			var id = $xz.val();

			//将id值赋值到市场活动的隐藏域的id中
			$("#activityId").val(id);

			//取得选中的市场活动的名称
			var name = $("#"+id).html();

			//将name值赋值到市场活动的文本框中
			$("#activityName").val(name);

			//关闭模态窗口
			$("#searchActivityModal").modal("hide");


		})

		//为转换按钮绑定事件，执行线索的转换操作
		$("#convertBtn").click(function () {

			/*

				在点击转换按钮后，在后台执行线索的转换操作
				转换操作后
				前端不需要执行任何的局部刷新操作
				线索转换后，直接跳转到线索的列表页

				所以我们可以发出一个传统请求来处理这个问题

				传统请求：
					浏览器地址栏输入地址，敲回车
					点击超链接，执行href中的链接<a href="">
					执行js代码：window.location.href ="";
					提交form表单
					...

			 */

			//判断"为客户创建交易"的复选框
			//如果选中了，说明在执行线索转换的基础上，需要额外创建交易
			if($("#isCreateTransaction").prop("checked")){

				//使用这种形式传递多个参数麻烦，而且不利于将来参数的扩展
				//window.location.href ="workbench/clue/convert.do?clueId=xxx&money=xxx&name=xxx&expectedDate=xxx&....";

				//提交表单
				$("#tranForm").submit();

			//如果没有选，说明只需要执行线索转换操作，不需要创建交易
			}else{

				window.location.href ="workbench/clue/convert.do?clueId=${param.id}";

			}



		})

	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">

		<%--

			${param.fullname} === request.getParameter("fullname")

		--%>

		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<!--

			以传统形式提交表单，表单元素中必须使用name属性来为后台传递参数

		    workbench/clue/convert.do?clueId=xxx&money=xxx&name=xxx&expectedDate=xxx&stage=xxx&activityId=xxx&flag=a

		-->
		<form id="tranForm" method="post" action="workbench/clue/convert.do">

			<input type="hidden" name="flag" value="a"/>

			<input type="hidden" name="clueId" value="${param.id}"/>

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stageList}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
			  <input type="hidden" id="activityId" name="activityId"/>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>