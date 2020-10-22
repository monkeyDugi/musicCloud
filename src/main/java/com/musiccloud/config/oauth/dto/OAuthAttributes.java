package com.musiccloud.config.oauth.dto;

import com.musiccloud.domain.Member;
import com.musiccloud.enumerated.Role;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;

    private OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해 주어야 한다
    // registrationId는 구글에서는 사용하지 않는다. 추후 네이버 등이 추가될 경우를 대비해서 만들어 놓은 것 이다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(attributes, userNameAttributeName, (String)attributes.get("name"), (String)attributes.get("email"));
    }

    // OAuth2User에서 반환 받은 값 그대로 Member 객체를 만들기 위해 Member에서 해당 메서드를 생성하지 않고, 여기서 한다.
    public Member toEntity() {
        return new Member(name, email, Role.USER);
    }
}
