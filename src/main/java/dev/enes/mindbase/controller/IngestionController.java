package dev.enes.mindbase.controller;

import dev.enes.mindbase.rabbitmq.producer.DocumentProducer;
import dev.enes.mindbase.service.DocumentIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/documents")
public class IngestionController {

    private final DocumentIngestionService ingestionService;

    public IngestionController(DocumentIngestionService ingestionService, DocumentProducer documentProducer) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) {
            try {
                ingestionService.queueFileForProcessing(file);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Dosyanız başarıyla kuyruğa alındı. Arka planda asenkron olarak işlenecek.");
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Dosya kuyruğa atılırken hata oluştu: " + e.getMessage());
            }
    }


}
