package com.example.controllers;

import com.example.models.Attendance;
import com.example.repositories.AttendanceRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/admin/attendance")
public class AttendanceReportController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceReportController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

   @GetMapping("/report")
public ResponseEntity<byte[]> generateAttendanceReport() {
    List<Attendance> attendanceRecords = attendanceRepository.findAll();

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Attendance Report"));

        PdfPTable table = new PdfPTable(4);
        table.addCell("ID");
        table.addCell("Student Name");
        table.addCell("Date");
        table.addCell("Status");

        for (Attendance record : attendanceRecords) {
            table.addCell(record.getId().toString());
            table.addCell(record.getStudent().getName());
            table.addCell(record.getDate().toString());
            table.addCell(record.getStatus());
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=attendance_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(out.toByteArray());

    } catch (DocumentException | java.io.IOException e) { // Catch both exceptions
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }
}

}
