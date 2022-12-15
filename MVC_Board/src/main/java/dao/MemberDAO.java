package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JdbcUtil;
import vo.AuthInfoBean;
import vo.MemberBean;

public class MemberDAO {
	
	private static MemberDAO instance = new MemberDAO();
	
	private MemberDAO() {}

	public static MemberDAO getInstance() {
		return instance;
	}
	// -------------------------------------------------------
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	// -------------------------------------------------------

	// 회원 가입
	// => 파라미터 : MemberBean 객체    리턴타입 : int(insertCount)
	public int insertMember(MemberBean member) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			// member 테이블에 회원정보 INSERT
			// => 단, 회원번호(idx)는 null 값 전달 시 AUTO_INCREMENT 에 의해 번호 자동 증가
			String sql = "INSERT INTO member VALUES (null,?,?,?,?,?,now(),?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, "N"); // 인증 상태 기본값 "N"
			
			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - insertMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return insertCount;
	}

	// 회원 가입
	// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isLoginSuccess)
	public boolean memberLogin(MemberBean member) {
		boolean isLoginSuccess = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// SELECT 구문을 사용하여 id 와 passwd 컬럼이 일치하는 레코드 검색
			String sql = "SELECT * FROM member WHERE id=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			rs = pstmt.executeQuery();
			
			// 레코드가 존재할 경우 isLoginSuccess 를 true 로 변경
			if(rs.next()) {
				isLoginSuccess = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - memberLogin()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isLoginSuccess;
	}

	// 회원정보 조회
	// => 파라미터 : 아이디     리턴타입 : MemberBean(member)
	public MemberBean selectMember(String id) {
		MemberBean member = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM member WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// MemberBean 객체 생성 및 데이터 저장
				member = new MemberBean();
				member.setName(rs.getString("name"));
				member.setId(rs.getString("id"));
				member.setEmail(rs.getString("email"));
				member.setGender(rs.getString("gender"));
				member.setDate(rs.getDate("date"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - selectMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return member;
	}

	// 인증 정보 등록
	public int insertAuthInfo(AuthInfoBean authInfo) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		try {
			// 아이디에 해당하는 레코드 검색(SELECT)
			// => 아이디 존재할 경우 아이디에 해당하는 기존 인증 코드 UPDATE 수행
			// => 아이디 존재하지 않을 경우 아이디와 새 인증 코드 INSERT 수행
			// => 이 때, 둘 중 하나라도 실행 시 결과값을 insertCount 저장
			String sql = "SELECT * FROM auth_info WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, authInfo.getId());
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 아이디 존재할 경우
				sql = "UPDATE auth_info SET auth_code=? WHERE id=?";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, authInfo.getAuth_code());
				pstmt2.setString(2, authInfo.getId());
			} else { // 아이디 존재하지 않을 경우
				sql = "INSERT INTO auth_info VALUES (?,?)";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, authInfo.getId());
				pstmt2.setString(2, authInfo.getAuth_code());
			}
			
			insertCount = pstmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - insertAuthInfo()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(pstmt2);
		}
		
		return insertCount;
	}

	// 인증 여부 확인
	public boolean isAuthenticatedUser(MemberBean member) {
		boolean isAuthenticatedUser = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 아이디에 해당하는 인증 여부가 "Y" 인지 판별
			String sql = "SELECT * FROM member WHERE id=? AND auth_status=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, "Y");
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 조회 결과가 존재할 경우 = 인증 회원
				isAuthenticatedUser = true;
			} 
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - isAuthenticatedUser()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isAuthenticatedUser;
	}

	// 인증 정보 확인
	public boolean selectAuthInfo(AuthInfoBean authInfo) {
		System.out.println(authInfo.getId() + ", " + authInfo.getAuth_code());
		boolean isAuthenticationSuccess = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 전달받은 AuthInfoBean 객체의 인증정보(아이디, 인증코드)가 일치하는
			// auth_info 테이블의 인증 정보를 조회
			// => 일치하는 레코드가 존재할 경우 = 인증코드가 일치할 경우
			//    isAuthenticationSuccess 를 true 로 변경
			String sql = "SELECT * FROM auth_info WHERE id=? AND auth_code=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, authInfo.getId());
			pstmt.setString(2, authInfo.getAuth_code());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				isAuthenticationSuccess = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - selectAuthInfo()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isAuthenticationSuccess;
	}

	// 인증 상태 변경
	public int updateAuthStatus(AuthInfoBean authInfo) {
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE member SET auth_status='Y' WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, authInfo.getId());
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - updateAuthStatus()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}

	// 인증 정보 삭제
	public int deleteAuthInfo(AuthInfoBean authInfo) {
		int deleteCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM auth_info WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, authInfo.getId());
			deleteCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - deleteAuthInfo()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return deleteCount;
	}
	
	
	
}












