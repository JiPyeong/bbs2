<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>bbsWood</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
:root {
	<!--/* color: 숫자가 커질수록 옅게 */--!>
	--gray1 : #787878;
	--gray2 : #cccccc;
	--gray3 : #f8f8f8;
}
* {
	box-sizing: border-box;
}
table {
	width: 100%;
	margin: 0 auto;
	border-spacing: 0;
	border-collapse: collapse;
}
tr {
	align-items: center;
	height: 35px;
}
td, th {
	line-height: 35px;
	text-align: center;
	width: 100px;
}

.board-title td {
	text-align: left;
	padding-left: 10px;
}

.board tr {
	border-bottom: 1px solid var(--gray2);
}
.board tr:first-child {
	background: var(--gray3);
}

.board td:nth-child(1), .board th:nth-child(1) {
	width: 80px;
}
.board td:nth-child(2), .board th:nth-child(2) {
	width: 300px;
}
.board td:nth-child(2) {
	padding-left: 15px;
	text-align: left;
}
.board td:nth-child(4), .board th:nth-child(4) {
	width: 120px;
}
.board {
	border-top: 1px solid var(--gray2);
}

.btns {
	margin: 10px auto 25px;
}

.btns td:first-child {
	text-align: left;
	padding-left: 10px;
	width: 150px;
}
.btns td:nth-child(2) {
	width: 400px;
}
.btns td:last-child {
	text-align: right;
	padding-right: 10px;
	width: 150px;
}

input, button, select {
	height: 27px;
	
}

</style>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><i class="fas fa-chalkboard"></i> 게시판 </h3>
        </div>
        
        <div>
            <table class="board-title">
            	<tr>
            		<td> 2개(1/1 페이지)</td>
            	</tr>
            </table>
            
            <table class="board">
            	<tr class="tr-first">
            		<th>번호</th>
            		<th>제목</th>
            		<th>작성자</th>
            		<th>작성일</th>
            		<th>조회수</th>
            	</tr>
            	
            	<c:forEach var="dto" items="${list}">
            		<tr>
            			<td>${dto.listNum}</td>
            			<td> <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a> </td>
            			<td>${dto.userName}</td>
            			<td>${dto.created}</td>
            			<td>${dto.hitCount}</td>
            		</tr>
            	</c:forEach>
            </table>
            
            <div>
            	${dataCount==0?"등록된 게시물이 없습니다.":paging}
            </div>
            
            <table class="btns">
            	<tr>
            		<td>
		            	<button type="button"  class="btn">새로고침</button>
            		</td>
            		<td>
		            	<form method="post">
		            		<select class="selectField">
		            			<option>제목+내용</option>
		            			<option>작성자</option>
		            		</select> 
		            		<input name="keyword" type="text" class="boxTF">
		            		<button type="submit" class="btn">검색</button>
	           			</form>
            		</td>
            		<td>
		            	<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbsWood/created.do'">글올리기</button>
            		</td>
            	</tr>
            </table>
        </div>
        
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>