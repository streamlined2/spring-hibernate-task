package com.streamlined.springhibernatetask.dto;

import lombok.Builder;

@Builder
public record TrainerCreatedResponse(Long id, String userName, char[] password) {

    @Override
    public String toString() {
        return "TrainerCreatedResponse{id=%d}".formatted(id());
    }

}
