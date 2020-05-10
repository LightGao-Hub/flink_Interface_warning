package com.shenque.warning.util;

public class TestEmail {

    public static void main(String[] args) {
        new TestEmail().sendEmail("测试Email", "Exception");
    }

    public void sendEmail(String exceptionInfo, String info) {
        SendEmailUtil sendEmailUtil = new SendEmailUtil(exceptionInfo, info, null);
        sendEmailUtil.send();
    }

}
