package mswoo.toyproject.my_service.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.enums.ErrorCode;
import mswoo.toyproject.my_service.domain.dto.MemberDto;
import mswoo.toyproject.my_service.domain.dto.MemberJoinDto;
import mswoo.toyproject.my_service.domain.entity.Member;
import mswoo.toyproject.my_service.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public List<MemberDto> searchMember() {
        return memberRepository.findAll().stream()
                .map(MemberDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDto joinMember(MemberJoinDto memberJoinDto) {
        if (memberRepository.existsByUserId(memberJoinDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_ID.name());
        }

        Member member = Member.builder()
                .authorityId(1L)
                .userId(memberJoinDto.getUserId())
                .userName(memberJoinDto.getUserName())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .build();

        Long id = memberRepository.save(member).getId();

        return MemberDto.builder().id(id).build();
    }
}
