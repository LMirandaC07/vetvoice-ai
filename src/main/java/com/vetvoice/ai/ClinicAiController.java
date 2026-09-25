package com.vetvoice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// FASE 3: primeiro contato com Spring AI. Por enquanto o agente só CONVERSA
// (sem Tool Calling ainda - isso é a Fase 4). A ideia aqui é você entender
// como o ChatClient funciona antes de dar poder de executar ações pra ele.
@RestController
@RequestMapping("/api/ai")
public class ClinicAiController {

    private final ChatClient chatClient;

    // ChatClient.Builder é injetado automaticamente pelo Spring AI a partir
    // da config em application.yml (spring.ai.openai.*). O .build() aqui
    // fixa um "system prompt" padrão pra todas as conversas desse client.
    public ClinicAiController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        Você é o assistente virtual da VetVoice, uma clínica veterinária.
                        Responda de forma curta, educada e profissional, em português.
                        Você ainda não tem acesso ao sistema de agendamentos - se o
                        usuário pedir para marcar, cancelar ou consultar um horário,
                        explique que essa função está sendo implementada em breve.
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
