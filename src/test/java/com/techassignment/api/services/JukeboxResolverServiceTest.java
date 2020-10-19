package com.techassignment.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JukeboxResolverServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCompatibleJukes_givenValidSettings_shouldReturnList() {
    }

    @Test
    void getCompatibleJukes_givenNonValidSettings_shouldReturn404() {
    }


    @Test
    void filterJukes_givenCompatibleSettingAndJuke_ShouldReturnTrue() {
    }

    @Test
    void filterJukes_givenNonCompatibleSettingAndJuke_ShouldReturnFalse() {
    }

    @Test
    void fetchSetting_givenValidSetting_ShouldReturnSetting() {
    }

    @Test
    void fetchSetting_givenNonValidSetting_ShouldReturn404() {
    }

    @Test
    void fetchJukesByModel_givenModel_shouldReturnJukesListOnlyWithThatModel() {
    }

    @Test
    void fetchJukesByModel_givenNonExistingModel_shouldReturnEmptyList() {
    }
}