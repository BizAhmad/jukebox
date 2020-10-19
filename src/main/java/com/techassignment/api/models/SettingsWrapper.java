package com.techassignment.api.models;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class SettingsWrapper {
    private List<Setting> settings;

    public SettingsWrapper() {
        settings = new ArrayList<>();
    }

    public <T> SettingsWrapper(List<Setting> settings) {
        this.settings = settings;
    }
}
