package dev.enes.mindbase.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngestionController {

    private final VectorStore vectorStore;
    public IngestionController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/api/load-data")
    public String loadData() {
        String companyRules = "Mindbase Şirket Kuralları: Şirketimizde standart mesai saatleri sabah 09:00 ile akşam 18:00 arasındadır. " +
                "Öğle molası 12:30 ile 13:30 saatleri arasındadır. Tüm çalışanların haftada 2 gün uzaktan (remote) çalışma hakkı bulunmaktadır. " +
                "Yıllık ücretli izin hakkı, şirkette 1. yılını dolduran her çalışan için 14 iş günüdür. " +
                "Donanım (bilgisayar vb.) arızalarında doğrudan IT departmanına talep açılmalıdır.";


        Document document = new Document(companyRules);


        TokenTextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.apply(List.of(document));

        vectorStore.accept(chunks);

        return "Başarılı! Veriler parçalandı, yapay zeka vektörlerine çevrildi ve veritabanına kaydedildi. Oluşan Parça Sayısı: " + chunks.size();
    }

}
