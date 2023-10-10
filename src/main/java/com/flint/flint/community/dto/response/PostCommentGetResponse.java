package com.flint.flint.community.dto.response;

import com.flint.flint.community.domain.post.PostComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PostCommentGetResponse {

    private long postCommentId;
    private String contents;
    private List<PostCommentGetResponse> replies = new ArrayList<>();

    public PostCommentGetResponse(long id, String contents) {
        this.postCommentId = id;
        this.contents = contents;
    }

    public static PostCommentGetResponse convertCommentToDTO(PostComment postComment) {
        return new PostCommentGetResponse(postComment.getId(), postComment.getContents());
    }
}
