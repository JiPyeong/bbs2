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
	--font-size-mid: 14px;
	--big-width: 1000px;
	--mid-width: 700px;
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
.boardLayout {
	border-top: 1px solid var(--gray2);
}
.boardLayout tr {
	height: 35px;
	text-align: center;
	border-bottom: 1px solid var(--gray2);
}
.boardLayout td {
	padding: 10px 10px;
}
.boardLayout tr {
	text-align: left;
}
.boardLayout tr:nth-child(1) {
	text-align: center;
}
.boardLayout tr:nth-child(2) td:last-child {
	text-align: right;
}
.boardLayout tr:nth-child(3) {
	height: 200px;
}
.boardLayout tr:nth-child(3){
	height: 200px;
}


.btns {
	margin: 10px auto 25px;
}

.btns tr {
	text-align: center;
}

input, button, select {
	height: 27px;
	
}

.replyLayout {
	margin: 25px 0 20px;
}

.replyLayout {
}
</style>

<script type="text/javascript">

<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
function deleteBoard(num) {
	let query = "num="+num+"&${query}";
	let url = "${pageContext.request.contextPath}/bbs2Wood/delete.do?"+query;
	
	if(confirm("해당 게시글을 삭제 하시겠습니까? ")) {
		location.href = url;
	}
}
</c:if>

</script>

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
            <table class="boardLayout">
            	<tr>
            		<td colspan="2">${dto.subject}</td>
            	</tr>
            	<tr>
            		<td>이름: ${dto.userName}</td>
            		<td>${dto.created} | 조회 ${dto.hitCount}</td>
            	</tr>
            	<tr>
            		<td colspan="2" valign="top">${dto.content}</td> 
            	</tr>
            	<tr>
            		<td>첨&nbsp;&nbsp;부 : 
            			<c:if test="${not empty dto.saveFilename}">
            				<a href="${pageContext.request.contextPath}/bbs2Wood/download.do?num=${dto.num}">${dto.originalFilename}</a>
            				(<fmt:formatNumber value="${dto.fileSize/1024}" pattern="0.00"/> kb)
            			</c:if>
            		</td>
            	</tr>
            	<tr>
            		<td colspan="2">이전글 : 
            			<c:if test="${not empty preReadDto}">
            				<a href="${pageContext.request.contextPath}/bbs2Wood/article.do?${query}&num=${preReadDto.num}">${preReadDto.subject}</a>
            			</c:if>
            		</td>
            	</tr>
            	<tr>
            		<td colspan="2">다음글 : 
            			<c:if test="${not empty nextReadDto}">
            				<a href="${pageContext.request.contextPath}/bbs2Wood/article.do?${query}&num=${nextReadDto.num}">${nextReadDto.subject}</a>
            			</c:if>
            		</td>
            	</tr>
            </table>
            
            <table>
            	<tr height="45">
				    <td width="300" align="left">
				    	<c:choose>
				    		<c:when test="${sessionScope.member.userId==dto.userId}">
				    			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbs2Wood/update.do?num=${dto.num}&page=${page}';">수정</button>
				    		</c:when>
				    		<c:otherwise>
				    			<button type="button" class="btn" disabled="disabled">수정</button>
				    		</c:otherwise>
				    	</c:choose>
				    	
				    	<c:choose>
				    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
				    			<button type="button" class="btn" onclick="deleteBoard('${dto.num}');">삭제</button>
				    		</c:when>
				    		<c:otherwise>
				    			<button type="button" class="btn" disabled="disabled">삭제</button>
				    		</c:otherwise>
				    	</c:choose>
				    </td>
			
				    <td align="right">
				        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbs2Wood/list.do?${query}';">리스트</button>
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