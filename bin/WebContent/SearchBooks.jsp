<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>圖書列表</title>

<script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/noty/packaged/jquery.noty.packaged.min.js"></script>
<link rel="stylesheet" href="./css/newtab.css" type="text/css">
<link rel="stylesheet" href="./css/bootstrap.min.css" type="text/css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" href="./css/common.css" type="text/css">
<link rel="stylesheet" href="./css/list.css" type="text/css">
<link rel="stylesheet" href="./css/general.css" type="text/css">
<link rel="stylesheet" href="./css/animate.min.css" type="text/css">

<meta name="keywords" content="">
<meta name="description" content="">
<meta name="robots" content="noindex, nofollow">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<script>
$(function() {
	var msg = getURLParameter("actionMessages");
	msg = msg || '';
	if (msg != '' && msg != 'null') {
		var n = noty({text: msg, type:'success'});
	}
	search();
});

function getURLParameter(name) {
    return decodeURI((RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]);
}

function ValidateNumber(e, pnumber)
{
    if (!/^\d+$/.test(pnumber))
    {
        $(e).val(/^\d+/.exec($(e).val()));
    }
    if ($(e).val().substring(0,1) === '0') {
    	$(e).val(0);
    }
    return false;
}

function search() {
	$.ajax({
		type: "POST",
		url : '${pageContext.request.contextPath}/queryBook.action',
		dataType : 'html',
		data :$("#SearchForm").serialize(),
		success: function(obj){
			$('#ResultList').html(obj);
			var n = noty({text: '搜尋成功', type:'success', layout:'bottomRight'});
		},
		error : function(xhr) {
			var n = noty({text: '不明錯誤發生!!!', type:'error'});
		}
	});
}

</script>
</head>

<body>
	<div class="container gc-container">
		<div class="success-message hidden"></div>

		<div class="row">
			<div class="table-section">
				<div class="table-label">
					<div class="floatL l5">圖書列表</div>
					<div
						class="floatR r5 minimize-maximize-container minimize-maximize">
						<i class="fa fa-caret-up"></i>
					</div>
					<div class="floatR r5 gc-full-width">
						<i class="fa fa-expand"></i>
					</div>
					<div class="clear"></div>
				</div>
				<div class="table-container">
						<div class="header-tools">
							<div class="floatL t5">
								<a class="btn btn-default" href="${pageContext.request.contextPath}/goCreate.action">
									<i class="fa fa-plus"></i> &nbsp;新增
								</a>
							</div>
							<div class="floatR"></div>
							<div class="clear"></div>
						</div>
						<form method="POST" id="SearchForm" name="SearchForm" action="">
						<table class="table table-bordered grocery-crud-table table-hover">
							<thead>
								<tr>
									<th colspan="2">功能</th>
									<th class="column-with-ordering">圖書名稱</th>
									<th class="column-with-ordering">圖書價格</th>
									<th class="column-with-ordering">圖書封面</th>
									<th class="column-with-ordering">創建時間</th>
								</tr>

								<tr class="filter-row gc-search-row">
									<td class="no-border-right ">
										<div class="floatL t5"></div>
									</td>
									<td class="no-border-left ">
										<div>
											<a href="javascript:search();"
												class="btn btn-default gc-refresh"> <i
												class="fa fa-refresh" >&nbsp;搜尋</i>
											</a>
										</div>

										<div class="clear"></div>
									</td>
									<td><input type="text"
										class="form-control searchable-input floatL"
										placeholder="搜尋讀書名稱" name="bookName"></td>
									<td><input type="text"
										class="form-control searchable-input floatL"Q
										placeholder="搜尋讀書價格" name="bookPrice" onkeyup="return ValidateNumber($(this),value)">
									</td>
									<td></td>
									<td></td>
								</tr>
							</thead>
							<tbody id="ResultList">
							</tbody>
						</table>
						
						</form>
						
						<!-- Table Footer -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>