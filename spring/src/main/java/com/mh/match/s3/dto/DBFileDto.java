package com.mh.match.s3.dto;

import com.mh.match.s3.entity.DBFile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DBFileDto {
    private String id;
    private String file_name;
    private String file_type;
    private String download_uri;

    public static DBFileDto of(DBFile dbFile) {
        if (dbFile == null) {
            return DBFileDto.builder()
                    .build();
        }
        return DBFileDto.builder()
                .id(dbFile.getId())
                .file_name(dbFile.getFile_name())
                .file_type(dbFile.getFile_type())
                .download_uri(dbFile.getDownload_uri())
                .build();
    }

    @Builder
    public DBFileDto(String id, String file_name, String file_type, String download_uri) {
        this.id = id;
        this.file_name = file_name;
        this.file_type = file_type;
        this.download_uri = download_uri;
    }
}
