package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.AuthInfoBean;

public class MemberAuthService {
	// 인증 정보 확인 요청
	// => 파라미터 : AuthInfoBean 객체    리턴타입 : boolean(isAuthenticationSuccess)
	public boolean checkAuthInfo(AuthInfoBean authInfo) {
		boolean isAuthenticationSuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 클래스의 selectAuthInfo() 메서드 호출하여 인증 정보 조회 작업 수행
		// => 파라미터 : AuthInfoBean 객체    리턴타입 : boolean(isAuthenticationSuccess)
		isAuthenticationSuccess = dao.selectAuthInfo(authInfo);
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isAuthenticationSuccess;
	}

	// 회원 인증 정보 갱신
	public boolean updateAuthInfo(AuthInfoBean authInfo) {
		boolean isUpdateSuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 클래스의 updateAuthStatus() 메서드를 호출하여 
		// member 테이블에서 아이디가 일치하는 레코드의 인증여부(auth_status)를 "Y" 로 변경
		// => 파라미터 : AuthInfoBean 객체    리턴타입 : int(updateCount)
		int updateCount = dao.updateAuthStatus(authInfo);
		
		// MemberDAO 클래스의 deleteAuthInfo() 메서드 호출하여 
		// auth_info 테이블에서 아이디가 일치하는 레코드의 인증 정보를 삭제 
		// => 파라미터 : AuthInfoBean 객체    리턴타입 : int(deleteCount)
		int deleteCount = dao.deleteAuthInfo(authInfo);
		
		// 작업 처리 결과에 따른 트랜잭션 처리  
		// => updateCount 와 deleteCount 모두 0보다 클 경우 성공, 아니면 실패
		if(updateCount > 0 && deleteCount > 0) { // 작업 성공했을 경우
			JdbcUtil.commit(con);
			isUpdateSuccess = true;
		} else { // 작업 실패했을 경우
			JdbcUtil.rollback(con);
		}
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isUpdateSuccess;
	}

}













