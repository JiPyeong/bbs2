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
		<p>
		    <a href="${pageContext.request.contextPath}/bbs2Ray/list.do">Ray</a>
		</p>
		<p>
		    <a href="${pageContext.request.contextPath}/bbs2Ro/list.do">Ro</a>
		</p>
		<p>
		    <a href="${pageContext.request.contextPath}/bbs2Terry/list.do">Terry</a>
		</p>
		<p>
		    <a href="${pageContext.request.contextPath}/bbs2Wood/list.do">Wood</a>
		</p>
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/></body>
</html>