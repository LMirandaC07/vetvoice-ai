package com.vetvoice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// FASE 3: primeiro contato com Spring AI. Por enquanto o agente sÃ³ CONVERSA
// (sem Tool Calling ainda - isso Ã© a Fase 4). A ideia aqui Ã© vocÃª entender
// como o ChatClient funciona antes de dar poder de executar aÃ§Ãµes pra ele.
@RestController
@RequestMapping("/api/ai")
public class ClinicAiController {

    private final ChatClient chatClient;

    // ChatClient.Builder Ã© injetado automaticamente pelo Spring AI a partir
    // da config em application.yml (spring.ai.openai.*). O .build() aqui
    // fixa um "system prompt" padrÃ£o pra todas as conversas desse client.
    public ClinicAiController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        VocÃª Ã© o assistente virtual da VetVoice, uma clÃ­nica veterinÃ¡ria.
                        Responda de forma curta, educada e profissional, em portuguÃªs.
                        VocÃª ainda nÃ£o tem acesso ao sistema de agendamentos - se o
                        usuÃ¡rio pedir para marcar, cancelar ou consultar um horÃ¡rio,
                        explique que essa funÃ§Ã£o estÃ¡ sendo implementada em breve.
                        """)
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

