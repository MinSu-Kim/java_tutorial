package java_tutorial.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * SMTP와 Mail 발송하기(Google, Naver)
 * 출처: https://ktko.tistory.com/entry/JAVA-SMTP와-Mail-발송하기Google-Naver [KTKO 개발 블로그와 여행 일기]
 * @author mskim 참조하여 완성하기
 *
 */
public class SendEmail {
	public static void main(String[] args) {
		// Recipient's email ID needs to be mentioned.
		String to = "net94@nate.com";

		// Sender's email ID needs to be mentioned
		String from = "net94.teacher@gmail.com";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
//		properties.setProperty("mail.user", "net94.teacher73@gmail.com");
//		properties.setProperty("mail.password", "kim71040533!");
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Now set the actual message
			message.setText("This is actual message");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
