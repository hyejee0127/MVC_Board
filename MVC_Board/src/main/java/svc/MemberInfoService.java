package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberInfoService {

	// 회원 정보 조회 요청
	public MemberBean getMember(String id) {
		MemberBean member = null;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO - selectMember() 로 회원정보 조회
		// => 파라미터 : 아이디     리턴타입 : MemberBean(member)
		member = dao.selectMember(id);
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return member;
	}

}
