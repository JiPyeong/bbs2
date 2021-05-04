package com.util;

public class CustomPaging {
	/**
	 * 전체 페이지 수 구하기
	 * @param rows		한 화면에 표시할 데이터 개수
	 * @param dataCount	전체 데이터 개수
	 * @return			총 페이지 수
	 */
	public int pageCount(int rows, int dataCount) {
		if(dataCount <=0)
			return 0;
		
		return dataCount / rows + (dataCount % rows > 0? 1 : 0);
	}
	
	/**
	 * 
	 * @param current_page	현재 화면에 표시할 페이지 번호
	 * @param total_page	전체 페이지 수
	 * @param list_url		링크를 설정할 url
	 * @param numPerBlock	하단 페이지 목록 개수
	 * @return				페이징 처리 결과
	 */
	public String paging(int current_page, int total_page, String list_url, int numPerBlock) {
		StringBuilder sb = new StringBuilder();
		
		int currentPageSetup;
		int n, page;
		
		if(current_page < 1 || total_page < 1)
			return "";
		
		if(list_url.indexOf("?") != -1) {
			list_url += "&";
		} else {
			list_url += "?";
		}
		
		// currentPageSetup : 표시할 첫 페이지 - 1
		currentPageSetup = (current_page / numPerBlock) * numPerBlock;
		if(current_page % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		sb.append("<div id='paginate'>");
		
		// 처음페이지(1페이지), 이전(이전페이지의 끝)
		
		if(total_page > numPerBlock && currentPageSetup >0) {
			sb.append("<a href='"+list_url+"page=1'>[처음]</a>");
			sb.append("&nbsp;<a href='"+list_url+"page="+currentPageSetup+"'>[이전]</a>");
		}
		
		// 페이징 처리
		page = currentPageSetup + 1;
		while(page <= total_page && page <= (currentPageSetup+numPerBlock)) {
			if(page == current_page) {
				sb.append("&nbsp;<span style='color: Fuchsia;'>"+page+"</span>");
			} else {
				sb.append("&nbsp;<a href='"+list_url+"page="+page+"'>"+page+"</a>");
			}
			page++;
		}
		
		// 다음(다음 페이지의 처음), 마지막 페이지
		n = currentPageSetup + numPerBlock+1;
		if(n > total_page) n = total_page;
		if(total_page - currentPageSetup > numPerBlock) {
			sb.append("&nbsp;<a href='"+list_url+"page="+n+"'>[다음]</a>");
			sb.append("&nbsp;<a href='"+list_url+"page="+total_page+"'>[끝]</a>");
		}
		sb.append("</div>");
		
		return sb.toString();
	}
}