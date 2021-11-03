package com.mh.match.group.projectboard.comment.dto;

import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.group.projectboard.comment.entity.ProjectArticleComment;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectArticleCommentResponseDto {

    @ApiModelProperty(name = "id", notes = "댓글의 id")
    private Long id;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto writer;

    @ApiModelProperty(name = "content", notes = "댓글 내용")
    private String content;
    @ApiModelProperty(name = "isModified", notes = "수정 여부(수정되었으면 댓글에 수정됨 표시)")
    private Boolean isModified;
    @ApiModelProperty(name = "isDeleted", notes = "삭제 여부(삭제되었으면 삭제된 메세지입니다 표시)")
    private Boolean isDeleted;
    @ApiModelProperty(name = "parentId", notes = "부모의 id(id끼리 묶을 수 있음)")
    private Long parentId;
    @ApiModelProperty(name = "depth", notes = "댓글의 뎁스 표시(1이면 대댓글을 의미)")
    private int depth;
    @ApiModelProperty(name = "replyCount", notes = "대댓글 수")
    private int replyCount;

    @ApiModelProperty(notes = "생성일")
    private LocalDateTime createDate;
    @ApiModelProperty(notes = "수정일")
    private LocalDateTime modifiedDate;

    public static ProjectArticleCommentResponseDto from(ProjectArticleComment pac){
        return ProjectArticleCommentResponseDto.builder()
            .id(pac.getId())
            .writer(MemberSimpleInfoResponseDto.from(pac.getMember()))
            .content(pac.getContent())
            .isModified(pac.getIsModified())
            .isDeleted(pac.getIsDeleted())
            .parentId(pac.getParentId())
            .depth(pac.getDepth())
            .replyCount(pac.getReplyCount())
            .createDate(pac.getCreateDate())
            .modifiedDate(pac.getModifiedDate())
            .build();
    }
}
