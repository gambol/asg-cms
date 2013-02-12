/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.weibocount.web.common;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fxcn
 */
public class EmailMimeMessage  extends MimeMessage{
    private static Log log = LogFactory.getLog(EmailMimeMessage.class);

    /** The value stored in the Message-ID header tag for the message.*/
    protected String messageID = "";

    /**
     * Create a new EmailMimeMessage object.
     * @param session The javax.mail.Session object the mail message is for.
     *
     */
    public EmailMimeMessage(javax.mail.Session session) {
        super(session);
    }

    /**
     * Set the value stored in the Message-ID header tag for the message.
     * @param p_value The value of the Message-ID header tag.
     *
     */
    void setMessageID(String value) {
        messageID = value;
    }

    /**
     * Calls the super.updateHeaders() method, and also ensures
     * that the Message-ID header tag
     * gets set if the setMessageID method was called.
     *
     * @throws MessagingException If an error occurs.
     */
    protected void updateHeaders() throws MessagingException {
        super.updateHeaders();
        if (messageID != null && messageID.length() > 0) {
            setHeader("Message-ID", messageID);
        }
    }

}
