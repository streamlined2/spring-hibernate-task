package dto;

import lombok.Builder;

@Builder
public record TrainerCreatedResponse(Long userId, String userName, char[] password) {

    @Override
    public String toString() {
        return "TrainerCreatedResponse{userId=%d}".formatted(userId());
    }

}
