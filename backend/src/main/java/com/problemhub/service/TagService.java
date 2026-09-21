package com.problemhub.service;

import com.problemhub.exception.BadRequestException;
import com.problemhub.exception.ResourceNotFoundException;
import com.problemhub.model.Tag;
import com.problemhub.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    @Transactional
    public Tag createTag(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new BadRequestException("Tag name cannot be empty");
        }
        String trimmed = name.trim();
        if (tagRepository.existsByName(trimmed)) {
            throw new BadRequestException("Tag already exists: " + trimmed);
        }
        return tagRepository.save(new Tag(null, trimmed));
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
    }
}
