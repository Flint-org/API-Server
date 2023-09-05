package com.flint.flint.member.spec;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 컬럼에 (게임, 영화, 쇼핑) 이렇게 저장, 디비에 저장되어 있는 값을 ENUM으로 뽑는 컨버터
 * @Author 정순원
 * @Since 2023-09-04
 */
@Converter
public class InterestConverter implements AttributeConverter<List<Interest>, String> {

    private static final String DELIMITER = ",";

    /**
     * ENUM -> DB
     */
    @Override
    public String convertToDatabaseColumn(List<Interest> attribute) {

        if (attribute.isEmpty() || attribute == null) {
            return null;
        }

        List<String> InterestList = convertEnumToString(attribute);

        return String.join(DELIMITER, InterestList);
    }

    /**
     * DB -> ENUM
     */
    @Override
    public List<Interest> convertToEntityAttribute(String dbData) {
        if (Strings.isEmpty(dbData)) {
            return new ArrayList<>();
        }

        List<Interest> InterestList = Arrays.stream(dbData.split(DELIMITER))
                .map(name -> convertStringToInterest(name))
                .collect(Collectors.toUnmodifiableList());

        return InterestList;
    }

    private List<String> convertEnumToString(List<Interest> attribute) {
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableList());
    }

    private Interest convertStringToInterest(String name) {
        return Interest.valueOf(name);
    }
}
