<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
	<div class="body-container" style="width: 700px;">
		<table style="box-sizing:border-box; border-spacing: 0; border-collapse: collapse; border: 1px solid black;">
			<tr height="35" style="border-bottom: 1px solid black;">
				<td width="220">게시판(일반)
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbsRay/list.do">Ray</a>
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbsRo/list.do">Ro</a>
				</td >
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbsTerry/list.do">Terry</a>
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbsWood/list.do">Wood</a>
				</td>
			</tr>	
				
			<tr height="35" style="border-bottom: 1px solid black;">
				<td width="220">게시판(첨부파일)
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbs2Ray/list.do">Ray</a>
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbs2Ro/list.do">Ro</a>
				</td >
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbs2Terry/list.do">Terry</a>
				</td>
				<td width="120">
				    <a href="${pageContext.request.contextPath}/bbs2Wood/list.do">Wood</a>
				</td>
			</tr>	
				
		</table>
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/></body>
</html>