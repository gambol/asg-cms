/*
 * Copyright(c) 2007-2009 by Yingzhi Tech
 * All Rights Reserved
 */
package com.bieshao.web.common;




import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.catalina.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fxcn
 */
public class EmailSenderDriver {

    private static Log log = LogFactory.getLog(EmailSenderDriver.class);
    private EmailMimeMessage msg = null;
    private Session session = null;
    private Properties props = null;
    private Multipart mp = null;
    /** Static variable representing a high priority message. */
    public final static String HIGH_PRIORITY = "1";
    /** Static variable representing a normal priority message. */
    public final static String NORMAL_PRIORITY = "3";
    /** Static variable representing a low priority message. */
    public final static String LOW_PRIORITY = "5";
    /** The SMTP host used to send the message. */
    private String smtpHost = new String("");
    /** If using SMTP authentication, then this is the username to login to the SMTP server as. */
    private String smtpUsername = null;
    /** If using SMTP authentication, then this is the password for the username to login to the SMTP server as. */
    private String smtpPassword = null;
    /** The port the SMTP server is listening on. */
    private int smtpPort = 25;
    private long timeout;
    /** If we are to use SMTP authentication when sending the message. */
    private boolean usingAuthentication = false;
    private boolean usingSSL = false;

    public EmailSenderDriver() {
        log.debug("init Sender");

        props = System.getProperties();
        //default timeout 30 secs
        timeout = 30000;
        setTimeout(timeout);

        session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        msg = new EmailMimeMessage(getSession());
        mp = new MimeMultipart();
    }

    /**
     * 设置SMPT地址
     * @param smtpHost
     * @throws javax.mail.MessagingException
     */
    private void setSMTPHost(String smtpHost) throws MessagingException {
        this.setSMTPHost(smtpHost, null, null);
    }

    /**
     * 设置SMPT地址、用户名、密码
     * @param smtpHost
     * @param smtpUsername
     * @param smtpPasswd
     * @throws javax.mail.MessagingException
     */
    public void setSMTPHost(String smtpHost, String smtpUsername,
            String smtpPasswd) throws MessagingException {
        log.debug("MailSender in setSMTPHost Method. Host: " + smtpHost + " Username: " + smtpUsername);
        this.smtpHost = smtpHost;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPasswd;

        props.put("mail.smtp.host", this.smtpHost);

        if ((this.smtpUsername != null && this.smtpUsername.length() > 0) || (smtpPassword != null && smtpPassword.length() > 0)) {
            props.put("mail.smtp.auth", "true");
            usingAuthentication = true;
        } else {
            usingAuthentication = false;
        }

        log.debug("EmailSender in setSMTPHost Method. UsingAuthenticaton: " + usingAuthentication);
    }

    /**
     * 设置SMPT端口
     * @param smtpPort
     * @throws java.lang.Exception
     */
    public void setSMTPPort(int smtpPort) throws Exception {
        this.smtpPort = smtpPort;
        props.put("mail.smtp.port", String.valueOf(smtpPort));
    }

