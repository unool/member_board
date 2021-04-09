package org.zerock.member_board.dto;
import lombok.Builder;
import lombok.Getter;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.role.Kind;
import org.zerock.member_board.entity.role.Role;
import java.util.Map;


@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey, name, email, picture, kind;
    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String email, String picture, String kind) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.kind = kind;
    }
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }


    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .kind(Kind.NAVER.getKey())
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .kind(Kind.GOOGLE.getKey())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
//    public User toEntity() {
//        return User.builder()
//                .name(name)
//                .email(email)
//                .picture(picture)
//                .role(Role.GUEST)
//                .build();
//    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .auth(Role.USER.getKey())
                .kind(kind)
                .build();
    }
}