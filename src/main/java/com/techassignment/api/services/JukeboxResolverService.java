package com.techassignment.api.services;


import com.techassignment.api.exceptions.ResourceNotFoundException;
import com.techassignment.api.models.Component;
import com.techassignment.api.models.Jukebox;
import com.techassignment.api.models.Setting;
import com.techassignment.api.models.SettingsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JukeboxResolverService {

    private final String URL = "http://my-json-server.typicode.com/touchtunes/tech-assignment";
    private final RestTemplate restTemplate;
    private final ParameterizedTypeReference<List<Jukebox>> parameterizedTypeReference;


    @Autowired
    public JukeboxResolverService(RestTemplate restTemplate, ParameterizedTypeReference<List<Jukebox>> parameterizedTypeReference) {
        this.restTemplate = restTemplate;
        this.parameterizedTypeReference = parameterizedTypeReference;
    }

    public List<Jukebox> getCompatibleJukes(UUID id, Optional<String> model) {
        Setting setting = fetchSetting(id);

        return fetchJukesByModel(model).stream().filter(jukebox -> filterJukes(jukebox, setting)).collect(Collectors.toList());
    }

    public boolean filterJukes(Jukebox jukes, Setting setting) {
        Set<String> requiredSet = new HashSet<>(setting.getRequires());
        Set<String> supportedSet = jukes.getComponents().stream().map(Component::getName).collect(Collectors.toSet());

        return supportedSet.containsAll(requiredSet);
    }

    public Setting fetchSetting(UUID id) throws ResourceNotFoundException {
        SettingsWrapper settingsResponse = restTemplate.getForObject(URL + "/settings", SettingsWrapper.class);

        return settingsResponse.getSettings()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("setting Id", null, id));
    }

    public List<Jukebox> fetchJukesByModel(Optional<String> model) throws ResourceNotFoundException {
        ResponseEntity<List<Jukebox>> jukesResponse = restTemplate.exchange(URL + "/jukes", HttpMethod.GET,
                null, parameterizedTypeReference);

        if (jukesResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResourceNotFoundException("jukebox model", null, model);
        }
        return model.map(s -> jukesResponse.getBody().stream().filter(j -> s.equals(j.getModel())).collect(Collectors.toList())).orElseGet(jukesResponse::getBody);
    }
}
