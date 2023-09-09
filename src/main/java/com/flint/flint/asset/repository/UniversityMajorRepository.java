package com.flint.flint.asset.repository;

import com.flint.flint.asset.domain.UniversityMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UniversityMajorRepository extends JpaRepository<UniversityMajor, Long> {
    @Query("SELECT DISTINCT u.largeClass FROM UniversityMajor u WHERE u.universityName = :univName ORDER BY u.largeClass")
    Optional<List<String>> findDistinctLargeClassesByUniversityName(@Param("univName") String univName);

    Optional<List<UniversityMajor>> findUniversityMajorsByUniversityNameAndLargeClassOrderByMajorName(String univName, String largeClass);
}
