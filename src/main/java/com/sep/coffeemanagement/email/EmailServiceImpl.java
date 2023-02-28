package com.sep.coffeemanagement.email;

import com.sep.coffeemanagement.exception.BadSqlException;
import com.sep.coffeemanagement.log.AppLogger;
import com.sep.coffeemanagement.log.LoggerFactory;
import com.sep.coffeemanagement.log.LoggerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  protected AppLogger APP_LOGGER = LoggerFactory.getLogger(
    LoggerType.APPLICATION
  );

  @Override
  public void sendSimpleMail(EmailDetail details) {
    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();

      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      javaMailSender.send(mailMessage);
    } catch (Exception e) {
      APP_LOGGER.error(e.getMessage());
      throw new BadSqlException("error email");
    }
  }
}
