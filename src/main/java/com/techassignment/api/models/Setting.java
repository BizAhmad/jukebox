package com.techassignment.api.models;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@RequiredArgsConstructor
@Setter
@AllArgsConstructor
public class Setting {
    private UUID id;
    private List<String> requires;
}
