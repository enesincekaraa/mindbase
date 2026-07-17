package dev.enes.mindbase.service;


import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestContent(String content) {
        Document document = new Document(content);
        TokenTextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.apply(List.of(document));
        vectorStore.accept(chunks);
        return chunks.size();
    }
}
