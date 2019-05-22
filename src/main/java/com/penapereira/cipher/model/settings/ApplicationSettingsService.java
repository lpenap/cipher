package com.penapereira.cipher.model.settings;

import java.util.List;

public interface ApplicationSettingsService {

        List<ApplicationSettings> findAll();

        ApplicationSettings save(ApplicationSettings appSettings);

        ApplicationSettings findById(String id);

        void deleteById(String id);
}
