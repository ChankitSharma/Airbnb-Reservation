package com.airbnb.service;

import com.airbnb.entity.Bookings;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class PDFService {
    public boolean generatePDF(String fileName, Bookings bookings) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            // Add header with logo
            Image img = Image.getInstance("D://Guest house logo.jpg");
            img.scaleToFit(100, 100);
            img.setAlignment(Element.ALIGN_LEFT);
            document.add(img);

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Invoice / Bill", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Empty line for spacing

            // Add booking details table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Table header
            PdfPCell c1 = new PdfPCell(new Phrase("Details", headerFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase("Information", headerFont));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c2);
            table.setHeaderRows(1);

            // Table rows
//            table.addCell(new Phrase("Booking ID:", normalFont));
//            table.addCell(new Phrase(bookings.getId().toString(), normalFont));

            table.addCell(new Phrase("User Name:", normalFont));
            table.addCell(new Phrase(bookings.getPropertyUser().getFirstName().toUpperCase(Locale.ROOT)+" "+bookings.getPropertyUser().getLastName().toUpperCase() ,normalFont));

            table.addCell(new Phrase("Guest Name:", normalFont));
            table.addCell(new Phrase(bookings.getGuestName().toUpperCase(Locale.ROOT), normalFont));

            table.addCell(new Phrase("Resort:", normalFont));
            table.addCell(new Phrase(bookings.getProperty().getPropertyName().toUpperCase(Locale.ROOT), normalFont));

            table.addCell(new Phrase("Total Nights:", normalFont));
            table.addCell(new Phrase(bookings.getTotalNights().toString(), normalFont));

            table.addCell(new Phrase("Nightly Price:", normalFont));
            table.addCell(new Phrase(String.valueOf(bookings.getProperty().getNightlyPrice()+"/-"), normalFont));

            table.addCell(new Phrase("Total Price:", normalFont));
            table.addCell(new Phrase(String.valueOf(bookings.getTotalPrice()+"/-"), normalFont));

            document.add(table);

            // Add footer
            Paragraph footer = new Paragraph("Thank you for booking with us!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace(); // It's a good practice to log the exception
        }
        return false;
    }
}
//    public void generatePDF(String fileName) {
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(fileName));
//
//            document.open();
//            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//            Chunk chunk = new Chunk(fileName, font);
//            document.add(chunk);
//            document.close();
//        } catch(Exception exception) {
//            exception.printStackTrace(); // It's a good practice to log the exception
//        }
//    }
//}