package com.travelbnb.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.travelbnb.payload.BookingDto;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PDFService {

    private static final String PDF_DIRECTORY="E://pdf_example//";

    public boolean generatePDF(String fileName, BookingDto bookingDto) {

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("E://pdf_example//iTextHelloWorld.pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk bookingConfirmation = new Chunk("Booking Confirmation", font);
            Chunk guestName = new Chunk("Guest Name: "+bookingDto.getName(), font);
            Chunk totalNights = new Chunk("Total Nights: "+bookingDto.getTotalNights(), font);
            Chunk nightlyPrice = new Chunk("Price Per Night: "+bookingDto.getProperty().getNightlyPrice(), font);
            Chunk totalPrice = new Chunk("Total Price: "+bookingDto.getPrice(), font);

            document.add(bookingConfirmation);
            document.add(new Paragraph("\n"));
            document.add(guestName);
            document.add(new Paragraph("\n"));
            document.add(totalNights);
            document.add(new Paragraph("\n"));
            document.add(nightlyPrice);
            document.add(new Paragraph("\n"));
            document.add(totalPrice);


            document.close();
            System.out.println("PDF Directory: " + PDF_DIRECTORY);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        return false;
    }
}
