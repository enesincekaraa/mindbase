package dev.enes.mindbase.config.mapper;

import dev.enes.mindbase.dto.ChatResponse;

public final class ChatMapper {

    private static final String DEFAULT_ERROR_MESSAGE = "Cevap üretilemedi.";

    private ChatMapper() {
        throw new UnsupportedOperationException("Utility sınıfından nesne üretilemez.");
    }
    public static ChatResponse toResponse( final String aiAnswer, final String tenantId) {
        if (aiAnswer == null || aiAnswer.trim().isEmpty()) {
            return new ChatResponse(DEFAULT_ERROR_MESSAGE, tenantId);
        }
        return new ChatResponse(aiAnswer, tenantId);
    }
}
