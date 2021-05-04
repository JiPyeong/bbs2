package com.bbsRay;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class rayDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertRay(DTO dto)throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO ray(num, subject, content, userId, hitCount, created) "
					+" VALUES(ray_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		return result;
	};
	
	public DTO readRay(int num) {
		DTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, r.userId, userName, subject, content, hitCount, created "
					+" FROM ray r "
					+" JOIN member1 m ON r.userId = m.userId "
					+" WHERE num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		
		
		
		return dto;
	};	
	
	public int updateRay(DTO dto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE ray SET subject = ?, content = ? "
					+" WHERE num = ? AND userId = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			pstmt.setString(4, dto.getUserId());
			
			result = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		
		return result;
	};
	
	public int deleteRay(int num, String userId)throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM ray WHERE num = ? ";
					if(! userId.equals("admin")) {
						sql+=" AND userId = ? ";
					}
				 pstmt = conn.prepareStatement(sql);
				 
				 pstmt.setInt(1, num);
				 
				 if(! userId.equals("admin")) {
					 pstmt.setString(2, userId);
				 }
				 result = pstmt.executeUpdate();
				 
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		
		return result;
	};
	
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			sql = " SELECT COUNT(*) FROM ray ";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		
		
		return result;
	};
	
	public List<DTO> listRay(int offset, int rows){
		List<DTO> list = new ArrayList<DTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT num, userName, subject, hitCount, TO_CHAR(created,'YYYY-MM-DD')created "
					+ " FROM ray r"
					+ " JOIN member1 m ON r.userId = m.userId "
					+ " ORDER BY num DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				DTO dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));				
				
				list.add(dto);
				
				
			};
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		
		return list;
	}
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT COUNT(*) FROM ray r ");
			sb.append(" JOIN member1 m 0N r.userId = m.userId ");
			if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ? ) >=1 OR INSTR(content, ? ) >= 1");
			}else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			}else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >=1 ");
			}
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
			}

			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		
		return result;
	}
	
	public List<DTO> listRay(int offset, int rows, String condition, String keyword){
		List<DTO> list = new ArrayList<DTO>();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT num, userName, subject, hitCount, TO_CHAR(created, 'YYYY-MM-DD')created ");
			sb.append(" FROM ray r ");
			sb.append(" JOIN member1 m 0N r.userId = m.userId ");
			if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >=1 OR instr(content, ?) >=1 ");
			}else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			}else {
				sb.append(" WHERE INSTR(" +condition+", ?) >=1" );
			}
			sb.append(" ORDER BY num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
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
				DTO dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		
		return list;
	}
	
	public int upHitCount(int num)throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE ray SET hitCount = hitCount +1 WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					
				}
			}
		}
	
		return result;
	}
	
	public DTO preReadRay(int num, String condition, String keyword) {
		DTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword.length()!=0) {
				sb.append("SELECT num, subject FROM ray r");
				sb.append(" JOIN member1 m ON r.userId = m.userId ");
				if(condition.equals("all")) {
					sb.append("WHERE (INSTR(subject, ?) >=1 OR INSTR(content, ?) >=1) ");
				}else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				}else {
					sb.append(" WHERE (INSTR"+condition+",?) > 0)");
				}
				sb.append("  AND (num > ?) ");
				sb.append(" ORDER BY num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			
				pstmt =conn.prepareStatement(sb.toString());
				
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, num);
				}else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, num);
				}
	
			}else {
				sb.append(" SELECT num, subject FROM ray ");
				sb.append(" WHERE num > ? ");
				sb.append(" ORDER BY num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				
			}

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		
		
		
		return dto;
	}
	
	public DTO nextReadRay(int num, String condition, String keyword) {
		DTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword.length()!=0) {
				sb.append("SELECT num, subject FROM ray r");
				sb.append(" JOIN member1 m ON r.userId = m.userId ");
				if(condition.equals("all")) {
					sb.append("WHERE (INSTR(subject, ?) >=1 OR INSTR(content, ?) >=1) ");
				}else if(condition.equals("created")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				}else {
					sb.append(" WHERE (INSTR"+condition+",?) > 0)");
				}
				sb.append("  AND (num < ?) ");
				sb.append(" ORDER BY num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
			
				pstmt =conn.prepareStatement(sb.toString());
				
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, num);
				}else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, num);
				}
	
			}else {
				sb.append(" SELECT num, subject FROM ray ");
				sb.append(" WHERE num < ? ");
				sb.append(" ORDER BY num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, num);
				
			}

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		
		
		
		return dto;
	}
	
	}	
	

