package com.sarafan.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String test;
}
