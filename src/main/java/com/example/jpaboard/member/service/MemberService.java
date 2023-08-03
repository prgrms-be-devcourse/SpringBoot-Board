package com.example.jpaboard.member.service;

import com.example.jpaboard.member.domain.Member;
import com.example.jpaboard.member.service.dto.CreateMemberRequest;
import com.example.jpaboard.member.service.dto.CreateMemberResponse;
import com.example.jpaboard.member.service.dto.FindMemberResponse;
import com.example.jpaboard.member.service.mapper.MemberMapper;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    /*

    CreateMemberResponse createMember(CreateMemberRequest createMemberRequest) {
        Member member = memberMapper.to(createMemberRequest);

        return new CreateMemberResponse(memberRepository.save(member));
    }

     */

    FindMemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않은 고객입니다."));

        return new FindMemberResponse(member);
    }

}
