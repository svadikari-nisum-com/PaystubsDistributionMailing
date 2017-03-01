/**
 * 
 */
package com.shyam;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author svadikari
 *
 */
public class PaystubsDistributionMailing {

	private static final String TEXT_HTML = "text/html";
	private static final String TRUE = "true";
	private static final String MAIL_SMTP_PORT = "mail.smtp.port";
	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_FROM = "Shyam Vadikari<svadikari@nisum.com>";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File directory = new File("/Paystubs/"
				+ Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.ENGLISH) + " "
				+ Calendar.getInstance().get(Calendar.YEAR));
		if (directory.exists()) {
			File files[] = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						sendNotificationMail(file);
					}
				}
			} else {
				System.out.println("Couldn't find pay stubs");
			}
		} else {
			System.out.println("Couldn't find " + directory.getAbsolutePath());
		}

	}

	public static boolean sendNotificationMail(File file) {
		boolean isMailSent = false;
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(MAIL_FROM));
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(buildHtmlTextPart());
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(file.getPath())));
			messageBodyPart.setFileName(file.getName());
			mp.addBodyPart(messageBodyPart);
			message.setContent(mp);
			message.setSubject("NISUM CONSULTING PVT LTD - PAYSLIP FOR THE MONTH OF "
					+ Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.ENGLISH)
					+ " " + Calendar.getInstance().get(Calendar.YEAR));
			InternetAddress[] addressList = new InternetAddress[1];
			addressList[0] = new InternetAddress(
					file.getName().substring(0, file.getName().lastIndexOf(".")) + "@nisum.com");
			message.setRecipients(Message.RecipientType.TO, addressList);
			Transport.send(message);
			isMailSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isMailSent;
	}

	private static BodyPart buildHtmlTextPart() throws MessagingException {
		MimeBodyPart messageBody = new MimeBodyPart();
		StringBuilder sbBody = new StringBuilder();
		sbBody.append("Hello,<br/><br/>Please find attached here with salary slip for "
				+ Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.ENGLISH) + " "
				+ Calendar.getInstance().get(Calendar.YEAR));
		sbBody.append(".<br/><br/><div style=\"font-size:12px;\">")
				.append("<span style=\"font-weight: bold\">Thanks & Regards,</span><br>")
				.append("<span style=\"font-size:12px\">Humen Resource Team.</span><br>")
				.append("<div><p style=\"font-size:12.8px;margin:0px;font-family:&quot;trebuchet ms&quot;;color:rgb(132,133,132)\"></p><p style=\"font-size:12.8px;margin:0px;font-family:&quot;trebuchet ms&quot;;color:rgb(132,133,132)\"><br></p><p style=\"font-size:12.8px;margin:0px;color:rgb(132,133,132)\"><span style=\"font-family:&quot;trebuchet ms&quot;;color:rgb(0,192,233)\"><font size=\"4\">nisum</font></span><span style=\"font-size:14px;font-family:&quot;trebuchet ms&quot;;color:rgb(0,192,233)\"> </span></p><p style=\"font-size:12.8px;margin:0px;color:rgb(132,133,132)\"><font size=\"2\"><span style=\"font-family:&quot;trebuchet ms&quot;\">consulting</span><span style=\"font-family:&quot;trebuchet ms&quot;;color:rgb(192,192,192)\">&nbsp;</span><span style=\"color:rgb(216,225,53)\"><b><font face=\"arial black, sans-serif\">|</font></b></span><span style=\"font-family:&quot;trebuchet ms&quot;;color:rgb(192,192,192)\">&nbsp;</span><span style=\"font-family:&quot;trebuchet ms&quot;\">digital</span><span style=\"font-family:&quot;trebuchet ms&quot;;color:rgb(192,192,192)\">&nbsp;</span><span style=\"color:rgb(216,225,53)\"><b><font face=\"arial black, sans-serif\">|</font></b></span><span style=\"font-family:&quot;trebuchet ms&quot;;color:rgb(192,192,192)\">&nbsp;</span><span style=\"font-family:&quot;trebuchet ms&quot;\">innovat<wbr>ion</span></font></p><p style=\"font-size:12.8px;margin:0px;font-family:&quot;trebuchet ms&quot;;color:rgb(132,133,132)\"><a href=\"http://www.nisum.com/\" style=\"color:rgb(17,85,204)\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?hl=en&amp;q=http://www.nisum.com/&amp;source=gmail&amp;ust=1475155874076000&amp;usg=AFQjCNHrJ3W5_TKKLkTGOegqhvNYJm0Shg\"><font size=\"1\">www.nisum.com</font></a></p></div>")
				.append("</div>");
		messageBody.setContent(sbBody.toString(), TEXT_HTML);
		return messageBody;
	}

	private static Session getSession() throws Exception {
		Properties props = new Properties();
		props.put(MAIL_SMTP_AUTH, TRUE);
		props.put(MAIL_SMTP_STARTTLS_ENABLE, TRUE);
		props.put(MAIL_SMTP_HOST, "smtp.gmail.com");
		props.put(MAIL_SMTP_PORT, "587");

		Session session = null;
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("nisumportal@gmail.com", "Nisum@123");
			}
		});
		return session;
	}
}
