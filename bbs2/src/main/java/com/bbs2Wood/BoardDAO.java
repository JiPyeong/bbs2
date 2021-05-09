package com.bbs2Wood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(BoardDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO bbs2Wood(num, userId, subject, content, saveFilename, originalFilename, "
					+ " filesize, hitCount, created) VALUES(bbs2Wood_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, 0, SYSDATE) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getSaveFilename());
			pstmt.setString(5, dto.getOriginalFilename());
			pstmt.setLong(6, dto.getFileSize());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT COUNT(*) FROM bbs2Wood";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT COUNT(*) FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ";
			if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?) > 0 OR INSTR(content, ?) > 0 ";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR("+condition+", ?) > 0 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) 
				pstmt.setString(2, keyword);
			
			rs = pstmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
	
	public List<BoardDTO> listBoard(int offset, int rows) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = " SELECT num, userName, subject, saveFilename, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created "
					+ " FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId "
					+ " ORDER BY num DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return list;
	}
	
	public List<BoardDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" SELECT num, userName, subject, saveFilename, hitCount, TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ");
			
			if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) > 0 OR INSTR(content, ?) > 0 ");
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\/|\\-|\\.)", "");
				sb.append(" WHERE TO_CHAR(created,'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR("+condition+" ,?) > 0");
			}
			sb.append(" ORDER BY num DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			pstmt = conn.prepareStatement(sb.toString());
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, rows);
			} else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return list;
	}
	
	public int updateHitCount(int num) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE bbs2Wood SET hitCount = hitCount+1 WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
	
	public BoardDTO readBoard(int num) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT num, b.userId, userName, subject, content, saveFilename, originalFilename, "
					+ " filesize, created, hitCount FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId "
					+ " WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("filesize"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return dto;
	}
	
	public BoardDTO preReadBoard(int num, String condition, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			if(keyword!=null && keyword.length() != 0) { // 검색 조건 있을 경우 쿼리문
				sb.append("SELECT num, subject FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE (INSTR(subject, ?) > 0 OR INSTR(content, ?) > 0) ");
				} else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\/|\\-|\\.)", "");
					sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
				} else {
					sb.append(" WHERE (INSTR("+condition+", ?) > 0) ");
				}
				sb.append(" AND (num > ?) ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ");
				pstmt = conn.prepareStatement(sb.toString());
				
				if(keyword.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, num);
				} else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, num);
				}
				
			} else { // 검색 조건 없을 경우 쿼리문
				sb.append("SELECT num, subject FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ");
				sb.append(" WHERE num > ? ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return dto;
	}
	
	public BoardDTO nextReadBoard(int num, String condition, String keyword) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			if(keyword!=null && keyword.length() != 0) { // 검색 조건 있을 경우 쿼리문
				sb.append("SELECT num, subject FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE (INSTR(subject, ?) > 0 OR INSTR(content, ?) > 0) ");
				} else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\/|\\-|\\.)", "");
					sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
				} else {
					sb.append(" WHERE (INSTR("+condition+", ?) > 0) ");
				}
				sb.append(" AND (num < ?) ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ");
				pstmt = conn.prepareStatement(sb.toString());
				
				if(keyword.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, num);
				} else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, num);
				}
				
			} else { // 검색 조건 없을 경우 쿼리문
				sb.append("SELECT num, subject FROM bbs2Wood b JOIN member1 m ON b.userId=m.userId ");
				sb.append(" WHERE num < ? ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return dto;
	}
	
	public int updateBoard(BoardDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE bbs2Wood SET subject=?, content=?, saveFilename=?, originalFilename=?, filesize=? WHERE num=? AND userId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getSaveFilename());
			pstmt.setString(4, dto.getOriginalFilename());
			pstmt.setLong(5, dto.getFileSize());
			pstmt.setInt(6, dto.getNum());
			pstmt.setString(7, dto.getUserId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
	
	public int deleteBoard(int num, String userId) throws SQLException  {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			if(userId.equals("admin")) { // 운영자도 삭제 가능 - 추후 롤로 대체 가능
				sql = "DELETE FROM bbs2Wood WHERE num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				result = pstmt.executeUpdate();
			} else { // 본인일때만 삭제 가능
				sql = "DELETE FROM bbs2Wood WHERE num=? AND userId=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.setString(2, userId);
				result = pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		} 
		return result;
	}
}
