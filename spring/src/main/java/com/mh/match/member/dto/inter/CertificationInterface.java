package com.mh.match.member.dto.inter;


import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CertificationInterface {
    Long getId();
    String getName();
    String getOrganization();
    String getCode();
    String getGrade();
    LocalDate getIssued_date();
    LocalDate getExpired_date();
    Boolean getIs_expire();
}
