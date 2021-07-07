/*
 * package com.test.project.service.common;
 * 
 * import org.apache.log4j.jdbc.JDBCAppender;
 * 
 * public class CustomJDBCAppender extends JDBCAppender {
 * 
 * @Override protected String getLogStatement(LoggingEvent event) {
 * 
 * Object eventMsgObj = event.getMessage(); String eventMessage = "";
 * 
 * if (eventMsgObj != null && eventMsgObj.toString() != null) { // DB에 입력하기 위해서는
 * (') 를 ('')로 치환해야지 에러없이 제대로 입력된다. eventMessage =
 * eventMsgObj.toString().replace("'", "''").replaceAll("(?<!\r)\n", "\r\n"); }
 * if (null != event.getThrowableInformation()) { // DB에 저장할 때는 "\r\n" 이 아닌
 * "\n"만 "\r\n"으로 바꾼다. // 부정형 후방탐색 정규표현식 이용 => replaceAll("(?<!\r)\n", "\r\n")
 * 
 * Throwable throwable = event.getThrowableInformation().getThrowable(); String
 * message = ""; StackTraceElement[] stackTrace = null;
 * 
 * if (throwable != null) { message = throwable.getMessage(); stackTrace =
 * throwable.getStackTrace(); if (message != null) { message =
 * message.replace("'", "''").replaceAll("(?<!\r)\n", "\r\n"); } }
 * 
 * Exception exception = new Exception(message, throwable);
 * exception.setStackTrace(stackTrace); LoggingEvent clone = new
 * LoggingEvent(event.fqnOfCategoryClass,
 * LogManager.getLogger(event.getLoggerName()), event.getLevel(), eventMessage,
 * exception);
 * 
 * if (eventMessage != null) { return getLayout().format(clone); } else { return
 * "SELECT 1 FROM DUAL"; }
 * 
 * } else { return "SELECT 1 FROM DUAL"; } }
 * 
 * }
 */