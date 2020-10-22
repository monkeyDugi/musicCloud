package com.musiccloud.repository;

import com.musiccloud.domain.Member;

public interface MemberRepository {

    Long save(Member member);

    // 소셜 로그인의 반환 값인 email을 통해 이미 생성된 사용자인지 아닌지 판단을 위한 메서드
    Member findByEmail(String email);

    // 테스트 코드 작성을 위한 것
    Member findOne(Long id);
}
