package com.example.yiseul.service;

import com.example.yiseul.converter.MemberConverter;
import com.example.yiseul.domain.Member;
import com.example.yiseul.dto.member.*;
import com.example.yiseul.global.exception.BaseException;
import com.example.yiseul.global.exception.ErrorCode;
import com.example.yiseul.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto createMember(MemberCreateRequestDto createRequestDto) {
        Member member = MemberConverter.convertMember(createRequestDto);
        Member savedMember = memberRepository.save(member);

        return MemberConverter.convertMemberResponseDto(savedMember);
    }

    public MemberPageResponseDto getMembers(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);

        return MemberConverter.convertMemberPageResponseDto(page);
    }

    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("MemberService : Member {} is not found",memberId);

                    return new BaseException(ErrorCode.MEMBER_NOT_FOUND);
                });

        return MemberConverter.convertMemberResponseDto(member);
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequestDto updateRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("MemberService : Member {} is not found",memberId);

                    return new BaseException(ErrorCode.MEMBER_NOT_FOUND);
                });

        member.updateInfo(updateRequestDto.name(), updateRequestDto.age(), updateRequestDto.hobby());
    }

    @Transactional
    public void deleteMember(Long memberId) {
        if (isMemberNotExist(memberId)) {
            log.error("MemberService : Member {} is not found",memberId);

            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND);
        }

        memberRepository.deleteById(memberId);
    }

    public MemberCursorResponseDto findMemberByCursor(Long cursorId, int size) {
        List<Member> members = getMembers(cursorId, size);

        if(members.size() == 0) {

            return MemberConverter.convertMemberCursorResponseDto(members,null);
        }

        Long nextCursorId = members.get(members.size() - 1).getId();

        return MemberConverter.convertMemberCursorResponseDto(members,nextCursorId);
    }

    private List<Member> getMembers(Long cursorId, int size){
        if (cursorId == null) {

            return memberRepository.findFirstPage(size);
        }

        if (cursorId < 0) {

            throw new BaseException(ErrorCode.CURSOR_ID_NEGATIVE);
        }

        return memberRepository.findByIdGreaterThanOrderByIdAsc(cursorId,size);
    }

    private boolean isMemberNotExist(Long memberId) {
        return memberRepository.existsById(memberId);
    }

}
