package com.realthon.on.ai.groq.dto.response;


import com.realthon.on.ai.groq.dto.RecommendedEventDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendEventsResponse {
    private List<RecommendedEventDto> items;
}