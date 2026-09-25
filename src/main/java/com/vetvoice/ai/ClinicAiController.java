package com.vetvoice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@ConditionalOnProperty(name = "vetvoice.ai.rag.enabled", havingValue = "true", matchIfMissing = true)
public class ClinicAiController {

    private final ChatClient chatClient;

    public ClinicAiController(ChatClient.Builder builder, VectorStore clinicKnowledgeBase) {
        SearchRequest searchRequest = SearchRequest.defaults()
                .withTopK(4)
                .withSimilarityThreshold(0.60);

        this.chatClient = builder
                .defaultSystem("""
                        VocÃª Ã© o assistente virtual da VetVoice, uma clÃ­nica veterinÃ¡ria.
                        Responda em portuguÃªs, de forma curta, educada e profissional.
                        Use o contexto recuperado da base da clÃ­nica para responder dÃºvidas.
                        Se a base nÃ£o trouxer informaÃ§Ã£o suficiente, diga claramente que nÃ£o possui
                        aquela informaÃ§Ã£o em vez de inventar uma resposta.
                        VocÃª ainda nÃ£o executa aÃ§Ãµes de agendamento ou cancelamento.
                        """)
                .defaultAdvisors(new QuestionAnswerAdvisor(clinicKnowledgeBase, searchRequest))
                .build();
    }

    public record AskRequest(String question) {}
    public record AskResponse(String answer) {}

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {
        String answer = chatClient.prompt()
                .user(request.question())
                .call()
                .content();
        return new AskResponse(answer);
    }
}
