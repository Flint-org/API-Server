package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostImage;
import com.flint.flint.community.dto.request.PostRequest;
import com.flint.flint.community.dto.response.PostListResponse;
import com.flint.flint.community.dto.response.PostPreSignedUrlResponse;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.community.spec.SortStrategy;
import com.flint.flint.media.dto.response.PreSignedUrlResponse;
import com.flint.flint.media.service.MediaService;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.flint.flint.common.spec.ResultCode.*;

/**
 * 게시글 서비스
 *
 * @author 신승건
 * @since 2023-09-13
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final MediaService mediaService;
    private final MemberService memberService;
    private static final String POST_IMAGE_FOLDER_NAME = "static";
    private static final int MAX_IMAGE_LIMIT = 20;

    @Transactional
    public List<PostPreSignedUrlResponse> createPost(String providerId, PostRequest postRequest) {
        Member member = memberService.getMemberByProviderId(providerId);

        // 이미지 개수 제한 검증
        if (isExceedMaxImage(postRequest.getFileNames().size()))
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, EXCESS_POST_IMAGE_LIMIT);

        Board board = boardService.getBoard(postRequest.getBoardId());

        Post post = Post.builder()
                .board(board)
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .member(member)
                .build();

        List<PostPreSignedUrlResponse> preSignedUrls = new ArrayList<>();

        // 저장할 파일이 있다면
        for (String fileName : postRequest.getFileNames()) {
            String path = "/" + POST_IMAGE_FOLDER_NAME + "/" + fileName;

            // pre-signed url 요청
            PreSignedUrlResponse preSignedURL = mediaService.getPreSignedURL(POST_IMAGE_FOLDER_NAME, fileName);

            // 이미지 엔티티 생성
            PostImage postImage = PostImage.builder()
                    .imgUrl(path)
                    .build();

            // 게시글의 이미지 리스트에 추가
            post.addImageUrl(postImage);

            // 응답 데이터 추가
            preSignedUrls.add(PostPreSignedUrlResponse.builder()
                    .fileName(fileName)
                    .preSignedUrl(preSignedURL.getPreSignedUrl())
                    .build()
            );
        }
        postRepository.save(post);
        return preSignedUrls;
    }

    /**
     * 게시글 목록 조회 페이징
     *
     * @param boardId      게시판 ID
     * @param cursorId     현재 커서 ID(기준 게시글 ID)
     * @param size         가져올 개수
     * @param sortStrategy 정렬 전략
     * @return 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<PostListResponse> getPostsByPaging(Long boardId, Long cursorId, Long size, SortStrategy sortStrategy) {
        if (size == null || size < 1) {
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, INVALID_PAGE_SIZE);
        }

        Post post = null;
        if (cursorId != null && cursorId != 0) {
            post = postRepository.findById(cursorId).orElseThrow(
                    () -> new FlintCustomException(HttpStatus.BAD_REQUEST, INVALID_POST_PAGE_CURSOR));
        }

        return postRepository.findPostsWithBoardByPaging(boardId, post, size, sortStrategy);
    }

    private boolean isExceedMaxImage(int requestSize) {
        return requestSize > MAX_IMAGE_LIMIT;
    }
}
