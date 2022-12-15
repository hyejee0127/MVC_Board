<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://1.252.62.53:3306/team1";
	String user = "team1";
	String password = "1234";
	
	Class.forName(driver);
	Connection con = DriverManager.getConnection(url, user, password);
	String sql = "CREATE TABLE test (idx INT)";
	PreparedStatement pstmt = con.prepareStatement(sql);
	pstmt.executeUpdate();
			
	pstmt.close();
	con.close();
	%>
</body>
</html>












