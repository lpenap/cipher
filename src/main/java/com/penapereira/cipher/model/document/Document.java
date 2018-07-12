package com.penapereira.cipher.model.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Document {

    @Id
    @GeneratedValue
    protected Long id;

    protected String title;

    @Column(length = 140000000)
    protected String text;
}
