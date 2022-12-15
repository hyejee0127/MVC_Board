package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberLoginProService {

	// 로그인 작업 요청
	// => 파라미터 : MemberBean 객체     리턴타입 : boolean(isLoginSuccess)
	public boolean memberLogin(MemberBean member) {
		boolean isLoginSuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 클래스의 memberLogin() 메서드 호출하여 회원 가입 작업 수행
        // 리턴되는 boolean 타입 결과 저장
		// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isLoginSuccess)
		isLoginSuccess = dao.memberLogin(member);
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isLoginSuccess;
	}

	// 인증 여부 확인 요청
	// => 파라미터 : MemberBean 객체     리턴타입 : boolean(isAuthenticatedUser)
	public boolean isAuthenticatedUser(MemberBean member) {
		boolean isAuthenticatedUser = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 클래스의 isAuthenticatedUser() 메서드 호출하여 인증 여부 확인 작업 수행
		// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isAuthenticatedUser)
		isAuthenticatedUser = dao.isAuthenticatedUser(member);
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isAuthenticatedUser;
	}

}













