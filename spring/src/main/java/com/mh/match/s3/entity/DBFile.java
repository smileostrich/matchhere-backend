package com.mh.match.s3.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "matching.files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DBFile {
    @Id
    private String id;

    private String file_name;
    private String file_type;
    private String download_uri;

    @Builder
    public DBFile(String id, String file_name, String file_type, String download_uri) {
        this.id = id;
        this.file_name = file_name;
        this.file_type = file_type;
        this.download_uri = download_uri;
    }
}
