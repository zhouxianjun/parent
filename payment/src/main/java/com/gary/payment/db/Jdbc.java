package com.gary.payment.db;

import java.sql.*;
import java.util.*;

public class Jdbc {
	private String url;
	private String driver;
	private Connection conn;

	private PreparedStatement ps;

	private ResultSet rs;
	public Jdbc(String url,String driver) {
		this.url = url;
		this.driver = driver;
	}
	private void getConnection() throws SQLException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int execuetUpdate(String sql, List<?> list) throws Exception {
		getConnection();
		int n = 0;
		ps = conn.prepareStatement(sql);
		for (int i = 0; list != null && i < list.size(); i++) {
			ps.setObject(i + 1, list.get(i));
		}
		n = ps.executeUpdate();
		return n;
	}
	public ResultSet execuetQuery(String sql, List<?> list) throws Exception {
		getConnection();
		ps = conn.prepareStatement(sql);
		for (int i = 0; list != null && i < list.size(); i++) {
			ps.setObject(i + 1, list.get(i));
		}
		rs = ps.executeQuery();
		return rs;

	}

	public void closedAll() throws SQLException {

		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (conn != null) {
			conn.close();
		}

	}
	
	public boolean hasTable(String name) {
        //判断某一个表是否存在
        boolean result = false;
        try {
            DatabaseMetaData meta = conn.getMetaData();//sqlConn 数据库连接
            ResultSet set = meta.getTables (null, null, name, null);
            while (set.next()) {
                result = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace ();
        }
        return result;
    } 
	
	public Connection getConn() {
		return conn;
	}
}
