package com.musiccloud.config.oauth;

import com.musiccloud.config.oauth.dto.OAuthAttributes;
import com.musiccloud.config.oauth.dto.SessionMember;
import com.musiccloud.domain.Member;
import com.musiccloud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    private final EntityManager em;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // 현재는 구글 로그인만 사용하기 때문에 필요는 없다. 하지만 추후 네이버 등 추가될 것을 대비하여 작성
        String registrationId = userRequest.getClientRegistration()
                                           .getRegistrationId();

        // OAuth2 로그인 진생 시 키가 되는 필드값
        // 구글의 경우 기본적으로 코드를 지원하지만 네이버, 카카오 등은 기본 지원하지 않는다.
        // 구글의 기본코드는 'sub'
        // 현재는 구글만 사용하기에 굳이 없어도 됨.
        String userNameAttributeName = userRequest.getClientRegistration()
                                                  .getProviderDetails()
                                                  .getUserInfoEndpoint()
                                                  .getUserNameAttributeName();
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        // 이후 다른 소셜 로그인도 이걸 사용하면 됨
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        // SessionMember에는 인증된 사용자만 필요
        // Member 클래스를 세션에 저장하지 않고, SessionMember dto를 따로 만든이유
        // - 엔티티인 Member를 세션에 저장하려고 하면 직렬화 구현을 하지 않았다는 에러가 발생한다.
        // - 해결을 위해 엔티티에 직렬화를 구현하면 다른 엔티티와의 관계형성 시 문제가 될 수 있다.
        // - @OneToMany 등 관계가 맺어지면 관련 엔티티들까지 직렬화 대상에 포함되므로 성능이슈가 잘 수 있다.
        // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        httpSession.setAttribute("member", new SessionMember(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        member.getRoleKey()
                )),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    // 구글 사용자 정보가 업데이트 되었을 때를 대비하여 update 기능 구현(이름만 반영)
    public Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail());
        if (member == null) {
            Long id = memberRepository.save(attributes.toEntity());
            return memberRepository.findOne(id);
        }
        System.out.println("findByEmail = " + em.contains(member));
        member.change(attributes.getName());
        return member;
    }
}
