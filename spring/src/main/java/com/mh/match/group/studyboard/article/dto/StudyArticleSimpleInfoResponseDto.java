package com.mh.match.group.studyboard.article.dto;

import com.mh.match.group.studyboard.article.entity.StudyArticle;
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
public class StudyArticleSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    private Long articleId;

    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @ApiModelProperty(name = "studyBoard", example = "일정게시판")
    private String studyBoard;

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

    public static StudyArticleSimpleInfoResponseDto of(StudyArticle studyArticle, List<String> tags) {
        return StudyArticleSimpleInfoResponseDto.builder()
            .articleId(studyArticle.getId())
            .title(studyArticle.getTitle())
            .studyBoard(studyArticle.getStudyBoard().getName())
            .writer(MemberSimpleInfoResponseDto.from(studyArticle.getMember()))
            .createdDate(studyArticle.getCreateDate())
            .modifiedDate(studyArticle.getModifiedDate())
            .viewCount(studyArticle.getViewCount())
            .tags(tags)
            .build();
    }

}
