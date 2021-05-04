package com.bbsTerry;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.CustomPaging;

@WebServlet("/bbsTerry/*")
public class TerryServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
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
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = new DAO();
		CustomPaging pg = new CustomPaging();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page !=null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "subject";
			keyword = "";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		int rows = 10;
		int total_page = pg.pageCount(rows, dataCount);
		if(current_page>total_page) {
			current_page=total_page;
		}
		
		int offset = (current_page - 1) * rows;
		if(offset<0) offset = 0;
		
		List<DTO> list = null;
		if(keyword.length()==0) {
			list=dao.list(offset, rows);
		} else {
			list = dao.list(offset, rows, condition, keyword);
		}
		
		int listNum;
		int n=0;
		
		for(DTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl = cp+"/bbsTerry/list.do";
		String articleUrl = cp+"/bbsTerry/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?"+query;
			articleUrl += "&"+query;
		}
		int a = 10;
		String paging = pg.paging(current_page, total_page, listUrl, a);
		
		req.setAttribute("list", list);
		if(list!=null) {
			List<DTO> rank = dao.rank();
			req.setAttribute("rank", rank);
		}
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/bbsTerry/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/bbsTerry/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = new DAO();
		DTO dto = new DTO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			dto.setUserName(info.getUserName());
			
			dao.insert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsTerry/list.do");
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		DAO dao = new DAO();
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			dao.updateHitCount(num);
			
			DTO dto = dao.read(num);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/bbsTerry/list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			DTO preReadDto = dao.preRead(num, condition, keyword);
			DTO nextReadDto = dao.nextRead(num, condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/bbsTerry/article.jsp");
			
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/bbsTerry/list.do?"+query);			
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = new DAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			DTO dto = dao.read(num);
			
			if(dto != null && dto.getUserId().equals(info.getUserId())) {
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/bbsTerry/created.jsp");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsTerry/list.do?page="+page);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = new DAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		
		try {
			DTO dto = new DTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.update(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsTerry/list.do?page="+page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = new DAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			dao.delete(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/bbsTerry/list.do?"+query);
	}
}
