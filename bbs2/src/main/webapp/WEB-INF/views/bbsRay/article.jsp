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

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
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
	margin-top : 30px;
    border: 1px solid gray;
    border-collapse: collapse;	
}

.line{
    height: 50px;
    line-height: 50px;
    padding-left: 10px;
    background: #bbd7e8;
    border-bottom: 1px solid gray;
}

.subject{
	width: 100%;
	text-align: center;
}


.content{
	height: 200px;
	padding: 20px;
	background: white;
}

.top{
	text-align: center;
	height: 10px;
}

.bottom{
	width: 300px;
    margin: auto;
    margin-top: 20px;
    padding-left: 100px;
}
	


.btm{
	float: right;
	margin-left: 10px;
}

.imgs{
	width: 50px;
    float: left;
    margin-right: 20px;
	cursor: pointer;	
}

.likebtn{
	border: none;

}

</style>

<script type="text/javascript">


<c:if test = "${dto.userId == sessionScope.member.userId || sessionScope.member.userId == 'admin'}">


function deleteBoard(num) {
	if(confirm("게시물을 삭제 하시겠습니까?")){
		var url = "${pageContext.request.contextPath}/bbsRay/delete.do?num="+num+"&${query}";
		location.href=url;
	}
}
</c:if>

/*
 * 1.db에 저장을 안해서 페이지 나가면 초기화가 됨
   2.한사람 당 좋아요 싫어요 합쳐서 한번밖에 못누르니까 이런방식으로 씀. 
   		만일 좋아요, 싫어요 각 한 번씩 누르게 하고 싶으면 다른방법
  		
 */

//버튼을 한번만 누를 수 있게 만드는 함수
//false를 리턴하면 값을 증가시키고 / true를 리턴하면 버튼 클릭을 막는다

var doubleSubmitFlag = false; // 1. 처음에 doubleSubmitFlag 변수에 false를 넣어놓는다

function doubleSubmitCheck() {
	
	if(doubleSubmitFlag){ // 3. 두번째부터는 flag변수가 true를 갖고 있으니까 여기로 들어온다
	
		return doubleSubmitFlag; //그냥 나감(flag변수 = true, return값도 true)
	
	}else{ // 2. 처음 변수가 false니까 여기로 와서 
		doubleSubmitFlag = true; //변수에 true를 넣고 
		return false;	//false를 리턴

	}
}

/*
 
 false를 리턴하면 버튼을 막게 하기
	var SubmitFlag = true;
	function SubmitCheck() {
	
	if(SubmitFlag){ //true로 들어와서
		SubmitFlag = false; //변수에 false를 넣고 
		return true;	//true를 리턴
	
	}else{  
		return SubmitFlag
	}
}
	아래코드도 if(doubleSubmitCheck()==false)return;로 변경해야함
 */



$(function(){
	$(".png1").click(function(){
		if(doubleSubmitCheck())return;
		
		var msg = "게시글에 공감 하시겠습니까?"
		
		if(! confirm(msg)){
			return false;
		}
		

		var likeCount = parseInt($("#like").text());
		likeCount = likeCount+1;
		$("#like").text(likeCount);

	});
	
	$(".png2").click(function(){
		if(doubleSubmitCheck())return;
	

		var msg = "게시글이 마음에 안드시나요?"
			
			if(! confirm(msg)){
				return false;
			}
					
		
		var dislikeCount = parseInt($("#dislike").text());
		dislikeCount = dislikeCount+1;
		$("#dislike").text(dislikeCount);

	});	

});


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
          		   <div >
	        	       <div class="top">
	        			<c:choose>
	        			<c:when test="${dto.userId == sessionScope.member.userId}">
	        				<button type="button" class="btn" style="float: right;" onclick="javascript:location.href='${pageContext.request.contextPath}/bbsRay/update.do?num=${dto.num}&page=${page}';">수정</button>
	        			</c:when>
	        			<c:otherwise>
	        				<button type="button" class="btn" style="float: right;" disabled="disabled">수정</button>
	        			</c:otherwise>
	        			</c:choose>
	        			<c:choose>
	        				<c:when test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId =='admin'}">
	        					<button type="button" class="btn" style="float: right;" onclick="deleteBoard('${dto.num}');">삭제</button>
	        				</c:when>
	        				<c:otherwise>
	        					<button type="button" class="btn" style="float: right;" disabled="disabled">삭제</button>
	        				</c:otherwise>
	        			</c:choose>
	        			<div>
	        				<button type="button" class="btn" style="float: left;" onclick="javascript:location.href='${pageContext.request.contextPath}/bbsRay/list.do?${query}';">리스트</button>
	        			</div>
	        			</div>
        			</div>
        	<table class="boardTb">
				<tr class="line">
					<td class="subject">
						${dto.subject}
					</td>
				</tr>
				<tr class="line">
					<td style="float: left; padding-left: 10px">
						${dto.userName}
					</td>
					<td style="float: right;  padding-right: 10px">
						${dto.created} |
						조회${dto.hitCount}
					</td>
				</tr>
				
				<tr class="line">
					<td class="content">${dto.content}</td>
				</tr>
				
				<tr>
					<td class="line">
						이전글 
						<c:if test="${not empty prRayDTO}">
							<a href="${pageContext.request.contextPath}/bbsRay/article.do?num=${prRayDTO.num}&${query}">${prRayDTO.subject}</a>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="line" >
						다음글 
						<c:if test="${not empty neRayDTO}">
							<a href="${pageContext.request.contextPath}/bbsRay/article.do?num=${neRayDTO.num}&${query}">${neRayDTO.subject}</a>
						</c:if>
					</td>
				</tr>
        	</table>
    
				<div class="bottom">
					<button type="button" class="btn likebtn">
						<img alt="" class="imgs png1" src="${pageContext.request.contextPath}/resource/images/up1.png">
						<span id="like" >0</span>
					</button>
					
					<button type="button" class="btn likebtn">
						<img alt="" class="imgs png2" src="${pageContext.request.contextPath}/resource/images/down1.png">
						<span id="dislike">0</span>
					</button>
				</div>
        </div>
	</div>
</div>
<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>