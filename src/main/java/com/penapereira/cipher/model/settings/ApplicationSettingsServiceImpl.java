package com.penapereira.cipher.model.settings;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSettingsServiceImpl implements ApplicationSettingsService {

    ApplicationSettingsRepository repository;

    @Autowired
    public ApplicationSettingsServiceImpl(ApplicationSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ApplicationSettings> findAll() {
        return repository.findAll();
    }

    @Override
    public ApplicationSettings save(ApplicationSettings appSettings) {
        return repository.save(appSettings);
    }

    @Override
    public ApplicationSettings findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

}
