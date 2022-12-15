package auth;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class SendMail {
	// 메일 발송 작업을 수행하는 sendMail() 메서드 정의
	// => 파라미터 : 수신자 주소, 메일 제목, 메일 본문
	// => 리턴타입 : 메일 발송 성공 여부 - boolean 타입(isSendSuccess)
	public boolean sendMail(String receiver, String title, String content) {
		boolean isSendSuccess = false; // 메일 발송 성공 여부 저장 변수(true : 성공, false : 실패)
		
		String sender = "admin@itwillbs.co.kr"; // 발신자 주소(상용 메일 사이트에는 변경 불가능)

		try {
			// -------- 메일 전송에 필요한 설정 작업 ---------
			Properties properties = System.getProperties();
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			properties.put("mail.smtp.host", "smtp.gmail.com"); // 구글(Gmail) 메일 발송 서버 주소 지정
			properties.put("mail.smtp.auth", "true"); // 인증 여부 설정(로그인 필요 시)
			properties.put("mail.smtp.port", "587"); // 메일 전송에 사용될 서비스 포트 설정(SMTP 포트) => 기본 25번. 각 서버별로 달라질 수 있음
			// --------------------------------------------
			// 메일 서버에 대한 인증 정보를 관리하는 사용자 정의 클래스(GoogleSMTPAuthenticator)의 인스턴스 생성
			Authenticator authenticator = new GoogleSMTPAuthenticator(); // 슈퍼클래스(Authenticator) 타입으로 업캐스팅
			Session mailSession = Session.getDefaultInstance(properties, authenticator);
			Message mailMessage = new MimeMessage(mailSession);
			// ---------------------------------------------------------------------
			// 전송할 메일에 대한 발송 정보 설정
			Address sender_address = new InternetAddress(sender, "아이티윌"); // 두번째 파라미터(발신자 이름)는 변경 가능
			Address receiver_address = new InternetAddress(receiver);
			mailMessage.setHeader("content-type", "text/html; charset=UTF-8");
			mailMessage.setFrom(sender_address);
			mailMessage.addRecipient(RecipientType.TO, receiver_address);
			mailMessage.setSubject(title);
			mailMessage.setContent(content, "text/html; charset=UTF-8");
			mailMessage.setSentDate(new Date());

			// 메일 전송
			Transport.send(mailMessage);
			System.out.println("메일이 정상적으로 전송되었습니다!");
			
			isSendSuccess = true;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("SMTP 서버 설정 또는 서비스 문제 발생!");
		}
		
		return isSendSuccess;
	}
	
}









