package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardListJsonAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BoardListService service = new BoardListService();
		List<BoardBean> boardList = service.getBoardList(1, 10);
		
		//for => json
		
		// json 출력
		
		return null;
	}

}
