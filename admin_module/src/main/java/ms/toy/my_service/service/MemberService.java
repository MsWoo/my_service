package ms.toy.my_service.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.MemberEditDto;
import ms.toy.my_service.domain.dto.MemberJoinDto;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.domain.dto.MemberDto;
import ms.toy.my_service.domain.dto.MemberInfo;
import ms.toy.my_service.domain.entity.Member;
import ms.toy.my_service.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public List<MemberDto> searchMember() {
        return memberRepository.findAll().stream()
                .map(MemberDto::toDto)
                .collect(Collectors.toList());
    }

    public MemberDto getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return MemberDto.toDto(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto joinMember(MemberJoinDto memberJoinDto) {
        if (memberRepository.existsByUserId(memberJoinDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_ID.name());
        }

        // todo [gotoend] mapper로 전환 필요
        // 필드가 추가될때마다 변경해줘야한다. mapper로 한방에 변환
        Member member = Member.builder()
                .authorityId(1L)
                .userId(memberJoinDto.getUserId())
                .userName(memberJoinDto.getUserName())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .createdBy(memberJoinDto.getUserId())
                .build();

        Long id = memberRepository.save(member).getId();

        return MemberDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto deleteMember(Long id) {
        memberRepository.deleteById(id);
        return MemberDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto editMember(Long id, MemberEditDto memberEditDto, MemberInfo memberInfo) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        // todo [gotoend] mapper로 전환 필요
        member.update(memberEditDto, memberInfo.getUserId());

        return MemberDto.builder().id(id).build();
    }
}
