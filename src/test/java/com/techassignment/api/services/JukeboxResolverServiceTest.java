package com.techassignment.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techassignment.api.exceptions.ResourceNotFoundException;
import com.techassignment.api.models.Component;
import com.techassignment.api.models.Jukebox;
import com.techassignment.api.models.Setting;
import com.techassignment.api.models.SettingsWrapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;


class JukeboxResolverServiceTest {

    private final String URL = "http://my-json-server.typicode.com/touchtunes/tech-assignment";

    private static List<Jukebox> savedList;

    @Mock
    private RestTemplate restTemplate = getRestTemplate();

    @Mock ParameterizedTypeReference parameterizedTypeReference = getParametrizedTypeReference();

    @Bean
    public RestTemplate getRestTemplate() {
        return Mockito.mock(RestTemplate.class);
    }
    @Bean
    public ParameterizedTypeReference<List<Jukebox>> getParametrizedTypeReference(){
        return new ParameterizedTypeReference<List<Jukebox>>() {};
    }

    @InjectMocks
    private JukeboxResolverService jukeboxResolverService;

    @BeforeAll
    static void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        savedList = mapper.readValue(Objects.requireNonNull(
                ClassLoader.getSystemResourceAsStream("jukes.json")),
                mapper.getTypeFactory().constructCollectionType(List.class, Jukebox.class));
    }

    @BeforeEach
    void setUp() {
        jukeboxResolverService = new JukeboxResolverService(restTemplate, getParametrizedTypeReference());
    }

    @Test
    void getCompatibleJukes_givenNonValidSettings_shouldReturnResourceNotFound() {
        UUID nonValidUUID = UUID.randomUUID();
        Mockito.when(restTemplate.getForObject(URL+"/settings", SettingsWrapper.class))
                .thenReturn(new SettingsWrapper());

        assertThrows(ResourceNotFoundException.class, () -> jukeboxResolverService.fetchSetting(nonValidUUID));
    }

    @Test
    void filterJukes_givenCompatibleSettingAndJuke_ShouldReturnTrue() {
        Assert.assertTrue(jukeboxResolverService.filterJukes(mockJukebox(), mockSettings()));
    }

    @Test
    void filterJukes_givenNonCompatibleSettingAndJuke_ShouldReturnFalse() {
        Assert.assertFalse(jukeboxResolverService.filterJukes(mockJukebox(), mockNonCompatSettings()));
    }

    @Test
    void fetchSetting_givenValidSetting_ShouldReturnSetting() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(restTemplate.getForObject(URL+"/settings", SettingsWrapper.class))
                .thenReturn(new SettingsWrapper(Collections.singletonList(mockSettings(uuid))));

        Assert.assertSame(new Setting(uuid, Collections.singletonList("money_pcb")).getId(),jukeboxResolverService.fetchSetting(uuid).getId());
    }

    @Test
    void fetchSetting_givenNonValidSetting_ShouldReturn404() {
        UUID nonValidUUID = UUID.randomUUID();
        Mockito.when(restTemplate.getForObject(URL+"/settings", SettingsWrapper.class))
                .thenReturn(new SettingsWrapper());

        assertThrows(ResourceNotFoundException.class, () -> jukeboxResolverService.fetchSetting(nonValidUUID));
    }

    @Test
    void fetchJukesByModel_givenModel_shouldReturnJukesListOnlyWithThatModel() {
        String testModel = "angelina";
        Mockito.when(restTemplate.exchange(URL + "/jukes", HttpMethod.GET,
                null, parameterizedTypeReference)).thenReturn(new ResponseEntity<>(savedList, HttpStatus.OK));

        List<Jukebox> response = jukeboxResolverService.fetchJukesByModel(Optional.of(testModel));
        for (Jukebox juke : response) {
            Assert.assertEquals(juke.getModel(), testModel);
        }
    }

    @Test
    void fetchJukesByModel_givenNonExistingModel_shouldReturnEmptyList() throws IOException {
        String testModel = "NON_EXISTANT";
        Mockito.when(restTemplate.exchange(URL + "/jukes", HttpMethod.GET,
                null, parameterizedTypeReference)).thenReturn(new ResponseEntity<>(savedList, HttpStatus.OK));

        List<Jukebox> response = jukeboxResolverService.fetchJukesByModel(Optional.of(testModel));
        Assert.assertEquals(response.size(), 0);
    }

    private Jukebox mockJukebox() {
        return new Jukebox("5ca94a8acc046e7aa8040605",
                "Angelina",
                Collections.singletonList(new Component("money_pcb")));
    }

    private Setting mockSettings(UUID id) {
        return new Setting(id,
                Collections.singletonList("money_pcb"));
    }

    private Setting mockSettings() {
        return new Setting(UUID.randomUUID(),
                Collections.singletonList("money_pcb"));
    }

    private Setting mockNonCompatSettings() {
        return new Setting(UUID.randomUUID(),
                Arrays.asList("money_pcb", "touchscreen"));
    }

}