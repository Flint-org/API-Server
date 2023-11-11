package com.flint.flint.community.domain.post;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 엔티티
 *
 * @author 신승건
 * @since 2023-08-04
 */
@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String contents;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostScrap> postScraps = new ArrayList<>();

    @Builder
    public Post(Member member, Board board, String title, String contents) {
        this.member = member;
        this.board = board;
        this.title = title;
        this.contents = contents;
    }

    public void addImageUrl(PostImage postImage) {
        this.postImages.add(postImage);
        postImage.changePost(this);
    }

    public void addPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
        postLike.changePost(this);
    }
}
