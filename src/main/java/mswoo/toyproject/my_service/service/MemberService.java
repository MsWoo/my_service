package mswoo.toyproject.my_service.service;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.dto.MemberJoinDto;
import mswoo.toyproject.my_service.domain.entity.Member;
import mswoo.toyproject.my_service.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Long joinMember(MemberJoinDto memberJoinDto) {
        Member member = Member.builder()
                .userId(memberJoinDto.getUserId())
                .userName(memberJoinDto.getUserName())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .build();

        Long id = memberRepository.save(member).getId();

        return id;
    }

}
