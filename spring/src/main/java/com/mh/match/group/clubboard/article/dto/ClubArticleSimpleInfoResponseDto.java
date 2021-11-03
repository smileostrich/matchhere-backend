package com.mh.match.group.clubboard.article.dto;

import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubArticleSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    private Long articleId;

    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @ApiModelProperty(name = "clubBoard", example = "일정게시판")
    private String clubBoard;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto writer;

    @ApiModelProperty(name = "createdDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime createdDate;

    @ApiModelProperty(name = "modifiedDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime modifiedDate;

    @ApiModelProperty(name = "viewCount", example = "2")
    private Integer viewCount;

    @ApiModelProperty(example = "[\"태그\", \"질문\"]")
    private List<String> tags;

    public static ClubArticleSimpleInfoResponseDto of(ClubArticle clubArticle, List<String> tags) {
        return ClubArticleSimpleInfoResponseDto.builder()
                .articleId(clubArticle.getId())
                .title(clubArticle.getTitle())
                .clubBoard(clubArticle.getClubBoard().getName())
                .writer(MemberSimpleInfoResponseDto.from(clubArticle.getMember()))
                .createdDate(clubArticle.getCreateDate())
                .modifiedDate(clubArticle.getModifiedDate())
                .viewCount(clubArticle.getViewCount())
                .tags(tags)
                .build();
    }

}