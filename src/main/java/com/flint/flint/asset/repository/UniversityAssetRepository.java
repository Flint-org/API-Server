package com.flint.flint.asset.repository;

import com.flint.flint.asset.domain.UniversityAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityAssetRepository extends JpaRepository<UniversityAsset, Long> {
    Optional<UniversityAsset> findUniversityAssetByUniversityName(String universityName);
}
