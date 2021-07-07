package com.test.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.ConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionUtil  {

	Logger log = LoggerFactory.getLogger(DBConnectionUtil.class);
	
	private static DBConnectionUtil instance = null;
	private HashMap<String, Connection> conn = new HashMap<>();	
	
	/**
	 * 싱글톤 인스턴스 받아오기
	 * 
	 * @return
	 */
	public static DBConnectionUtil getInstance(){
		
		if(instance == null){
			synchronized (DBConnectionUtil.class) {
				instance = new DBConnectionUtil();
			}
		}
		
		return instance;
	}
	
	/**
	 * 커넥션 연결 가져오기
	 * 
	 * @param driverNm
	 * @param url
	 * @param user
	 * @param pwd
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Connection getConnection(String driverNm, String url, String user, String pwd) throws SQLException, ClassNotFoundException{
		
		// 현재 커넥션이 있는지
		if(conn.get(user) == null){
			Class.forName(driverNm);
			Connection connTemp = DriverManager.getConnection(url, user, pwd); 
			
			conn.put(user, connTemp);
		}
		
		return conn.get(user);
	}
	
	/**
	 * 단일 조회 쿼리
	 * 
	 * @param user
	 * @param query
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, Object> getSelectQuery(String user, String query, List<Object> paramList) throws SQLException{
		
		if(conn.get(user) == null) new ConnectionClosedException();
		
		HashMap<String, Object> result = new HashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.get(user).prepareStatement(query);
			
			// 파리미터 세팅
			for(Object param : paramList){
				pstmt.setObject(paramList.indexOf(param) + 1, param);
			}
			
			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			
			while(rs.next()){
				for(int i = 1; i <= md.getColumnCount(); i++){
					result.put(md.getColumnName(i), rs.getObject(i));
				}
			}
			
		} finally {
			// TODO: handle finally clause
			if (rs != null){
				if(!rs.isClosed()){
					rs.close();
				}
			}
			if (pstmt != null){
				if(!pstmt.isClosed()){
					pstmt.close();
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * 리스트 조회 쿼리
	 * 
	 * @param user
	 * @param query
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, Object>> getSelectListQuery(String user, String query, List<Object> paramList) throws SQLException{
	
		if(conn.get(user) == null) new ConnectionClosedException();
		
		List<HashMap<String, Object>> result = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.get(user).prepareStatement(query);
			
			// 파리미터 세팅
			for(Object param : paramList){
				pstmt.setObject(paramList.indexOf(param) + 1, param);
			}
			
			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			
			while(rs.next()) {
				HashMap<String,Object> row = new HashMap<String, Object>();
				for(int i = 1; i <= md.getColumnCount(); i++) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				
				result.add(row);
			}
			
		} finally {
			// TODO: handle finally clause
			if (rs != null){
				if(!rs.isClosed()){
					rs.close();
				}
			}
			if (pstmt != null){
				if(!pstmt.isClosed()){
					pstmt.close();
				}
			}
		}
	
		return result;
	}
	
	/**
	 * 커넥션 종료
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void connectionClose(String user) throws SQLException{
		
		// 해당 커넥션이 있는지
		if(conn.get(user) != null){
			// 커넥션이 닫혀있는지
			if(!conn.get(user).isClosed()){
				conn.get(user).close();
			}
			// 커넥션 관리도 지움
			conn.remove(user);
		}
	}
	
	/**
	 * 커넥션 전부 종료
	 * 
	 * @throws SQLException
	 */
	public void connectionCloseAll() throws SQLException{
		 
		for(String key : conn.keySet()){
			if(!conn.get(key).isClosed()){
				conn.get(key).close();
			}
		}
		
		conn.clear();
	}
	
	/**
	 * 현재 열려있는 커넥션
	 * 
	 * @return
	 */
	public int getConnectionCount(){
		return conn.size();
	}
	
}
