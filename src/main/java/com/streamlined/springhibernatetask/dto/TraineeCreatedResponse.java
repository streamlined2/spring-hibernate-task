package com.streamlined.springhibernatetask.dto;

import lombok.Builder;

@Builder
public record TraineeCreatedResponse(Long id, String userName, char[] password) {

    @Override
    public String toString() {
        return "TraineeCreatedResponse{userId=%d}".formatted(id());
    }

}
