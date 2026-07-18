package dev.enes.mindbase.service;

import dev.enes.mindbase.context.TenantContext;
import dev.enes.mindbase.rabbitmq.message.DocumentMessage;
import dev.enes.mindbase.rabbitmq.producer.DocumentProducer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentIngestionService {
    private final DocumentProducer documentProducer;

    public DocumentIngestionService(DocumentProducer documentProducer) {
        this.documentProducer = documentProducer;
    }

    public void queueFileForProcessing(MultipartFile file) throws IOException {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) throw new IllegalStateException("Tenant kimliği bulunamadı.");

        String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);

        DocumentMessage message = new DocumentMessage(
                fileContent,
                file.getOriginalFilename(),
                tenantId
        );
        documentProducer.sendToQueue(message);
    }

}
