package com.shenque.warning.util;

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Date;

public class SendEmailUtil implements Serializable {

    private String account;    //登录用户名
    private String pass;        //登录密码
    private String host;        //服务器地址（邮件服务器）
    private String port;        //端口
    private String protocol; //协议
    private String to;    //收件人

    private Properties prop = new JavaReadProperties().read();

    static class MyAuthenricator extends Authenticator{
        String u = null;
        String p = null;
        public MyAuthenricator(String u,String p){
            this.u=u;
            this.p=p;
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u,p);
        }
    }


    private String subject;    //主题
    private String content;    //内容
    private String fileStr;    //附件路径

    //如果不需要附件， fileStr就传送null
    public SendEmailUtil(String subject, String content, String fileStr) {
        this.subject = subject;
        this.content = content;
        this.fileStr = fileStr;

        this.account = prop.getProperty("e.account");
        this.pass = prop.getProperty("e.pass");
        this.host = prop.getProperty("e.host");
        this.port = prop.getProperty("e.port");
        this.protocol = prop.getProperty("e.protocol");
        this.to = prop.getProperty("e.to");

    }



    public void send() {
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", protocol);
        //服务器
        prop.setProperty("mail.smtp.host", host);
        //端口
        prop.setProperty("mail.smtp.port", port);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            for(String strTo: to.split(",")) {
                //发件人
                mimeMessage.setFrom(new InternetAddress(account,"gaoliang"));        //可以设置发件人的别名
                //mimeMessage.setFrom(new InternetAddress(account));    //如果不需要就省略
                //收件人
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
                //主题
                mimeMessage.setSubject(subject);
                //时间
                mimeMessage.setSentDate(new Date());
                //容器类，可以包含多个MimeBodyPart对象
                Multipart mp = new MimeMultipart();

                //MimeBodyPart可以包装文本，图片，附件
                MimeBodyPart body = new MimeBodyPart();
                //HTML正文
                body.setContent(content, "text/html; charset=UTF-8");
                mp.addBodyPart(body);

                //添加图片&附件 这里加了个判断，如果不需要图片附件就传送null
                if(fileStr != null){
                    body = new MimeBodyPart();
                    body.attachFile(fileStr);
                    mp.addBodyPart(body);
                }

                //设置邮件内容
                mimeMessage.setContent(mp);
                //仅仅发送文本
                //mimeMessage.setText(content);
                mimeMessage.saveChanges();
                Transport.send(mimeMessage);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
