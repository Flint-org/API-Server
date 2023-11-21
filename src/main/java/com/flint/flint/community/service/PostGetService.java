package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.response.DetailPostResponse;
import com.flint.flint.community.dto.response.PostResponse;
import com.flint.flint.community.dto.response.WriterResponse;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.idcard.dto.response.IdCardGetResponse;
import com.flint.flint.idcard.service.IdCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-10-19
 */
@Service
@RequiredArgsConstructor
public class PostGetService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final IdCardService idCardService;
    private final BoardService boardService;

    @Transactional(readOnly = true)
    public List<PostResponse> searchInAllBoard(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleOrContentsContaining(keyword, pageable);

        return posts.stream().
                map(post -> new PostResponse(post.getId(), post.getTitle(), getFirstImageUrl(post)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchInSpecificBoard(String boardName, String keyword, Pageable pageable) {
        Board board = boardRepository.findByGeneralBoardName(boardName).orElseThrow(() -> new FlintCustomException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, ResultCode.BOARD_NOT_FOUND));
        Page<Post> posts = postRepository.findByTitleOrContentsContainingAndBoard(board.getId(), keyword, pageable);

        return posts.stream().
                map(post -> new PostResponse(post.getId(), post.getTitle(), getFirstImageUrl(post)))
                .toList();
    }

    // Post에서 첫 번째 이미지 URL을 가져오는 메서드
    private String getFirstImageUrl(Post post) {
        if (post.getPostImages() != null && !post.getPostImages().isEmpty()) {
            return post.getPostImages().get(0).getImgUrl();
        } else {
            return ""; //TODO 기본 이미지 URL을 반환
        }
    }

    @Transactional(readOnly = true)
    public DetailPostResponse getDetailPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));

        IdCardGetResponse.GetIdCard writerIdCard = idCardService.getMyIdCardByMemberId(post.getMember().getId());

        return DetailPostResponse.builder()
                .postId(postId)
                .boardName(boardService.getFormattedBoardName(post.getBoard()))
                .title(post.getTitle())
                .contents(post.getContents())
                .createAt(post.getCreatedAt())
                .commentCount(post.getPostComments().size())
                .likeCount(post.getPostLikes().size())
                .scrapCount(post.getPostScraps().size())
                .images(post.convertImagesToString())
                .writer(WriterResponse.builder()
                        .memberId(post.getMember().getId())
                        .major(writerIdCard.getMajor())
                        .universityName(writerIdCard.getUniversity())
                        .universityLogoUrl(writerIdCard.getUnivInfo().getLogoUrl())
                        .build())
                .build();
    }
}