    /**
     * 设置目标邮箱
     * @param toMailAddr
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setTo(String toMailAddr) throws MessagingException,
            UnsupportedEncodingException {
        this.setTo(toMailAddr, "");
    }

    /**
     * 设置目标邮箱和目标接收人名称
     * @param toMailAddr
     * @param toName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    public void setTo(String toMailAddr, String toName) throws
            MessagingException, UnsupportedEncodingException {
        this.processRecipientList(Message.RecipientType.TO, toMailAddr, toName);
    }

    /**
     * 设置抄送邮箱
     * @param ccMailAddr
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setCC(String ccMailAddr) throws MessagingException,
            UnsupportedEncodingException {
        this.setCC(ccMailAddr, "");
    }

    /**
     * 设置抄送邮箱和抄送者名称
     * @param ccMailAddr
     * @param ccName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setCC(String ccMailAddr, String ccName) throws
            MessagingException, UnsupportedEncodingException {
        this.processRecipientList(Message.RecipientType.CC, ccMailAddr, ccName);
    }

    /**
     * 设置暗送邮箱
     * @param bccMailAddr
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setBCC(String bccMailAddr) throws MessagingException,
            UnsupportedEncodingException {
        this.setBCC(bccMailAddr, "");
    }

    /**
     * 设置暗送邮箱和暗送者名称
     * @param bccMailAddr
     * @param bccName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setBCC(String bccMailAddr, String bccName) throws
            MessagingException, UnsupportedEncodingException {
        this.processRecipientList(Message.RecipientType.BCC, bccMailAddr, bccName);
    }

    /**
     * 处理接收者列表
     * @param recipientType
     * @param toMailAddr
     * @param toName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void processRecipientList(Message.RecipientType recipientType,
            String toMailAddr, String toName) throws MessagingException,
            UnsupportedEncodingException {
        //
        //If the to message address has a comma in it, then it must be a comma separated list of email recipients
        //
        StringTokenizer st = new StringTokenizer(toMailAddr, ",");
        int tokenCount = st.countTokens();
        InternetAddress[] recipientList = new InternetAddress[tokenCount];

        //
        //Tokenize the recipient list, and create the Internet Address Array of Recipients
        //
        for (int i = 0; st.hasMoreTokens(); i++) {
            //Get the next token
            String msgTo = st.nextToken();

            //Ensure the token received is a valid address
            if (msgTo != null && msgTo.trim().length() > 0) {
                //If we only have one email address then we can display the to name
                if (tokenCount == 1 && toName != null && toName.length() > 0) {
                    recipientList[i] = new InternetAddress(msgTo, MimeUtility.encodeText(toName,"GBK","B"));
//                    recipientList[i] = new InternetAddress(msgTo, "=?GBK?B?"+ Base64.encodeToString(toName.getBytes(), true)+"?=");
                } //Otherwise just display the email address as the to name.
                else {
                    recipientList[i] = new InternetAddress(msgTo);
                }
            }
        }

        msg.setRecipients(recipientType, recipientList);
    }

    /**
     * 设置发送者邮箱
     * @param fromMailAddr
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setFrom(String fromMailAddr) throws MessagingException,
            UnsupportedEncodingException {
        this.setFrom(fromMailAddr, "");
    }

    /**
     * 设置发送者邮箱和发送者名称
     * @param fromMailAddr
     * @param fromName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    public void setFrom(String fromMailAddr, String fromName) throws
            MessagingException, UnsupportedEncodingException {
        String msgFrom = fromMailAddr;

        if (fromName != null && fromName.length() > 0) {
            msg.setFrom(new InternetAddress(msgFrom, MimeUtility.encodeText(fromName,"GBK","B")));
//            msg.setFrom(new InternetAddress(msgFrom, "=?GBK?B?"+ Base64.encodeToString(fromName.getBytes(), true)+"?="));
        } else {
            msg.setFrom(new InternetAddress(msgFrom));
        }
    }

    /**
     * 设置回复邮箱
     * @param replyToMailAddr
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setReplyTo(String replyToMailAddr) throws MessagingException,
            UnsupportedEncodingException {
        this.setReplyTo(replyToMailAddr, "");
    }

    /**
     * 设置回复邮箱和回复者名称
     * @param replyToMailAddr
     * @param replyToName
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void setReplyTo(String replyToMailAddr, String replyToName) throws
            MessagingException, UnsupportedEncodingException {
        String msgReplyTo = replyToMailAddr;
        String msgReplyToName = replyToName;

        if (msgReplyToName != null && msgReplyToName.length() > 0) {

            InternetAddress[] address = {new InternetAddress(msgReplyTo, MimeUtility.encodeText(replyToName,"GBK","B"))};
//            InternetAddress[] address = {new InternetAddress(msgReplyTo, "=?GBK?B?"+ Base64.encodeToString(replyToName.getBytes(), true)+"?=")};
            msg.setReplyTo(address);
        } else {
            InternetAddress[] address = {new InternetAddress(msgReplyTo)};
            msg.setReplyTo(address);
        }
    }

    /**
     * 设置邮箱标题
     * @param subject
     * @throws javax.mail.MessagingException
     */
    public void setSubject(String subject) throws MessagingException {
//        msg.setSubject("=?GBK?B?"+ Base64.encodeToString(subject.getBytes(), true)+"?=");
        msg.setSubject(subject);
    }

