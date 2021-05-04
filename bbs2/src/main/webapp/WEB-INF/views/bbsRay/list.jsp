<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
</style>
<script type="text/javascript">

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
        <div class="topLine">
        <div class="topLeft" style="float: left">총 {dataCount}건의 게시물</div>
        
        <div class="topRight"style="float: left">
        <form  name="serachForm" action="${pageContext.request.contextPath}/bbsRay/list.do" method="post">
	        <select class="selectBar">
	        	<option value="all"  ${condition=="all"?"selected='selected'":""}>제목+내용</option>
	        	<option value="subject"  ${condition=="subject"?"selected='selected'":""}>제목</option>
	        	<option value="content"  ${condition=="content"?"selected='selected'":""}>내용</option>
	        	<option value="userName"  ${condition=="userName"?"selected='selected'":""}>작성자</option>
	        	<option value="created"  ${condition=="created"?"selected='selected'":""}>등록일</option>
	        </select>
	        <input type="text" name= "keyword" class="searchBar" value="${keyword}">
	        <button type="button" class="btn" onclick="searchList()">검색</button>
        </form>
        </div>
        </div>
		<table style="width: 800px; margin-left: 50px;">
			<tr>
				<th>번호</th>
				<th>제목</th>	
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>		
			</tr>
			<tr>
				<td>{dto}</td>
				<td>{dto}</td>	
				<td>{dto.userName}</td>
				<td>{dto.created}</td>
				<td>{dto.hitCount}</td>		
			</tr>

		</table>
				<div style="float: right;">
				<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbsRay/created.do';">글올리기</button>
				</div>



	
	</div>
</div>	
	
<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html>