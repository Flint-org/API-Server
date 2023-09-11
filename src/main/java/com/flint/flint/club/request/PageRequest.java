package com.flint.flint.club.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.*;

@Data
@NoArgsConstructor
public class PageRequest {
    private String sortProperties; // 최신순, 인기순
    private int page = 1;
    private int size = 5;
    private String direct;
    private Direction direction;

    @Builder
    public PageRequest(String sortProperties,
                       String direct) {
        this.sortProperties = sortProperties;
        this.direct = direct;
        this.direction = getDirection(direct);
    }

    private Direction getDirection(String direct) {
        if(direct.equals("ascending")) {
            this.direction = Direction.ASC;
        }
        else {
            this.direction = Direction.DESC;
        }

        return direction;
    }

    public org.springframework.data.domain.PageRequest of(String sortProperties, Direction direction) {
        return org.springframework.data.domain.PageRequest.of(page-1, size, direction, sortProperties);

    }
}
