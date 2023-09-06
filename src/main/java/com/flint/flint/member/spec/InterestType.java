package com.flint.flint.member.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-09-04
 */
@Getter
@AllArgsConstructor
public enum InterestType {

    ENTERTAINMENTCULTUREandARTS("엔터테인먼트 및 문화예술", null),
        GAME("게임", ENTERTAINMENTCULTUREandARTS), MOVIE("영화", ENTERTAINMENTCULTUREandARTS), DRAMA("드라마", ENTERTAINMENTCULTUREandARTS),
        EXHIBITION("전시회", ENTERTAINMENTCULTUREandARTS), ANIMETION("애니메이션", ENTERTAINMENTCULTUREandARTS),
        CONCERT("콘서트", ENTERTAINMENTCULTUREandARTS), PLAY("연극", ENTERTAINMENTCULTUREandARTS),
        MUSICAL("뮤지컬", ENTERTAINMENTCULTUREandARTS), WEBTOON("웹툰", ENTERTAINMENTCULTUREandARTS), //엔터테인먼트 및 문화예술

    EXERCISEANDFITNESS("운동 및 피트니스", null),
        RUNNING("런닝", EXERCISEANDFITNESS), HEALTH("헬스", EXERCISEANDFITNESS),
        YOGA("요가", EXERCISEANDFITNESS), PILATES("필라테스", EXERCISEANDFITNESS),
        SPORTS("스포츠", EXERCISEANDFITNESS), WALK("산책", EXERCISEANDFITNESS), DRIVE("드라이브", EXERCISEANDFITNESS), //운동 및 피트니스


    HOBBYandCREATION("취미 및 창작", null),
        READING("독서", HOBBYandCREATION), DRAWING("그림그리기", HOBBYandCREATION), WRITING("글쓰기", HOBBYandCREATION),
        DANCE("댄스", HOBBYandCREATION), MUSICALINSTRUMENTS("악기 연주", HOBBYandCREATION),
        LISTENINGSONG("노래 감상", HOBBYandCREATION), SINGING("노래 부르기", HOBBYandCREATION),
        COOKING("요리", HOBBYandCREATION), PHOTOGRAPHY("사진 찍기", HOBBYandCREATION), //취미 및 창작


    FASHIONandBEAUTY("패션 및 뷰티", null),
        SHOPPING("쇼핑", FASHIONandBEAUTY), FASHION("패션", FASHIONandBEAUTY), BEAUTY("뷰티", FASHIONandBEAUTY), //패션 및 뷰티


    TRAVELandLEISURE("여행 및 레저", null),
        TRAVEL("여행", TRAVELandLEISURE), SKIING("스키", TRAVELandLEISURE), FINDINGFOOD("맛집 탐방", TRAVELandLEISURE),
        BBARGE("빠지", TRAVELandLEISURE), SNORKELING("스노콜링", TRAVELandLEISURE), //여행 및 레저


    LIFESTYLE("라이프스타일", null),
        DRINKING("술", LIFESTYLE), CAFE("카페", LIFESTYLE), FANLING("덕질", LIFESTYLE), SELFDEVELOPMENT("자기개발", LIFESTYLE),
        ANIMAL("동물", LIFESTYLE), SOCIALISSUES("사회이슈", LIFESTYLE), SERVICE("봉사",LIFESTYLE); //라이프스타일

    private String name;
    private InterestType upperType;
}
