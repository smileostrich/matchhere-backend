package com.mh.match.group.projectboard.article.dto;

import com.mh.match.group.projectboard.article.entity.ProjectArticle;
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
public class ProjectArticleSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    private Long articleId;
    @ApiModelProperty(example = "게시물 제목")
    private String title;
    @ApiModelProperty(name = "projectBoard", example = "일정게시판")
    private String projectBoard;

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

    public static ProjectArticleSimpleInfoResponseDto of(ProjectArticle projectArticle, List<String> tags) {
        return ProjectArticleSimpleInfoResponseDto.builder()
                .articleId(projectArticle.getId())
                .title(projectArticle.getTitle())
                .projectBoard(projectArticle.getProjectBoard().getName())
                .writer(MemberSimpleInfoResponseDto.from(projectArticle.getMember()))
                .createdDate(projectArticle.getCreateDate())
                .modifiedDate(projectArticle.getModifiedDate())
                .viewCount(projectArticle.getViewCount())
                .tags(tags)
                .build();
    }

}
