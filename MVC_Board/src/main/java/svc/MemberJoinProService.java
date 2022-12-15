package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberJoinProService {
	
	public boolean registMember(MemberBean member) {
		boolean isRegistSuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 클래스의 insertMember() 메서드 호출하여 회원 가입 작업 수행
        // 리턴되는 int 타입 결과 저장
		// => 파라미터 : MemberBean 객체    리턴타입 : int(insertCount)
		int insertCount = dao.insertMember(member);
		
		// 작업 처리 결과에 따른 트랜잭션 처리  
		if(insertCount > 0) { // 작업 성공했을 경우
			JdbcUtil.commit(con);
			isRegistSuccess = true;
		} else { // 작업 실패했을 경우
			JdbcUtil.rollback(con);
		}
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isRegistSuccess;
	}
}












