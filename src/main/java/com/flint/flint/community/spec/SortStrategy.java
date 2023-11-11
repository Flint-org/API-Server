package com.flint.flint.community.spec;

public enum SortStrategy {
    CHRONOLOGICAL,
    POPULAR;

    public static SortStrategy findMatchedEnum(String s){
        for(SortStrategy strategy: SortStrategy.values()){
            if(strategy.name().equals(s)){
                return strategy;
            }
        }
        return null;
    }
}
