package com.citasmedica.backend.backendcitasmedicas.services;

import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public byte[] generatePdf(String pacienteNombre, String dni, String receta, String fecha, String firma) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            // Crear una página
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear un flujo de contenido para la página
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Escribir el nombre del doctor
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("DR. ERNESTO ARTURO GÓMEZ");
                contentStream.endText();

                // Escribir la especialidad en líneas separadas
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 730); // Ajustar la posición vertical
                contentStream.showText("ESPECIALISTA EN OTORRINOLARINGOLOGÍA");
                contentStream.newLine();
                contentStream.showText("CIRUGÍA MAXILOFACIAL");
                contentStream.newLine();
                contentStream.showText("(NARIZ - GARGANTA - OÍDO)");
                contentStream.newLine();
                contentStream.showText(firma);
                contentStream.endText();

                // Dibujar la línea negra debajo del texto anterior
                contentStream.setLineWidth(1.0f); // Grosor de la línea
                contentStream.moveTo(50, 680); // Punto de inicio de la línea
                contentStream.lineTo(550, 680); // Punto de final de la línea (con el mismo valor Y)
                contentStream.stroke(); // Dibujar la línea

                // Continuar escribiendo el resto del contenido
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 650); // Ajustar la posición vertical
                contentStream.showText("Paciente: " + pacienteNombre);
                contentStream.newLine();
                contentStream.showText("DNI: " + dni);
                contentStream.newLine();
                contentStream.showText("Detalles de la Receta: " + receta);
                contentStream.newLine();
                contentStream.showText("Fecha: " + fecha);
                contentStream.newLine();
                contentStream.endText();

                // Agregar la firma como imagen
                String imagePath = Paths.get("src", "img", "Screenshot 2024-08-14 131800.png").toAbsolutePath()
                        .toString();
                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
                contentStream.drawImage(pdImage, 50, 300, 200, 100);
            }

            document.save(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
