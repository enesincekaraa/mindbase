package dev.enes.mindbase.rabbitmq.consumer;

import dev.enes.mindbase.rabbitmq.config.RabbitMQConfig;
import dev.enes.mindbase.rabbitmq.message.DocumentMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentConsumer {

    private final VectorStore vectorStore;

    public DocumentConsumer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @RabbitListener(queues = RabbitMQConfig.DOCUMENT_QUEUE)
    public void consumeDocumentAndStore(DocumentMessage message) {
        System.out.println("⏳ Kuyruktan dosya alındı, işleniyor... Dosya: " + message.fileName());

        try {

            // --- TEST İÇİN EKLENEN BOMBA ---
            if (message.content().contains("BOMBA")) {
                throw new RuntimeException("Sistem bilerek patlatıldı!");
            }
            Document document = new Document(
                    message.content(),
                    Map.of("tenantId",message.tenantId()));

            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> chunks = splitter.apply(List.of(document));
            vectorStore.accept(chunks);
            System.out.println("✅ İşlem tamamlandı! " + chunks.size() + " parça vektör DB'ye kaydedildi. Kurum: " + message.tenantId());

        }catch (Exception e){
            System.err.println("❌ Dosya işlenirken hata oluştu: " + e.getMessage());

            throw new AmqpRejectAndDontRequeueException("Mesaj işlenemedi , DLQ'ya aktarılıyor." ,e);
        }
    }
}
