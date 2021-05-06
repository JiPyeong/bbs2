<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ray 게시판</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<style type="text/css">

*{
	padding: 0; margin: 0;
}

.body-container{
	width: 900px;
}
.boardTb{
	width: 700px;
	margin-left: 100px;
    border: 1px solid gray;
    border-collapse:  collapse;	
}
.line{
	height: 30px;
	line-height: 30px;	
}
.conLeft{
	width: 100px;
	text-align: center;
	height: 40px;
	line-height: 40px;
	background: #bbd7e8;
}

.conRight{
	width: 600px;
	padding-left: 10px;

}
.inputBar{
	width: 580px;
	
}
.subject{
	height: 20px;
	line-height: 20px;
}
.bottom{
	text-align: center;
	margin-top: 20px;
}

</style>
<script type="text/javascript">

function sendOk() {
	var f = document.boardForm;
	
	var str = f.subject.value;
	if(!str){
		alert("제목을 입력하세요.");
		f.subject.focus();
		return;
	}
	
	str = f.content.value;
	if(!str){
		alert("내용을 입력하세요.");
		f.content.focus();
		return;
	}
	
	f.action="${pageContext.request.contextPath}/bbsRay/${mode}_ok.do";

	f.submit();
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
	<div class="body-container">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 게시판 </h3>
        </div>
		<div>
			<form name="boardForm" method="post">
				<table class="boardTb">
					<tr class="line">
						<td class="conLeft">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
						<td class="conRight">
							<input type="text" name="subject" class="inputBar subject" value="${dto.subject}">						
						</td>
					</tr>
					
					<tr class="line">
						<td class="conLeft">작성자</td>
						<td class="conRight">
							${sessionScope.member.userName}
						</td>
					</tr>
					
					<tr class="line">
						<td class="conLeft">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
						<td class="conRight">
							<textarea rows="20" name="content" class="inputBar" style="resize: none;">${dto.content}</textarea>
						</td> 		
					</tr> 
				</table>
				
				<div>
					<div class="bottom">
						<c:if test="${mode=='update'}">
							<input type="hidden" name="num" value="${dto.num}">
							<input type="hidden" name="page" value="${page}">
						</c:if>
						<button type="button" class="btn" onclick="sendOk()">${mode=='update'?'수정완료':'등록하기' }</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbsRay/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
					</div>
				
				</div>
				
				
				
			</form>		
		</div>
	</div>
</div>
<div class="footer"> 
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>