package com.web_project.zayavki.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class InMemoryAbstractService<T, UUID, R extends JpaRepository<T, UUID>> implements AbstractService<T, UUID>{
    private final JpaRepository<T, UUID> jpaRepository;

    public InMemoryAbstractService(R jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List findAll() {
        return jpaRepository.findAll(Sort.by("id"));
    }

    @Override
    public T createNote(T model){
        return jpaRepository.save(model);
    }

    @Override
    public T findById(UUID id){
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public T updateNote(T model, UUID id){
        if(jpaRepository.existsById(id)){
            return jpaRepository.save(model);
        }
        return null;
    }

    @Override
    public void deleteNote(UUID id){
        if(jpaRepository.existsById(id)){
            jpaRepository.deleteById(id);
        }
    }
}
