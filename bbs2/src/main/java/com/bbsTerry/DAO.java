package com.bbsTerry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class DAO {
	private Connection conn = DBConn.getConnection();
	
	public int insert(DTO dto) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO terryBoard(num, subject, content, userId, userName, hitCount, created) "
					+ "VALUES(terry_seq.NEXTVAL, ?, ?, ?, ?, 0, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getUserId());
			pstmt.setString(4, dto.getUserName());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
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
			sql = "SELECT COUNT(*) FROM terryBoard";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	
	public List<DTO> list(int offset, int rows){
		
		List<DTO> list = new ArrayList<DTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, subject, m1.userName, TO_CHAR(created, 'YYYY-MM-DD') created, hitCount "
					+ "From terryBoard tb "
					+ "JOIN member1 m1 ON tb.userId = m1.userId "
					+ "ORDER BY num DESC "
					+ "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				DTO dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setUserName(rs.getString("userName"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM terryBoard tb JOIN member1 m1 ON tb.userId = m1.userId";
			if(condition.equals("all")) {
				sql += " WHERE INSTR(subject, ?)>=1 OR INSTR(content, ?)>=1";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR(" + condition + ", ?)>=1";
			}
			pstmt = conn.prepareStatement(sql);
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
		} finally {
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
	
	public List<DTO> list(int offset, int rows, String condition, String keyword){
		
		List<DTO> list = new ArrayList<DTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, subject, m1.userName, TO_CHAR(created, 'YYYY-MM-DD') created, hitCount "
					+ "From terryBoard tb "
					+ "JOIN member1 m1 ON m1.userId = tb.userId ";
			if(condition.equals("all")) {
				sql += "WHERE INSTR(subject, ?)>=1 OR INSTR(content, ?)>=1";
			} else if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
			} else {
				sql += "WHERE INSTR("+condition+", ?)>=1";
			}
			sql += "ORDER BY num DESC "
					+ "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			
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
				dto.setSubject(rs.getString("subject"));
				dto.setUserName(rs.getString("userName"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	
	public int updateHitCount(int num) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE terryBoard SET hitCount = hitCount + 1 WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public DTO read(int num) {
		DTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, tb.userId, tb.userName, subject, content, hitCount, created "
					+ "FROM terryBoard tb "
					+ "JOIN member1 m1 ON tb.userId = m1.userId "
					+ "WHERE num = ?";
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
		} finally {
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
		return dto;
	}
	
	public List<DTO> rank() {
		List<DTO> list = new ArrayList<DTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT * FROM (SELECT num, subject, hitCount, ROW_NUMBER() OVER(ORDER BY hitCount DESC) rank "
					+ "FROM terryBoard) WHERE rank <=10";
			
			pstmt=conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				DTO dto = new DTO();
				dto.setNum(rs.getInt("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setRank(rs.getInt("rank"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	public DTO preRead(int num, String condition, String keyword) {
        DTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	if(keyword.length() != 0) {
        		sql="SELECT num, subject, FROM terryBoard tb "
        				+ "JOIN member1 m1 ON tb.userId = m1.userId ";
                if(condition.equals("all")) {
                    sql+="WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ";
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sql+="WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ";
                } else {
                    sql+=" WHERE ( INSTR("+condition+", ?) > 0) ";
                }
                sql+="AND (num > ? ) "
                +"ORDER BY num ASC "
                +"FETCH  FIRST  1  ROWS  ONLY ";

                pstmt=conn.prepareStatement(sql);
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sql="SELECT num, subject FROM terryBoard "
                +"WHERE num > ? "
                +"ORDER BY num ASC "
                +"FETCH  FIRST  1  ROWS  ONLY ";

                pstmt=conn.prepareStatement(sql);
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new DTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}
    
        return dto;
    }
	
	public DTO nextRead(int num, String condition, String keyword) {
        DTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	if(keyword.length() != 0) {
        		sql="SELECT num, subject, FROM terryBoard tb "
        				+ "JOIN member1 m1 ON tb.userId = m1.userId ";
                if(condition.equals("all")) {
                    sql+="WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1  ) ";
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sql+="WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ";
                } else {
                    sql+=" WHERE ( INSTR("+condition+", ?) > 0) ";
                }
                sql+="AND (num < ? ) "
                +"ORDER BY num DESC "
                +"FETCH  FIRST  1  ROWS  ONLY ";

                pstmt=conn.prepareStatement(sql);
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                } else {
                    pstmt.setString(1, keyword);
                   	pstmt.setInt(2, num);
                }
            } else {
                sql="SELECT num, subject FROM terryBoard "
                +"WHERE num < ? "
                +"ORDER BY num DESC "
                +"FETCH  FIRST  1  ROWS  ONLY ";

                pstmt=conn.prepareStatement(sql);
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new DTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (SQLException e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (SQLException e2){
				}
			}
		}

        return dto;
    }
	
	public int update(DTO dto) throws SQLException{
    	int result = 0;
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
			sql="UPDATE terrayBoard SET subject = ?, content = ? WHERE num = ? AND userId = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			pstmt.setString(4, dto.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null);
			try {
				pstmt.close();
			} catch (Exception e2) {
			}
		}
    	
    	
    	return result;
    }
	
	public int delete(int num, String userId) throws SQLException{
    	int result = 0;
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
			sql = "DELETE FROM terryBoard WHERE num = ? AND userId=?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);				
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
    	
    	return result;
    }
}
