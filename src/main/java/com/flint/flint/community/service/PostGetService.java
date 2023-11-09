package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.response.AllPostGetResponse;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public List<AllPostGetResponse> searchInAllBoard(String keyword) {
        List<Post> postList = postRepository.findByTitleContainingOrContentsContainingOrderByCreatedAtDesc(keyword);

        return postList.stream()
                .map(post -> new AllPostGetResponse(post.getId(), post.getTitle(), getFirstImageUrl(post)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AllPostGetResponse> searchInSpecificBoard(String boardName, String keyword) {
        Board board = boardRepository.findByGeneralBoardName(boardName).orElseThrow(() -> new FlintCustomException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, ResultCode.BOARD_NOT_FOUND));
        List<Post> postList = postRepository.findByTitleContainingOrContentsContainingAndBoardOrderByCreatedAtDesc(board.getId(), keyword);

        return postList.stream()
                .map(post -> new AllPostGetResponse(post.getId(), post.getTitle(), getFirstImageUrl(post)))
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
}