    /**
     * 设置纯文本内容
     * @param body
     * @throws javax.mail.MessagingException
     */
    public void setBody(String body) throws MessagingException {
        MimeBodyPart mbp = new MimeBodyPart();
//        mbp.setText(body, "GBK");
        mbp.setContent(body, "text/html;CHARSET=GBK");
//        mbp.setText(body, "text/html;CHARSET=GBK");
        mp.addBodyPart(mbp);
    }

    /**
     * 设置邮箱头属性
     * @param name
     * @param value
     * @throws javax.mail.MessagingException
     */
    private void setHeader(String name, String value) throws MessagingException {
        msg.setHeader(name, value);
    }

    /**
     * 设置优先级
     * 1 ~ 2 高优先级
     * 3 中优先级
     * 4 ~ 5 低优先级
     *
     * @param priorityValue
     * @throws java.lang.Exception
     */
    private void setPriority(String priorityValue) throws Exception {
        //if (priorityValue < 1 || priorityValue > 5) {
        //    throw new Exception ("An invalid priority value of " + priorityValue + " has been specified for the email.");
        //}
        if (priorityValue != null && priorityValue.trim().length() > 0) {
            msg.setHeader("X-Priority", priorityValue);
        }
    }

    /**
     * 设置消息ID
     * @param value
     * @throws javax.mail.MessagingException
     */
    private void setMessageID(String value) throws MessagingException {
        msg.setMessageID(value);
    }

    /**
     * Take an original MimeMessage object and forward it to a new destination.
     *
     * @param p_msgFrom Email address of who the message is from. e.g. "jdoe@test.com"
     * @param p_msgTo Email address of who the message is to. e.g. "jdoe@test.com"
     * @param p_msgSubject Subject for the forwarded email message.
     * @param p_msgBody Body of the forwarded email message.
     * @param msgOrig The original MimeMessage that is being forwarded.
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */
    private void forwardMessage(String p_msgFrom,
            String p_msgTo,
            String p_msgSubject,
            String p_msgBody,
            MimeMessage msgOrig) throws MessagingException,
            UnsupportedEncodingException {
        forwardMessage(p_msgFrom, null, p_msgTo, null, p_msgSubject, p_msgBody,
                msgOrig);
    }

    /**
     * 转发消息
     * @param p_msgFrom
     * @param p_msgFromName
     * @param p_msgTo
     * @param p_msgToName
     * @param p_msgSubject
     * @param p_msgBody
     * @param msgOrig
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    private void forwardMessage(String p_msgFrom,
            String p_msgFromName,
            String p_msgTo,
            String p_msgToName,
            String p_msgSubject,
            String p_msgBody,
            MimeMessage msgOrig) throws MessagingException,
            UnsupportedEncodingException {
        this.setFrom(p_msgFrom, p_msgFromName);
        this.setTo(p_msgTo, p_msgToName);
        this.setSubject(p_msgSubject);
        this.setBody(p_msgBody);

        MimeBodyPart part = new MimeBodyPart();
        part.setDisposition(Part.ATTACHMENT);
        part.setFileName(msgOrig.getSubject());
        part.setContent(msgOrig, "message/rfc822");

        mp.addBodyPart(part);
    }

    /**
     * 添加一个附件
     * @param p_fileName 附件全路径名
     * @throws javax.mail.MessagingException
     */
    public void addFileAttachment(String p_fileName) throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart mbp_file = new MimeBodyPart();

