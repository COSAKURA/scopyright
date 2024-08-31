package lltw.scopyright.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sakura
 */
@Component
@Slf4j
public class SendMail {

    private final JavaMailSender sender;

    @Autowired
    public SendMail(JavaMailSender sender) {
        this.sender = sender;
    }

    // 发送邮件
    public void sendSimpleMail(String subject, String to, String content) {

        log.info("++++++++++++++++++++++++++++subject: " + subject);
        log.info("++++++++++++++++++++++++++++from: " + to);
        log.info("++++++++++++++++++++++++++++content: " + content);

        if (sender == null) {
            log.error("JavaMailSender is not initialized");
            return;
        }

        // 编写并发送邮件
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(content);
        String from = "847070349@qq.com";
        mail.setTo(to);
        mail.setFrom(from);

        try {
            sender.send(mail);
            log.info("邮件发送成功");
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }
}
