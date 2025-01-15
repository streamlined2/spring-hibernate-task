package dto;

import lombok.Builder;

@Builder
public record TraineeCreatedResponse(Long userId, String userName, char[] password) {

    @Override
    public String toString() {
        return "TraineeCreatedResponse{userId=%d}".formatted(userId());
    }

}
