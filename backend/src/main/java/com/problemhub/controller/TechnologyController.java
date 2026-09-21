package com.problemhub.controller;

import com.problemhub.model.Technology;
import com.problemhub.service.TechnologyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    public ResponseEntity<List<Technology>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technology> getTechnologyById(@PathVariable Long id) {
        return ResponseEntity.ok(technologyService.getTechnologyById(id));
    }

    @PostMapping
    public ResponseEntity<Technology> createTechnology(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        Technology created = technologyService.createTechnology(name);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }
}
