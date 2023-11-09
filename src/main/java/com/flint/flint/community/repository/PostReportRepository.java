package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostReport;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByMemberAndPost(Member member, Post post);
}
