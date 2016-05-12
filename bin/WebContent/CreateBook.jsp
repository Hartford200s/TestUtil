<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
<script type="text/javascript" src="js/noty/packaged/jquery.noty.packaged.min.js"></script>

<link rel="stylesheet" href="./css/newtab.css" type="text/css">
<link rel="stylesheet" href="./css/chosen.css" type="text/css">
<link rel="stylesheet" href="./css/bootstrap.min.css"
	type="text/css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" href="./css/common.css" type="text/css">
<link rel="stylesheet" href="./css/general.css" type="text/css">
<link rel="stylesheet" href="./css/add-edit-form.css"
	type="text/css">

<meta name="keywords" content="">
<meta name="description" content="">
<meta name="robots" content="noindex, nofollow">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script>

function pageToolTrim(trimString){
    return trimString.replace(/^\s+|\s+$/gm,'');
}

function validateBook() {
	var boo = true;
	if (pageToolTrim($("#bookName").val()) == '' || pageToolTrim($("#bookPrice").val()) == ''){
		boo = false;
	}
	return boo;
}

function doCreate() {
	if (!validateBook()) {
		var n = noty({text: '資料不允許空白', type:'error'});
		return false;
	}
	$.ajax({
		type: "POST",
		url : '${pageContext.request.contextPath}/checkBookName.action',
		dataType : 'json',
		data :{bookName:$("#bookName").val()},
		success: function(obj){
			if('exist' == obj.Result){
				var n = noty({text: '圖書名稱重複', type:'error'});
				return false;
			}else{
				$("#crudForm").submit();
			}			
		},
		error : function(xhr) {
			var n = noty({text: '不明錯誤發生!!!', type:'error'});
		}
	});
}

function doSearch() {
	window.open('${pageContext.request.contextPath}/goSearch.action','_self');
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

function getURLParameter(name) {
    return decodeURI((RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]);
}
</script>
</head>
<body>
	<div class="crud-form">
		<div class="container gc-container">
			<div class="row">
				<div class="col-md-12">
					<div class="table-label">
						<div class="floatL l5">新增圖書</div>
						<div
							class="floatR r5 minimize-maximize-container minimize-maximize">
							<i class="fa fa-caret-up"></i>
						</div>
						<div class="floatR r5 gc-full-width">
							<i class="fa fa-expand"></i>
						</div>
						<div class="clear"></div>
					</div>
					<div class="form-container table-container">
						<form
							action="doCreate.action"
							method="post" id="crudForm" enctype="multipart/form-data"
							class="form-horizontal" accept-charset="utf-8">
							<div class="form-group customerName_form_group">
								<label class="col-sm-3 control-label"> 圖書名稱 </label>
								<div class="col-sm-9">
									<s:textfield id="bookName" name="book.bookName" class="form-control" value="" maxlength="50"></s:textfield>	
								</div>
							</div>
							<div class="form-group contactLastName_form_group">
								<label class="col-sm-3 control-label"> 圖書價格 </label>
								<div class="col-sm-9">
									<s:textfield id="bookPrice" name="book.bookPrice" class="form-control" value="0" maxlength="50"  onkeyup="return ValidateNumber($(this),value)"></s:textfield>
								</div>
							</div>
							<div class="form-group contactLastName_form_group">
								<label class="col-sm-3 control-label"> 圖書封面 </label>
								<div class="col-sm-9">
									<s:file name="uploadImg" label="Select a File to upload" size="40" cssClass="button-std" />
								</div>
							</div>
							<!-- Start of hidden inputs -->
							<!-- End of hidden inputs -->


							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-7">
									<button class="btn btn-default btn-success b10" type="button"
										id="form-button-save"
										onclick="doCreate();">
										<i class="fa fa-check"></i> 存檔
									</button>
									<button class="btn btn-default cancel-button b10" type="button"
										id="cancel-button"
										onclick="doSearch();">
										<i class="fa fa-warning"></i> 取消
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>