package com.penapereira.cipher.model.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
