package dev.enes.mindbase.controller;

import dev.enes.mindbase.service.DocumentIngestionService;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class IngestionController {

    private final VectorStore vectorStore;
    private final DocumentIngestionService ingestionService;
    public IngestionController(VectorStore vectorStore, DocumentIngestionService ingestionService) {
        this.vectorStore = vectorStore;
        this.ingestionService = ingestionService;
    }


    @GetMapping("/load-local")
    public ResponseEntity<String> loadLocalData() throws IOException {

        try {
            int chunkSize = ingestionService.loadRulesFromFile();

            return ResponseEntity.ok("Sistemdeki 'kurallar.txt' başarıyla eklendi. Parça: " + chunkSize);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Yerel dosya okunurken hata oluştu: " + e.getMessage());
        }

    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) {
            try {
                int chunkSize = ingestionService.processAndStoreFile(file);
                return ResponseEntity.ok("Dışarıdan gelen dosya başarıyla işlendi! Parça: " + chunkSize);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Dosya işlenirken hata oluştu: " + e.getMessage());
            }
    }


}
