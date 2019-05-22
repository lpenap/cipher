package com.penapereira.cipher.model.settings;

import java.util.Properties;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ApplicationSettings {

    @Id
    private String id;

    private Long version;

    Properties properties;
}
