package com.flint.flint.club.response;

import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.dto.MajorsResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClubCommentsAndRepliesResponse {
    private LogoInfoResponse univInfo; // 대학 로고, 이름, 전공
    private String memberName; // member 이름
    private String memberMajor; // member 전공
    private String commentContent; // 댓글 내용
    private List<ClubCommentsAndRepliesResponse> commentReplies = new ArrayList<>(); // 대댓글 내용

    @Builder
    public ClubCommentsAndRepliesResponse(LogoInfoResponse univInfo, String memberName, String memberMajor, String commentContent) {
        this.univInfo = univInfo;
        this.memberName = memberName;
        this.memberMajor = memberMajor;
        this.commentContent = commentContent;
    }

    public void addCommentReply(ClubCommentsAndRepliesResponse reply) {
        this.commentReplies.add(reply);
    }
}
