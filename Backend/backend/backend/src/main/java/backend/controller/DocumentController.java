package backend.controller;

import backend.service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import backend.service.OllamaService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentController {
private final OllamaService ollamaService;
    private final PdfService pdfService;

    public DocumentController(PdfService pdfService, OllamaService ollamaService) {
    this.pdfService = pdfService;
    this.ollamaService = ollamaService;
    }
    @GetMapping("/health")
    public String healthCheck() {
        return "Document Summary Assistant Backend is running!";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please upload a file.");
        }

        try {
            String extractedText = pdfService.extractText(file);

            String summary = ollamaService.summarize(extractedText);

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error reading PDF: " + e.getMessage());
        }
    }
}