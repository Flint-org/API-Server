package com.flint.flint.community.domain.post;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.community.dto.request.PostCommentUpdateRequest;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 댓글 엔티티
 *
 * @author 신승건
 * @since 2023-08-04
 */
@Entity
@Getter
@NoArgsConstructor
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 300, nullable = false)
    private String contents;

    @Builder
    public PostComment(Post post, PostComment parentComment, List<PostComment> replies, Member member, String contents) {
        this.post = post;
        this.parentComment = parentComment;
        this.member = member;
        this.replies = new ArrayList<>();
        this.contents = contents;
    }

    public void setParentComment(PostComment parentComment) {
        this.parentComment = parentComment;
    }
    public void addCommentReply(PostComment reply) {
        this.replies.add(reply);
        reply.setParentComment(this);
    }

    public void updateContent(String contents) {
        this.contents = contents;
    }
}
