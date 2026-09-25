package com.vetvoice.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "vetvoice.ai.rag.enabled", havingValue = "true", matchIfMissing = true)
public class RagConfig {

    @Bean
    VectorStore clinicKnowledgeBase(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);

        vectorStore.add(List.of(
                document("agendamento", "Consultas da VetVoice devem ser agendadas com um veterinário, um pet e um horário disponível. O sistema não permite dois agendamentos ativos para o mesmo veterinário no mesmo horário."),
                document("cancelamento", "Uma consulta cancelada deixa de bloquear aquele horário para um novo agendamento."),
                document("confirmacao", "Consultas criadas podem ser confirmadas quando o atendimento for validado pela clínica."),
                document("cadastro", "A VetVoice mantém cadastros de clientes, pets e veterinários. Cada pet pertence a um cliente."),
                document("assistente", "O assistente da VetVoice usa uma base de conhecimento e não deve inventar informações ausentes no contexto recuperado."),
                document("tecnologia", "O VetVoice AI usa Java 21, Spring Boot, PostgreSQL, Flyway, Docker e Spring AI com recuperação semântica de contexto.")
        ));

        return vectorStore;
    }

    private Document document(String topic, String content) {
        return new Document(content, Map.of(
                "source", "vetvoice-knowledge-base",
                "topic", topic
        ));
    }
}
