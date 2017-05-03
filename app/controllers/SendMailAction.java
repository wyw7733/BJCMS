package controllers;

import models.MailSenderInfo;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.MailUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


/**
 * Created by zuopanpan on 2016/4/29.
 */
public class SendMailAction extends Controller{
    /**
     * 发送邮件
     * @param
     * @return
     */
    public static Result sendMail() {
       DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String mailAddress = form.data().get("mailAddress");
        String code = form.data().get("code");
        // 这个类主要是设置邮件
        /* MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.tom.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("youngcool87@tom.com");
        mailInfo.setPassword("welcome12a");// 您的邮箱密码
        mailInfo.setFromAddress("youngcool87@tom.com");
        mailInfo.setToAddress(mailAddress);
        mailInfo.setSubject("西安市旅游局账户-注册验证码");
        mailInfo.setContent("验证码:"+code);
        // 这个类主要来发送邮件

        MailUtil mailsender = new MailUtil();
        try {
            mailsender.sendHtmlMail(mailInfo);
            return ok(callbackparam + "({message:'true'})");
        } catch (Exception e) {
            e.printStackTrace();
            return ok(callbackparam + "({message:'false'})");
        }*/
        boolean tag = sendMail1("youngcool87@tom.com","welcome12a",mailAddress,"西安市旅游局账户-注册验证码","验证码:"+code);
        if(tag){
            return ok(callbackparam + "({message:'true'})");
        }else{
            return ok(callbackparam + "({message:'false'})");
        }

    }
    /***
     * 发送邮件
     * @param userName
     * @param password
     * @param title
     * @param content
     */
    private static boolean sendMail1(final String userName, final String password, String receiveMail, String title, String content){
        boolean tag=false;
        Properties ps = new Properties();
        ps.setProperty("mail.smtp.host", "smtp." + receiveMail.split("@")[1]);
        ps.setProperty("mail.smtp.auth", "true");
        Session s = Session.getInstance(ps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        Message m = new MimeMessage(s);
        try {
            m.setFrom(new InternetAddress(userName, password));
            m.setSubject(title);
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail));
            m.setText(content);
            Transport t = s.getTransport("smtp");
            t.send(m);
            t.close();
            tag=true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }finally {
            return tag;
        }
    }

}


