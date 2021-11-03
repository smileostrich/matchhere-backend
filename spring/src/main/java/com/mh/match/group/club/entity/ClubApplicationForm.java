package com.mh.match.group.club.entity;

import com.mh.match.group.club.dto.request.ClubApplicationRequestDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "matching.club_application_form")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubApplicationForm {

    @EmbeddedId
    private CompositeMemberClub compositeMemberClub;

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    private String bio;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public static ClubApplicationForm of(ClubApplicationRequestDto dto, CompositeMemberClub cm,
                                         String name) {
        return ClubApplicationForm.builder()
                .compositeMemberClub(cm)
                .name(name)
                .bio(dto.getBio())
                .createDate(LocalDateTime.now())
                .build();
    }
}