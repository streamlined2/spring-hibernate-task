package dto;

import lombok.Builder;

@Builder
public record TrainerDto(Long userId, String firstName, String lastName, String userName, boolean isActive,
        String specialization) {

    @Override
    public String toString() {
        return "TrainerDto{userId=%d}".formatted(userId());
    }

}
