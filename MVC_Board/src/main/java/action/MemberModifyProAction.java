package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberModifyProService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberModifyProAction");
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("sId");
		String passwd = request.getParameter("passwd");
		
		// MemberModifyProService 클래스의 isCheckPasswd() 메서드 호출하여 패스워드 일치 여부 확인
		// => 파라미터 : 아이디, 패스워드    리턴타입 : boolean(isCheckPasswd)
		MemberModifyProService service = new MemberModifyProService();
		boolean isCheckPasswd = service.isCheckPasswd(id, passwd);
		
		if(!isCheckPasswd) { // 수정 권한이 없는 경우
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 일치하지 않습니다!')");
			out.println("history.back()");
			out.println("</script>");
		} else { // 수정 권한이 있는 경우
			// BoardBean 객체 생성 후 폼 파라미터 저장
			MemberBean member = new MemberBean();
			member.setName(request.getParameter("name"));
			member.setGender(request.getParameter("gender"));
			member.setEmail(request.getParameter("email1") + "@" + request.getParameter("email2"));
			member.setId(request.getParameter("id"));
			member.setPasswd(request.getParameter("newPasswd"));
			System.out.println(member);
			
			// Service 클래스의 modifyMember() 메서드를 호출하여 게시물 수정 작업 요청
			// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isCheckPasswd)
			boolean isModifySuccess = service.modifyMember(member);
			
			// 수정 요청 결과 판별
			// => 실패 시 자바스크립트를 통해 "수정 실패!" 출력 후 이전페이지로 돌아가기
			//    아니면, ActionForward 객체를 사용하여 BoardDetail.bo 서블릿 주소 요청(Redirect)
			//    (서블릿 주소 요청 시 글번호와 페이지번호를 파라미터로 함께 전달) 
			if(!isModifySuccess) {
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('수정 실패!')");
				out.println("history.back()");
				out.println("</schript>");
			} else {
				session.setAttribute("sId", member.getId());
	            forward = new ActionForward();
	            forward.setPath("MemberInfo.me");
	            forward.setRedirect(true);
			}
		}
		return forward;
	}
}
