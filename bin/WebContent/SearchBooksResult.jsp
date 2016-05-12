<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:iterator value="bookList" status="index1">
	<tr>
		<td class="no-border-right"></td>
		<td class="no-border-left">
			<div>
				<a class="btn btn-default"
					href="${pageContext.request.contextPath}/goUpdate.action?id=<s:property value="id"/>"><i
					class="fa fa-pencil"></i> 修改</a> 
				<a class="btn btn-default"
					href="javascript:checkDelete('<s:property value="id"/>','<s:property value="bookName"/>');"><i
					class="fa fa-trash-o"></i> 刪除</a>

			</div>
		</td>
		<td><s:property value="bookName" /></td>
		<td><s:property value="bookPrice" /></td>
		<td><s:if test="hasImage()">
				<img width="180px" height="180px"
					src='${pageContext.request.contextPath}/<s:property value="bookImage"/>'>
			</s:if> <s:else>
												未上傳圖片
											</s:else></td>
		<td><s:date name="createTime" format="MM/dd/yyyy HH:mm:ss" /></td>
	</tr>
</s:iterator>
<script type="text/javascript">
function checkDelete(id, bookName) {
	if (confirm("是否刪除:"+bookName)) {
		window.open("${pageContext.request.contextPath}/doDelete.action?id="+id, '_self');
	}
}
</script>