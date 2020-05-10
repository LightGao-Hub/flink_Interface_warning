package com.shenque.warning.util

/**
  * @author gl 
  */
class EmailThread(exceptionInfo:String, info:String) extends Runnable with Serializable {
  override def run(): Unit = {
    val sendEmailUtil = new SendEmailUtil(exceptionInfo, info, null)
    sendEmailUtil.send()
  }
}
