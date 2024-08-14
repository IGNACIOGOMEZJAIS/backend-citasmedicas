package com.citasmedica.backend.backendcitasmedicas.services;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class EmailService {

    private final String sendGridApiKey;

    public EmailService() {
        Dotenv dotenv = Dotenv.load();
        this.sendGridApiKey = dotenv.get("SENDGRID_API_KEY");
    }

    public void sendEmail(String toEmail, String subject, String body) throws IOException {
        Email from = new Email("gomezdrernesto@gmail.com");
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

    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] pdfContent) throws IOException {
        Email from = new Email("gomezdrernesto@gmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);

        // Configurar el archivo adjunto
        Attachments attachments = new Attachments();
        attachments.setFilename("receta_medica.pdf");
        attachments.setType("application/pdf");
        attachments.setDisposition("attachment");
        attachments.setContent(Base64.getEncoder().encodeToString(pdfContent));  // Codificar el contenido en Base64
        mail.addAttachments(attachments);

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
