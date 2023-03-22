package com.sep.coffeemanagement.utils;

import com.sep.coffeemanagement.dto.mail.DataMailDto;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class MailSenderUtil {
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  public void sendHtmlMail(DataMailDto dataMail, String templateName)
    throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

    Context context = new Context();
    context.setVariables(dataMail.getProps());

    String html = templateEngine.process(templateName, context);

    helper.setTo(dataMail.getTo());
    helper.setSubject(dataMail.getSubject());
    helper.setText(html, true);

    mailSender.send(message);
  }

  public String autoGeneratePassword() {
    String[] specials = { "!", "@", "#", "$", "%", "^", "&", "*" };
    Random r = new Random();
    StringBuilder sb = new StringBuilder(
      RandomStringUtils.randomAlphabetic(1).toUpperCase()
    );
    sb.append(RandomStringUtils.randomAlphabetic(7));
    sb.append(r.nextInt(10));
    sb.append(specials[r.nextInt(specials.length - 1)]);
    return sb.toString();
  }
}
