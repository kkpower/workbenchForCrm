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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为创建按钮绑定事件，打开市场活动添加操作的模态窗口
		$("#addBtn").click(function () {

			/*

				操作模态窗口：

					找到模态窗口的jquery对象，调用modal方法，为方法传递参数，取值：
																				show：打开模态窗口
																				hide：关闭模态窗口

			 */

			/*alert(123);

			$("#createActivityModal").modal("show");*/

			/*

				发出ajax请求，取得用户信息列表，将用户信息铺到所有者的下拉框中
				数据铺完之后，最后再打开模态窗口

			 */

			$.ajax({

				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function (data) {

					/*

						data
							List<User> uList...
							[{用户1},{2},{3}]

					 */

					var html = "<option></option>";

					//每一个n，就是每一个用户的json对象
					$.each(data,function (i,n) {

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})

					//将以上拼好的option，放在所有者select标签对中
					$("#create-owner").html(html);

					//将当前登录的用户当做所有者下拉框默认的选项
					//取得当前登录的用户的id
					//el表达式是能够使用在js代码中的，但是el表达式必须要套用在字符串的引号中
					var id = "${user.id}";
					$("#create-owner").val(id);

					//处理完所有者的下拉框中的信息之后，打开添加操作的模态窗口
					$("#createActivityModal").modal("show");

				}

			})



		})


		//为保存按钮绑定事件，执行市场活动的添加操作
		$("#saveBtn").click(function () {

			$.ajax({

				url : "workbench/activity/save.do",
				data : {

					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())

				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*

						data
							{"success":true/false}

					 */

					if(data.success){

						//添加成功

						//刷新市场活动列表
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))

						//清空添加操作模态窗口中的表单信息
						/*

							jquery为我们提供了submit()方法，用来提交表单
							但是，jquery并没有为我们提供reset()方法重置表单，idea乱提示...

							虽然jquery没有为我们提供重置操作，但是原生js为我们提供了这个方法


							jquery对象转换成为dom对象
								jquery对象[0]

							dom对象转换为jquery对象
								$(dom对象)

						 */
						//$("#activitySaveForm")[0].reset();


						//关闭模态窗口
						$("#createActivityModal").modal("hide");


					}else{

						alert("添加市场活动失败");

					}

				}

			})

		})

		pageList(1,2);

		//为查询按钮绑定事件，执行市场活动信息的条件查询操作
		$("#searchBtn").click(function () {

			//将搜索框中的信息保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);

		})

		//为全选的复选框绑定事件，执行全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);

		})

		//为普通的复选框绑定事件，操作全选的复选框
		/*$("input[name=xz]").click(function () {

			alert(123);

		})*/

		/*

			以上做法不行，因为以上我们需要绑定的元素（所有name等于xz的input元素）都是我们通过js动态拼接生成的
			动态拼接生成的元素，不能像以前那样来绑定事件
			动态拼接生成的元素，需要使用on方法来绑定事件

			$(找到需要绑定的元素的有效的父级元素).on(绑定事件的方式，需要绑定的元素，回调函数)

		 */

		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})

		//为删除按钮绑定事件，执行市场活动的删除操作
		$("#deleteBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要删除的记录");

			//有可能选了一条，也有可能选了多条（因为可批量删除）
			}else{

				if(confirm("确定删除所选记录吗？")){

					//id=xxx&id=xxx&id=xxx

					var param = "";

					for(var i=0;i<$xz.length;i++){

						param += "id="+$($xz[i]).val();

						//判断：如果不是最后一条记录
						if(i<$xz.length-1){

							//在尾部追加&符号
							param += "&";

						}

					}

					//alert(param);

					//以上我们将参数拼接完成了
					//接下来我们来发出ajax请求，执行市场活动的删除操作
					$.ajax({

						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function (data) {

							/*

                                data
                                    {"success":true/false}

                             */

							if(data.success){

								//删除成功
								//刷新市场活动列表
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))

							}else{

								alert("删除市场活动失败");

							}

						}

					})

				}



			}

		})


		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要修改的记录");

			}else if($xz.length>1){

				alert("只能选择一条记录执行修改操作");

			//肯定是选了，而且肯定选的是一条记录
			}else{

				//如果能够保证选中的是一条记录，即使是复选框，我们也可以直接通过val方法，取得唯一的id值
				var id = $xz.val();

				$.ajax({

					url : "workbench/activity/getUserListAndActivity.do",
					data : {

						"id" : id

					},
					type : "get",
					dataType : "json",
					success : function (data) {

						/*

							data
								List<User> uList..
								Activity a ...
								{"uList":[{用户1},{2},{3}],"a":{市场活动}}
						 */

						//处理所有者
						var html = "<option></option>";

						$.each(data.uList,function (i,n) {

							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						})

						$("#edit-owner").html(html);

						//处理修改表单中的信息
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						//打开修改操作的模态窗口
						$("#editActivityModal").modal("show");

					}

				})

			}


		})




		//为更新按钮绑定事件，执行市场活动的修改操作
		$("#updateBtn").click(function () {

			$.ajax({

				url : "workbench/activity/update.do",
				data : {

					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())

				},
				type : "post",
				dataType : "json",
				success : function (data) {


					if(data.success){

						//修改成功

						//刷新市场活动列表
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭模态窗口
						$("#editActivityModal").modal("hide");


					}else{

						alert("修改市场活动失败");

					}

				}

			})

		})



	});

	/*

		关于pageList方法的入口：
		1.点击页面左侧菜单中的 "市场活动"，需要调用pageList方法，局部刷新市场活动列表
		2.点击"查询"按钮，需要调用pageList方法，局部刷新市场活动列表
		3.点击分页插件相关按钮的时候，需要调用pageList方法，局部刷新市场活动列表
		4.添加市场活动后，需要调用pageList方法，局部刷新市场活动列表
		5.修改市场活动后，需要调用pageList方法，局部刷新市场活动列表
		6.删除市场活动后，需要调用pageList方法，局部刷新市场活动列表


		关于pageList方法的参数：
		pageNo：当前页的页码
		pageSize：每页需要展现的记录数
		这两个参数是与分页操作相关的参数，同时也是所有关系型数据库用来做分页相关操作的基础参数
										有了这两个参数后，所有的需要的分页相关的信息都能够取得


		发出ajax请求，需要为后台提供哪些参数：
		分页查询相关的参数：pageNo，pageSize
		条件查询相关的参数：name，owner，startDate，endDate
		一共为后台传递6个参数


	 */
	function pageList(pageNo,pageSize) {

		//alert("（局部）刷新市场活动列表");

		//将全选复选框的√灭掉
		$("#qx").prop("checked",false);

		//将隐藏域中的信息取出填充到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({

			url : "workbench/activity/pageList.do",
			data : {

				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val()),
				"pageNo" : pageNo,
				"pageSize" : pageSize

			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*

					data
						List<Activity> dataList..
						int total..
						{"total":100,"dataList":[{市场活动1},{2},{3}]}

				 */

				var html = "";

				//var str = "'\"\"'";
				//var str = '"\'\'"';

				//n:市场活动的json对象
				$.each(data.dataList,function (i,n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})

				//将以上所有拼接好的tr和td往tBody标签对里塞
				$("#activityBody").html(html);

				//列表信息处理完毕后，通过bootstrap的分页插件实现分页操作

				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该函数的触发时机：在点击分页插件相关按钮的时候触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});




			}

		})

	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">



								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activitySaveForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">



								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">

								<!--

									关于textarea
										是一个表单元素
										与其他的表单元素不同，其他的表单元素操作的是value属性值
										textarea操作的标签对中的内容
										虽然textarea操作的是标签对中的内容，但是textarea仍然属于表单元素，只要是表单元素，一律使用val来操作值，而不是使用html来操作值

								-->
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--

						data-dismiss="modal"
							关闭模态窗口

					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text"  id="search-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text"  id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

					<!--

						data-toggle="modal"
							表示触发按钮，将会弹出一个模态窗口（模态框）

						data-target="#createActivityModal"
							找到开启的模态窗口的目标，通过#id来找到需要打开的模态窗口

						需求：在点击"创建按钮后"，打开模态窗口前，先弹出一个alert(123)

						关于这个需求，我们现在很不方便去处理，因为操作模态窗口的两组属性和属性值，写死在了button按钮中，只要写了，就会立刻触发
						如果是这样的话，将来对于模态窗口的触发（模态窗口的打开或者关闭）时机就会随着属性值而产生作用

						在实际项目开发中，对于按钮的行为的触发，不应该由属性和属性值来决定，应该由我们自己为按钮绑事件来决定

					-->

				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称123</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>