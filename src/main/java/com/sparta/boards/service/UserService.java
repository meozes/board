package com.sparta.boards.service;

import com.sparta.boards.dto.SignupRequestDto;
import com.sparta.boards.domain.User;
import com.sparta.boards.domain.UserRole;
import com.sparta.boards.repository.UserRepository;
import com.sparta.boards.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sparta.boards.security.kakao.KakaoOAuth2;
import com.sparta.boards.security.kakao.KakaoUserInfo;


import javax.swing.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
    }


    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

//        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{3,}$");
        Matcher matcher = pattern.matcher(username);

        if (matcher.find() == false || username.length() < 3) {
            throw new IllegalArgumentException("닉네임은 최소 3자 이상, 알파벳 대소문자, 숫자로 구성해주세요");
       }

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        Optional<User> founded = userRepository.findByEmail(email);
        if (founded.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 Email이 존재합니다.");
        }

        String pw = requestDto.getPassword();
        if(pw.contains(username) || pw.length()<4){
            throw new IllegalArgumentException("비밀번호는 최소 4자 이상, 닉네임과 같은 값이 포함되지 않게 구성해주세요");
        }

        String pwCheck = requestDto.getPwCheck();
        boolean check = Pattern.matches(pw, pwCheck);
        if (check == false){
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }



        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
//        String email = requestDto.getEmail();
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
            // 카카오 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 카카오 이메일과 동일한 이메일 회원이 있는 경우
                // 카카오 Id 를 회원정보에 저장
                kakaoUser.setKakaoId(kakaoId);
                userRepository.save(kakaoUser);

            } else {
                // 카카오 정보로 회원가입
                // username = 카카오 nickname
                String username = nickname;
                // password = 카카오 Id + ADMIN TOKEN
                String password = kakaoId + ADMIN_TOKEN;
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                UserRole role = UserRole.USER;

                kakaoUser = new User(username, encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }
        }

        // 강제 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

