package com.mh.match.mogakco.dto;

import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.mogakco.entity.MogakcoArticle;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MogakcoArticleSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    private Long articleId;

    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto writer;

    @ApiModelProperty(name = "createdDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime createdDate;

    @ApiModelProperty(name = "modifiedDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime modifiedDate;

    @ApiModelProperty(example = "[\"태그\", \"질문\"]")
    private List<String> tags;

    public static MogakcoArticleSimpleInfoResponseDto of(MogakcoArticle mogakcoArticle, List<String> tags) {
        return MogakcoArticleSimpleInfoResponseDto.builder()
                .articleId(mogakcoArticle.getId())
                .title(mogakcoArticle.getTitle())
                .writer(MemberSimpleInfoResponseDto.from(mogakcoArticle.getMember()))
                .createdDate(mogakcoArticle.getCreateDate())
                .modifiedDate(mogakcoArticle.getModifiedDate())
                .tags(tags)
                .build();
    }

}