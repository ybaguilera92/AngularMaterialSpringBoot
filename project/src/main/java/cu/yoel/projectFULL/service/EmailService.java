package cu.yoel.projectFULL.service;


import cu.yoel.projectFULL.models.Email;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendSMail(Email email) throws MessagingException, UnsupportedEncodingException;
}
