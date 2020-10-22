package com.musiccloud.config.oauth.dto;

import com.musiccloud.domain.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {

    private final String name;
    private final String email;

    public SessionMember(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
