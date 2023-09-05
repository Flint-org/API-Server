package com.flint.flint.member.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-09-04
 */
@Getter
@AllArgsConstructor
public enum Interest {

    GAME("게임"), MOVIE("영화"), DRAMA("드라마"), EXHIBITION("전시회"), ANIMETION("애니메이션"),
    CONCERT("콘서트"), PLAY("연극"), MUSICAL("뮤지컬"), WEBTOON("웹툰"), //엔터테인먼트 및 문화예술


    RUNNING("런닝"), HEALTH("헬스"), YOGA("요가"), PILATES("필라테스"),
    SPORTS("스포츠"), WALK("산책"), DRIVE("드라이브"), //운동 및 피트니스


    READING("독서"), DRAWING("그림그리기"), WRITING("글쓰기"), DANCE("댄스"), MUSICALINSTRUMENTS("악기 연주"),
    LISTENINGSONG("노래 감상"), SINGING("노래 부르기"), COOKING("요리"), PHOTOGRAPHY("사진 찍기"), //취미 및 창작


    SHOPPING("쇼핑"), FASHION("패션"), BEAUTY("뷰티"), //패션 및 뷰티


    TRAVEL("여행"), SKIING("스키"), FINDINGFOOD("맛집 탐방"), BBARGE("빠지"), SNORKELING("스노콜링"), //여행 및 레저


    DRINKING("술"), CAFE("카페"), FANLING("덕질"), SELFDEVELOPMENT("자기개발"),
    ANIMAL("동물"), SOCIALISSUES("사회이슈"), SERVICE("봉사)"); //라이프스타일

    private String name;
}
