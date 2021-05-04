 package com.bbsRay;

import java.io.IOException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/bbsRay/*")
public class rayServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}		
		
	}
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		rayDAO dao = new rayDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int currentPage = 1;
		if(page != null)
		currentPage = Integer.parseInt(page);
		
		String condition = req.getParameter("condtition");
		String keyword = req.getParameter("keyword");
		if(condition==null)
			condition = "all";
			keyword ="";
	
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "UTF-8");
		}
	
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount();
		}else {
			dataCount = dao.dataCount(condition, keyword);
		}
		int rows = 10;
		int totalPage = util.pageCount(rows, dataCount);
		if(currentPage > totalPage)
			currentPage = totalPage;
		
		int offset = (currentPage -1 )* rows;
		if(offset < 0) offset =0;
		
		List<DTO> list = null;
		if(keyword.length() == 0) {
			list = dao.listRay(offset, rows);
		}else {
			list = dao.listRay(offset, rows, condition, keyword);
		}
		
		int listNum, n=0;
		for(DTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);;
			n++;
		}
		String query="";
		if(keyword.length() != 0) {
			query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		
		String listUrl = cp+"/bbsRay/list.do";
		String articleUrl = cp+"/bbsRay/article.do?page="+currentPage;
		if(query.length()!=0) {
			listUrl += "?" + query;
			articleUrl += "&" +query;
		}
		String paging = util.paging(currentPage, totalPage, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", currentPage);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", totalPage);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);	
		
		forward(req, resp, "/WEB-INF/views/bbsRay/list.jsp");
	}
	
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/bbsRay/created.jsp");
	}
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		rayDAO dao = new rayDAO();
		DTO dto = new DTO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.insertRay(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsRay/list.do");
		
	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		rayDAO dao = new rayDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword ="";
			}
			keyword = URLDecoder.decode(keyword, "UTF-8");
			
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			dao.upHitCount(num);
			
			DTO dto = dao.readRay(num);
			if(dto == null) {
				resp.sendRedirect(cp+"/bbsRay/list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			DTO prRayDTO = dao.preReadRay(num, condition, keyword);
			DTO neRayDTO = dao.nextReadRay(num, condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("prRayDTO", prRayDTO);
			req.setAttribute("neRayDTO", neRayDTO);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/bbsRay/article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbsRay/list.do?"+query);
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		rayDAO dao = new rayDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			DTO dto = dao.readRay(num);
			
			if(dto!=null && dto.getUserId().equals(info.getUserId())) {
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/bbsRay/created.jsp");
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsRay/list.do?page="+page);
		
	}
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		rayDAO dao = new rayDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		
		try {
			DTO dto = new DTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.updateRay(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsRay/list.do?page="+page);
				
		
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		rayDAO dao = new rayDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			dao.deleteRay(num, info.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsRay/list.do?"+query);
		
		
	}
}
