package com.flint.flint.club.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubCommentCreateRequest {
    private String contents;

    @Builder
    public ClubCommentCreateRequest(String contents) {
        this.contents = contents;
    }

}
