package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberModifyProService {

	public boolean isCheckPasswd(String id, String passwd) {
		System.out.println("MemberModifyProService - isCheckPasswd");
		boolean isCheckPasswd = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 클래스의 isBoardWriter() 메서드 호출하여 패스워드 일치 여부 확인 작업 수행 후
        // 리턴되는 boolean 타입 결과 저장
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		isCheckPasswd = dao.isCheckPasswd(id, passwd);
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isCheckPasswd;		
	}

	public boolean modifyMember(MemberBean member) {
		System.out.println("MemberModifyProService - modifyMember");
		
		boolean isModifySuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 클래스의 updateBoard() 메서드 호출하여 게시물 수정 작업 수행
		// => 파라미터 : BoardBean 객체    리턴타입 : int(updateCount)
		int updateCount = dao.updateMember(member);
		
		// 트랜잭션 처리
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			// isModifySuccess 를 true 로 변경
			isModifySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return isModifySuccess;
	}
}
