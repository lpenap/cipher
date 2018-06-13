package com.penapereira.cipher.model.document;

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
    protected String text;
}
