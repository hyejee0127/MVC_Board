package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cipher.MyMessageDigest;
import svc.MemberLoginProService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberLoginProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberLoginProAction");
		
		ActionForward forward = null;
		
		// 전달받은 파라미터 가져와서 MemberBean 객체에 저장
		MemberBean member = new MemberBean();
		member.setId(request.getParameter("id"));
//		member.setPasswd(request.getParameter("passwd"));
		// ------------------------- 패스워드 단방향 암호화 기능 추가 ----------------------------
		// MyMessageDigest 객체의 hashing() 메서드를 호출하여 "SHA-256" 알고리즘으로 패스워드 해싱
		MyMessageDigest md = new MyMessageDigest("SHA-256");
		member.setPasswd(md.hashing(request.getParameter("passwd")));
		// ---------------------------------------------------------------------------------------
//		System.out.println(member);
		
		MemberLoginProService service = new MemberLoginProService();
		
		// MemberLoginProService - isAuthenticatedUser() 메서드 호출하여 인증 여부 확인 요청
		// => 파라미터 : MemberBean 객체     리턴타입 : boolean(isAuthenticatedUser)
		boolean isAuthenticatedUser = service.isAuthenticatedUser(member);
		
		// 인증 여부 확인 결과가 false 일 경우
		// 자바스크립트를 사용하여 "메일 인증 필수!" 출력 후 이전페이지로 돌아가기
		// 아니면, 로그인 작업 수행
		if(!isAuthenticatedUser) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('메일 인증 필수!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			// MemberLoginProService - memberLogin() 메서드 호출하여 로그인 작업 요청
			// => 파라미터 : MemberBean 객체     리턴타입 : boolean(isLoginSuccess)
			boolean isLoginSuccess = service.memberLogin(member);
			
			// 결과 판별
			// 실패 시 자바스크립트를 통해 "로그인 실패!" 출력 후 이전페이지로 돌아가기
			// 아니면, 세션 객체에 로그인 아이디를 "sId" 라는 속성명으로 저장 후 메인페이지로 포워딩
			// => 단, 자바 클래스 내에서 세션 객체에 접근하기 위해서는
			//    request 객체의 getSession() 메서드를 호출하여 HttpSession 타입 객체 리턴받아 사용
			if(!isLoginSuccess) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('로그인 실패!')");
				out.println("history.back()");
				out.println("</script>");
			} else {
				// request 객체의 getSession() 메서드를 호출하여 HttpSession 타입 객체 리턴
				HttpSession session = request.getSession();
				session.setAttribute("sId", member.getId());
				
				forward = new ActionForward();
				forward.setPath("./");
				forward.setRedirect(true);
			}
		}
		
		return forward;
	}

}













