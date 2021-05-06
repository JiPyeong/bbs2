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
.boardLayout tr:nth-child(2) td:first-child {
	text-align: left;
}
.boardLayout tr:nth-child(4), .boardLayout tr:nth-child(5) {
	text-align: left;
}
.boardLayout tr:nth-child(2) td:last-child {
	text-align: right;
}
.boardLayout tr:nth-child(3) {
	text-align: left;
	height: 200px;
}
.boardLayout tr:nth-child(3){
	text-align: left;
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
$(function(){
	// 댓글등록
	$("body").on("click", ".btnSendReply", function(){
		var num = "${dto.num}";
		var $tb = $(this).closest("table");
		var content = $tb.find("textarea").val();
		
		if(! content) {
			$tb.find("textarea").focus();
			return false;
		}
		
		content = encodeURIComponent(content);
		
		var query = "num="+num+"&content="+content+"&answer=0";
		location.href="${pageContext.request.contextPath}/replyWood/register.do?"+query;
		
	});
});

// 댓글 삭제
$(function(){
	$("body").on("click", ".deleteReply", function(){
		if(! confirm('댓글을 삭제 하시겠습니까?')) {
			return false;
		}
		
		var replyNum = $(this).attr("data-replyNum");
		var page = $(this).attr("data-pageNo");
		
		alert("삭제 댓글 번호: "+replyNum+", 페이지번호 :"+page);
	});

});

// 댓글 좋아요 / 싫어요
$(function(){
	// 댓글 좋아요 / 싫어요 등록
	$("body").on("click", ".btnSendReplyLike", function(){
		var replyNum = $(this).attr("data-replyNum");
		var replyLike = $(this).attr("data-replyLike");
		
		var msg = "싫어요를 누르시겠습니까? ";
		if(replyLike === "1") {
			msg = "좋아요를 누르시겠습니까? ";
		}
		
		if(! confirm(msg)) {
			return false;
		}
		
		alert("댓글번호 : "+replyNum+", 좋아요/싫어요 : "+replyLike);
		
		// 좋아요/싫어요 값 할당
		var $btn = $(this);
		$btn.parent("td").children().eq(0).find("span").text("5");
		$btn.parent("td").children().eq(1).find("span").text("4");
		
	});

});

// 댓글별 답글 리스트
function listReplyAnswer(answer) {

}

// 댓글별 답글 개수
function countReplyAnswer(answer) {

}

// 답글 버튼(댓글별 답글 등록폼 및 답글리스트)
$(function(){
	$("body").on("click", ".btnReplyAnswerLayout", function(){
		var $trReplyAnswer = $(this).closest("tr").next();
		var replyNum = $(this).attr("data-replyNum");
		
		var isVisible = $trReplyAnswer.is(":visible");
		if(isVisible) {
			$trReplyAnswer.hide(150); // 150ms 만큼 애니메이션
		} else {
			$trReplyAnswer.show(150);
		}
	});
	
});

// 댓글별 답글 등록
$(function(){
	$("body").on("click", ".btnSendReplyAnswer", function(){
		var num = "10";
		var replyNum = $(this).attr("data-replyNum");
		
		var $td = $(this).closest("td");
		var content = $td.find("textarea").val().trim();
		if(! content) {
			$td.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		
		var query = "num="+num+"&content="+content+"&answer="+replyNum;
		alert(query);
	});
});

// 댓글별 답글 삭제
$(function(){
	$("body").on("click", ".deleteReplyAnswer", function(){

	});
});
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
            		<td colspan="2">이전글 : 
            			<c:if test="${not empty preReadDto}">
            				<a href="${pageContext.request.contextPath}/bbsWood/article.do?${query}&num=${preReadDto.num}">${preReadDto.subject}</a>
            			</c:if>
            		</td>
            	</tr>
            	<tr>
            		<td colspan="2">다음글 : 
            			<c:if test="${not empty nextReadDto}">
            				<a href="${pageContext.request.contextPath}/bbsWood/article.do?${query}&num=${nextReadDto.num}">${nextReadDto.subject}</a>
            			</c:if>
            		</td>
            	</tr>
            </table>
            
            <table>
            	<tr height="45">
			    <td width="300" align="left">
			    	<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId}">
			    			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbs/update.do?num=${dto.num}&page=${page}';">수정</button>
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
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/bbs/list.do?${query}';">리스트</button>
			    </td>
			</tr>
            </table>
            
            
            
            
            
            <table style='width: 100%; margin: 15px auto 0; border-spacing: 0;'>
          <tr height='30'> 
           <td align='left' >
           	<span style='font-weight: bold;' >댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가 주세요.</span>
           </td>
          </tr>
          <tr>
             <td style='padding:5px 5px 0px;'>
                  <textarea class='boxTA' style='width:99%; height: 70px;'></textarea>
              </td>
          </tr>
          <tr>
             <td align='right'>
                  <button type='button' class='btn btnSendReply' style='padding:10px 20px;'>댓글 등록</button>
              </td>
          </tr>
     </table>
          
     <div id="listReply">
		<table style='width: 100%; margin: 10px auto 30px; border-spacing: 0;'>
			<thead id="listReplyHeader">
				<tr height="35">
				    <td colspan='2'>
				       <div style="clear: both;">
				           <div style="float: left;"><span style="color: #3EA9CD; font-weight: bold;">댓글 50개</span> <span>[댓글 목록, 1/10 페이지]</span></div>
				           <div style="float: right; text-align: right;"></div>
				       </div>
				    </td>
				</tr>
			</thead>
			<tbody id="listReplyBody">
				<c:forEach var="dto" items="${rList }">
				    <tr height='35' style='background: #eeeeee;'>
				       <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-right:none;'>
				           <span><b>${dto.userName }</b></span>
				        </td>
				       <td width='50%' style='padding:5px 5px; border:1px solid #cccccc; border-left:none;' align='right'>
				           <span>${dto.created}</span> |
				           <span class="deleteReply" style="cursor: pointer;" data-replyNum='310' data-pageNo='1'>삭제</span>
				        </td>
				    </tr>
				    <tr>
				        <td colspan='2' valign='top' style='padding:5px 5px;'>${dto.content}</td>
				    </tr>
				    
				    <tr>
				        <td style='padding:7px 5px;'>
				            <button type='button' class='btn btnReplyAnswerLayout' data-replyNum='310'>답글 <span id="answerCount310">0</span></button>
				        </td>
				        <td style='padding:7px 5px;' align='right'>
				            <button type='button' class='btn btnSendReplyLike' data-replyNum='310' data-replyLike='1'>좋아요 <span>5</span></button>
	                         <button type='button' class='btn btnSendReplyLike' data-replyNum='310' data-replyLike='0'>싫어요 <span>2</span></button>
				        </td>
				    </tr>
				</c:forEach>
			    

			
			</tbody>
			<tfoot id="listReplyFooter">
				<tr height='40' align="center">
		            <td colspan='2' >
		              1 2 3
		            </td>
	            </tr>
			</tfoot>
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