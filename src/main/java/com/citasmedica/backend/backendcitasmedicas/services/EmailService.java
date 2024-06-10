package com.citasmedica.backend.backendcitasmedicas.services;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final String sendGridApiKey;
    public EmailService() {
        Dotenv dotenv = Dotenv.load();
        this.sendGridApiKey = dotenv.get("SENDGRID_API_KEY");
    }

    public void sendEmail(String toEmail, String subject, String body) throws IOException {
        Email from = new Email("nacgosh@gmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getBody());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
