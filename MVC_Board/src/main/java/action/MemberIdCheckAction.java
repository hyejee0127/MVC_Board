package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import svc.MemberInfoService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberIdCheckAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id=request.getParameter("id");
		
		MemberInfoService service = new MemberInfoService();
		MemberBean member = service.getMember(id);
		String result="";
		if(member!=null) {
			//아이디 있음, 아이디 중복
			result="아이디 중복";
		}else {
			//아이디 없음, 아이디 사용가능
			result="아이디 사용가능";
		}
		// result 출력
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();
		
		return null;
	}

}
