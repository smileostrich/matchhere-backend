package com.mh.match.s3.repository;


import com.mh.match.s3.entity.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBFileRepository extends JpaRepository<DBFile, String> {

}
