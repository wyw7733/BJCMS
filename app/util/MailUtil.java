package util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import models.MailSenderInfo;

/**
 *
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:ITProject<br>
 * Class Type:类<br>
 * Comments:发送邮件UTIL<br>
 * JDK version:1.7<br>
 * Namespace:com.beijia.project.util<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 tianqi 2016-03-21 新项目 <br>
 * --------------------------------------------------------------<br>
 */

public class MailUtil {


    /**
     * 以HTML格式发送邮件
     *
     * @param
     */
    public boolean sendHtmlMail(MailSenderInfo mailInfo) throws Exception {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);

        // 根据session创建一个邮件消息
        Message mailMessage = new MimeMessage(sendMailSession);
        // 创建邮件发送者地址
        Address from = new InternetAddress(mailInfo.getFromAddress());
        // 设置邮件消息的发送者
        mailMessage.setFrom(from);
        // 创建邮件的接收者地址，并设置到邮件消息中
        Address to = new InternetAddress(mailInfo.getToAddress());
        // Message.RecipientType.TO属性表示接收者的类型为TO
        mailMessage.setRecipient(Message.RecipientType.TO, to);
        // 设置邮件消息的主题
        mailMessage.setSubject(mailInfo.getSubject());
        // 设置邮件消息发送的时间
        mailMessage.setSentDate(new Date());
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        // 将MiniMultipart对象设置为邮件内容
        mailMessage.setContent(mainPart);
        // 发送邮件
        Transport.send(mailMessage);
        return true;
    }

	/*public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.tom.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("youngcool87@tom.com");
		mailInfo.setPassword("welcome12a");// 您的邮箱密码
		mailInfo.setFromAddress("youngcool87@tom.com");
		mailInfo.setToAddress("youngcool87@tom.com");
		mailInfo.setSubject("设置邮箱标题");
		mailInfo.setContent("设置邮箱内容");
		// 这个类主要来发送邮件

		MailUtil mailsender = new MailUtil();
		try {
			mailsender.sendHtmlMail(mailInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

}
