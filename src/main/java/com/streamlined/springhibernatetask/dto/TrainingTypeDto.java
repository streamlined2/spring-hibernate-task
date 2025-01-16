package com.streamlined.springhibernatetask.dto;

import lombok.Builder;

@Builder
public record TrainingTypeDto(Long id, String name) {
}
