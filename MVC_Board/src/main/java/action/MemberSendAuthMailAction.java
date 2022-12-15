package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.GenerateUserAuthenticationCode;
import auth.SendMail;
import svc.MemberSendAuthMailService;
import vo.ActionForward;
import vo.AuthInfoBean;

public class MemberSendAuthMailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		String id = request.getParameter("id");
		String email = request.getParameter("email");
//		System.out.println(id + ", " + email);
		
		// GenerateUserAuthenticationCode 클래스 인스턴스 생성 및 Getter 호출하여 인증코드 리턴받기
		// => 파라미터로 생성할 코드의 길이 전달(ex. 50글자)
		GenerateUserAuthenticationCode genAuthCode = new GenerateUserAuthenticationCode(50);
		String authCode = genAuthCode.getAuthCode();
//		System.out.println("인증코드 : " + authCode);
		
		// 인증 메일 발송에 사용될 정보(수신자 메일 주소, 메일 제목, 메일 본문) 생성
		// => 수신자 메일 주소는 가입자의 email 주소를 그대로 사용
		String subject = "[아이티윌] 아이티윌 가입 인증 메일입니다.";
		String content = "<a href='http://localhost:8080/MVC_Board/MemberAuth.me?id=" + id + 
							"&authCode=" + authCode + "'><b>인증하려면 클릭하세요.</b></a>";
		
		// 인증 메일 발송을 위해 SendMail 클래스 인스턴스 생성 및 sendMail() 메서드 호출하여 메일 발송
		// => 파라미터 : 수신자 메일 주소, 메일 제목, 메일 본문
		SendMail sendMail = new SendMail();
		boolean isSendSuccess = sendMail.sendMail(email, subject, content);
		
		// AuthInfoBean 객체 생성 및 아이디, 인증코드 저장
		AuthInfoBean authInfo = new AuthInfoBean(id, authCode);
		
		// MemberSendAuthMailService 클래스의 인스턴스 생성 후
		// registAuthInfo() 메서드를 호출하여 인증 정보를 DB 에 등록 작업 요청
		// => 파라미터 : AuthInfoBean 객체    리턴타입 : boolean(isRegistSuccess)
		MemberSendAuthMailService service = new MemberSendAuthMailService();
		boolean isRegistSuccess = service.registAuthInfo(authInfo);
		
		// 만약, 인증 메일 발송 실패 또는 인증 정보 등록 실패일 경우
		// => 새로운 인증 정보 발송을 위해 MemberSendAuthMail.me 요청(Redirect)
		//    (파라미터 : 아이디, 이메일)
		// 아니면, 회원 가입 완료 페이지("MemberJoinResult.me") 서블릿 요청(Redirect)
		forward = new ActionForward();
		forward.setRedirect(true);
		if(!isSendSuccess || !isRegistSuccess) {
			forward.setPath("MemberSendAuthMail.me?id=" + id + "&email=" + email);
		} else {
			forward.setPath("MemberJoinResult.me");
		}
		
		return forward;
	}

}
















