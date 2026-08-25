package backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.stereotype.Service;

@Service
public class SummaryPdfService {

    public byte[] generatePdf(String summary) throws IOException {

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream =
                    new PDPageContentStream(document, page);

            contentStream.beginText();

            contentStream.setFont(
                    new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
                    18
            );

            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Document Summary");

            contentStream.setFont(
                    new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                    11
            );

            contentStream.newLineAtOffset(0, -35);

            String[] lines = summary.split("\n");

            float y = 715;

            for (String line : lines) {

                if (y < 50) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);

                    contentStream =
                            new PDPageContentStream(document, page);

                    contentStream.beginText();
                    contentStream.setFont(
                            new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                            11
                    );

                    y = 750;
                    contentStream.newLineAtOffset(50, y);
                }

                String safeLine = line
                   .replace("\t", "    ")
                   .replace("•", "-")
                   .replace("–", "-")
                   .replace("—", "-")
                   .replace("’", "'")
                   .replace("“", "\"")
                   .replace("”", "\"");

                contentStream.showText(safeLine);

                contentStream.newLineAtOffset(0, -15);
                y -= 15;
            }

            contentStream.endText();
            contentStream.close();

            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }
}