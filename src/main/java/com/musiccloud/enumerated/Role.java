package com.musiccloud.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : 이병덕
 * @description : 로그인 권한으로 추후 권한 추가 예정이므로 생성
 * @date : 2020.10.19
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
