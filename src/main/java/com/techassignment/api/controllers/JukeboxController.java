package com.techassignment.api.controllers;

import com.techassignment.api.models.Jukebox;
import com.techassignment.api.services.JukeboxResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class JukeboxController {

    private JukeboxResolverService jukeboxResolverService;

    @Autowired
    public JukeboxController(JukeboxResolverService jukeboxResolverService) {
        this.jukeboxResolverService = jukeboxResolverService;
    }

    @GetMapping("/compatible/{id}")
    public ResponseEntity<List<Jukebox>> getJukeboxes(
            @PathVariable("id") String id,
            @RequestParam(name = "model", required = false) Optional<String> model,
            @RequestParam(name = "offset", required = false) Optional<Integer> offset,
            @RequestParam(name = "limit", required = false) Optional<Integer> limit) {

        List<Jukebox> jukes = jukeboxResolverService.getCompatibleJukes(UUID.fromString(id), model);

        return ResponseEntity.ok(jukes.subList(offset.orElse(0), limit.orElse(jukes.size())));
    }

}
