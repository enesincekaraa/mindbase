package dev.enes.mindbase.service;


import dev.enes.mindbase.context.TenantContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;
    @Value("${mindbase.rules.filepath:classpath:kurallar.txt}")
    private Resource rulesFile;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int loadRulesFromFile() throws IOException {
        return readBreakSave(rulesFile);
    }

    public int processAndStoreFile(MultipartFile file) throws IOException {
        return readBreakSave(file.getResource());
    }

    private int readBreakSave(Resource resource) throws IOException {

        String tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID bulunamadı! Dosya yüklenemez.");
        }

        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        for (Document doc : documents){
            doc.getMetadata().put("tenantId", tenantId);
        }

        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(documents);
        vectorStore.accept(chunks);
        return chunks.size();
    }
}
