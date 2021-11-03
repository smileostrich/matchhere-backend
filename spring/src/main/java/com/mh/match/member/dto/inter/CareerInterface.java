package com.mh.match.member.dto.inter;

import java.time.LocalDate;

public interface CareerInterface {
    Long getId();
    String getCompany();
    String getRole();
    LocalDate getStart_date();
    LocalDate getEnd_date();
    Boolean getIs_incumbent();
    String getDescription();
}
