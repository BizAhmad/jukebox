package com.techassignment.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jukebox {

    private String id;
    private String model;
    private List<Component> components = new ArrayList<>();

    public Jukebox(String id, String model, List<Component> components) {
        this.id = id;
        this.model = model;
        this.components = components;
    }
}
