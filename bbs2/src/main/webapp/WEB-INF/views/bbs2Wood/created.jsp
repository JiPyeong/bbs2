<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>bbs2Wood</title>
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
.boardLayout {
	border-top: 1px solid var(--gray2);
}
.boardLayout tr {
	height: 40px;
	text-align: center;
	border-bottom: 1px solid var(--gray2);
}
.boardLayout tr:nth-child(3) {
	height: 180px;
}
.boardLayout td:first-child {
	background: var(--gray3);
	width: 100px;
}
.boardLayout td:last-child {
	text-align: left;
	padding-left: 10px;
	width: 600px;
}
.boardLayout input {
	width: 580px;
}
.boardLayout textarea {
	width: 580px;
}

input[name=selectFile] {
	width: 290px;
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

</style>

<script type="text/javascript">
$(function() {
	$("form #submitBtn").click(function() {
		let b = true;
		$("#boardForm input[name=subject], input[name=content]").not("button").each(function() {
			if(! $(this).val().trim()) {
				b = false;
				alert($(this).closest("tr").find("td:first").text().replaceAll(/\s/g,"")+"을(를) 입력하세요.");
				$(this).focus();
				return false;
			}
		});
		
		if(b) $("#boardForm").attr("action","${pageContext.request.contextPath}/bbs2Wood/${mode}_ok.do").submit();
		
	});
})

<c:if test="${mode=='update'}">
	function deleteFile(num) {
		let url = "${pageContext.request.contextPath}/bbs2Wood/deleteFile.do?num="+num+"&page=${page}";
		location.href = url;
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
            <form name="boardForm" id="boardForm" method="post" enctype="multipart/form-data">
            	<table class="boardLayout">
            		<tr>
            			<td>제&nbsp;&nbsp;목</td>
            			<td>
            				<input name="subject" type="text" value="${dto.subject}">
            			</td>
            		</tr>

            		<tr>
            			<td>작성자</td>
            			<td>${sessionScope.member.userName}</td>
            		</tr>
            		
            		<tr>
            			<td>내&nbsp;&nbsp;용</td>
            			<td>
            				<textarea name="content" class="boxTA">${dto.content}</textarea>
            			</td>
            		</tr>
            		
            		<tr>
            			<td>첨&nbsp;&nbsp;부</td>
            			<td>
            				<input type="file" name="selectFile" class="boxTF" size="53">
            			</td>
            		</tr>
            		
            		<c:if test="${mode=='update'}">
            		<tr>
            			<td>첨부된파일</td>
            			<td>
            				<c:if test="${not empty dto.saveFilename}">
            				<a href="javascript:deleteFile('${dto.num}')"><i class="far fa-trash-alt"></i></a>
            				${dto.originalFilename}
            				</c:if>
            			</td>
            		</tr>	
            		</c:if>
            		
            	</table>
            	
            	<table class="btns">
            		<tr>
            			<td>
            				<button id="submitBtn" type="button" class="btn">${mode=='update'?'수정완료':'등록하기'}</button>
            				<button type="reset" class="btn">다시입력</button>
            				<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbs2Wood/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
            			</td>
            			<c:if test="${mode=='update'}">
            				<input type="hidden" name="num" value="${dto.num}">
			        	 	<input type="hidden" name="page" value="${page}">
			        	 	<input type="hidden" name="fileSize" value="${dto.fileSize}">
			        	 	<input type="hidden" name="saveFilename" value="${dto.saveFilename}">
			        	 	<input type="hidden" name="originalFilename" value="${dto.originalFilename}">
            			</c:if>
            		</tr>
            	</table>
            </form>
        </div>
        
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>