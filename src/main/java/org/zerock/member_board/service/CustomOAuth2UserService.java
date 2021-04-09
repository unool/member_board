package org.zerock.member_board.service;
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
import org.zerock.member_board.controller.HomeController;
import org.zerock.member_board.dto.OAuthAttributes;
import org.zerock.member_board.dto.SessionUser;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.repository.MemberRepository;
import org.zerock.member_board.repository.UserRepository;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();


        // oauth2 로그인 진행 시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuthAttributes: attribute를 담을 클래스 (개발자가 생성)
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        // SessioUser: 세션에 사용자 정보를 저장하기 위한 DTO 클래스 (개발자가 생성)

        httpSession.setAttribute("user", new SessionUser(member));
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getAuth())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
    private Member saveOrUpdate(OAuthAttributes attributes) {

        List<Member> result = memberRepository.findById(attributes.getEmail()).stream()
                .map(entity-> entity.update(attributes.getName(), attributes.getPicture(), attributes.getKind())).collect(Collectors.toList());

        Member member;
        if(result.isEmpty())
        {
            member = attributes.toEntity();
        }
        else
        {
            member = result.get(0);
        }

        return memberRepository.save(member);


//        User 객체 사용
//        List<User> result = userRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName(), attributes.getPicture())).collect(Collectors.toList());
//
//        User user;
//
//        if(result.isEmpty())
//        {
//            user = attributes.toEntity();
//        }
//        else
//        {
//            user = result.get(0);
//        }
//
//        return userRepository.save(user);


    }
}