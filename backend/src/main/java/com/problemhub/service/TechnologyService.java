package com.problemhub.service;

import com.problemhub.exception.BadRequestException;
import com.problemhub.exception.ResourceNotFoundException;
import com.problemhub.model.Technology;
import com.problemhub.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    public Technology getTechnologyById(Long id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id));
    }

    @Transactional
    public Technology createTechnology(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException("Technology name cannot be empty");
        }
        String trimmed = name.trim();
        if (technologyRepository.existsByName(trimmed)) {
            throw new BadRequestException("Technology already exists: " + trimmed);
        }
        return technologyRepository.save(new Technology(null, trimmed));
    }

    @Transactional
    public void deleteTechnology(Long id) {
        if (!technologyRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Technology not found with id: " + id);
        }
    }
}
