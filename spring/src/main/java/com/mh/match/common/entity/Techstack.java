package com.mh.match.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.techstack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Techstack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String img_uri;

    public Techstack(String name, String img_uri) {
        this.name = name;
        this.img_uri = img_uri;
    }
}