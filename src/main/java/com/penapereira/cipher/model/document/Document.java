package com.penapereira.cipher.model.document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Document implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;

    protected String title;

    @Column(length = 1000000000)
    protected String text;
}
