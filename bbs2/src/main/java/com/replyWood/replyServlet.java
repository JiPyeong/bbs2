package com.replyWood;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;

@WebServlet("/replyWood/*")
public class replyServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
		if(uri.indexOf("register.do") != -1) {
			register(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}
	}
	
	protected void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String page = req.getParameter("page");
		String cp = req.getContextPath();
		replyDAO dao = new replyDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		replyDTO dto = new replyDTO();
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			dto.setNum(num);
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.insertReply(dto);
			
			resp.sendRedirect(cp+"/bbsWood/article.do?page="+page+"&num="+num);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

}