        //
        //Attach the file to the message
        //
        FileDataSource fds = new FileDataSource(p_fileName);
        mbp_file.setDataHandler(new DataHandler(fds));
        mbp_file.setFileName(MimeUtility.encodeText(fds.getName(),"GBK","B"));
//        mbp_file.setFileName("=?GBK?B?"+ Base64.encodeToString(fds.getName().getBytes(), true)+"?=");

        mp.addBodyPart(mbp_file);
    }

    /**
     * 添加一个附件
     * @param fileName
     * @param inputStream
     * @param streamLen
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    private void addFileAttachmentFromStream(String fileName,
            InputStream inputStream,
            int streamLen) throws MessagingException, IOException {

        String logMsg = "";
        byte b[] = new byte[streamLen];
        byte encodeByte[] = new byte[streamLen * 3];
        String encodeString = new String("");

        BufferedInputStream bistrm = new BufferedInputStream(inputStream);
        int bytes_read = 0;
        bytes_read = bistrm.read(b, 0, streamLen);

        logMsg = "Bytes Read From Stream: " + Integer.toString(bytes_read);
        log.debug(logMsg);

        /*
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Base64Encoder b64 = new Base64Encoder(new ByteArrayInputStream(b), bos);
        b64.process();
        enc_b = bos.toByteArray();
         */
//        encodeByte = Base64.encodeToByte(b, false);
        encodeByte = Base64.encode(b);

        logMsg = "Encoded Byte Count: " + Integer.toString(encodeByte.length);
        log.debug(logMsg);

        InternetHeaders hdr = new InternetHeaders();
        hdr.addHeader("Content-Type", "application/octet-stream; name=\"" + fileName + "\"");
        hdr.addHeader("Content-Transfer-Encoding", "base64");
        hdr.addHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

        MimeBodyPart mbp_file = new MimeBodyPart(hdr, encodeByte);
        mp.addBodyPart(mbp_file);

    }

    /**
     * 发送邮件，这是一个阻塞的方法
     * @throws javax.mail.MessagingException
     */
    public void sendMsg() throws MessagingException {

        //
        //add the Multipart to the message
        //
        msg.setContent(mp);

        //
        // set the Date: header
        //
        msg.setSentDate(new Date());

        msg.saveChanges();

        //TEA 05/13/03, New method for sending so that we
        //can support SMTP authentication
        if (usingAuthentication) {
            Transport transport = getSession().getTransport("smtp");
            transport.connect(smtpHost, smtpUsername, smtpPassword);
            if (msg == null) {
                System.out.println("msg null");
            }
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } else {
            Transport.send(msg);
        }

    }

    /**
     * 启用SSL
     */
    public void setSSLEnable(){
        setSSLEnabled(true);
    }

    /**
     * 设置是否启用SSL
     */
    public void setSSLEnabled(boolean flag) {
        usingSSL = flag;
        if(usingSSL){
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
        }else{
            props.remove("mail.smtp.starttls.enable");
            props.remove("mail.smtp.socketFactory.class");
            props.remove("mail.smtp.socketFactory.fallback");
        }
    }

    /**
     * 设置发送超时毫秒数
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
        props.put("mail.smtp.timeout", String.valueOf(timeout));
    }

    /**
     * 获取session
     * @return
     */
    public Session getSession() {
        return session;
    }

    public static void main(String[] args) throws Exception {
        EmailSenderDriver esd = new EmailSenderDriver();
        esd.setTo("imfxcn@yeah.net", "fjfjfj");
        esd.setFrom("topheytest@163.com", "测试返馈");
        esd.setSubject("京都府");
        esd.setBody("<html>测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正文</html>");
        esd.setSMTPHost("smtp.163.com", "topheytest@163.com", "winnie");
        esd.setSMTPPort(25);
        esd.setTimeout(1000 * 30);
//        esd.setSSLEnable();
        esd.sendMsg();
    }
}
