package com.spring.board.springboard.user.service;

import com.spring.board.springboard.user.domain.Hobby;
import com.spring.board.springboard.user.domain.Member;
import com.spring.board.springboard.user.domain.dto.MemberDetailResponseDto;
import com.spring.board.springboard.user.domain.dto.MemberLoginDto;
import com.spring.board.springboard.user.domain.dto.MemberRequestDto;
import com.spring.board.springboard.user.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("사용자는 회원으로 가입할 수 있다.")
    void test_register() {
        // given
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);
        String email = "user@naver.com";
        MemberRequestDto request = new MemberRequestDto(
                email, "password1234", "아무개", 22, Hobby.SLEEP);
        Member member = new Member(
                request.email(),
                request.password(),
                request.name(),
                request.age(),
                request.hobby()
        );

        given(memberRepository.save(any()))
                .willReturn(member);


        memberService.register(request);

        // then
        verify(memberRepository).save(
                memberArgumentCaptor.capture());
    }

    @Test
    @DisplayName("회원은 로그인을 할 수 있다.")
    void test_login() {
        // given
        String email = "user@naver.com";
        String password = "password1234";
        MemberLoginDto memberLoginDto = new MemberLoginDto(email, password);
        Member member = new Member(email, password, "아무개", 22, Hobby.SLEEP);

        given(memberRepository.findByEmail(any()))
                .willReturn(
                        Optional.of(member));

        MemberDetailResponseDto findMember = memberService.login(memberLoginDto);

        assertThat(member.getId())
                .isEqualTo(
                        findMember.memberId());
        assertThat(member.getName())
                .isEqualTo(
                        findMember.name());
        assertThat(member.getAge())
                .isEqualTo(
                        findMember.age());
        assertThat(member.getHobby())
                .isEqualTo(
                        findMember.hobby());
        assertThat(member.getEmail())
                .isEqualTo(
                        findMember.email());
    }

    @Test
    @DisplayName("인증된 회원은 자신의 정보를 찾아올 수 있다.")
    void test_findById() {
        String email = "user@naver.com";
        String password = "password1234";
        Member member = new Member(email, password, "아무개", 22, Hobby.SLEEP);

        given(memberRepository.findById(any()))
                .willReturn(
                        Optional.of(member));

        MemberDetailResponseDto findMemberDto = memberService.findById(
                member.getId());

        assertThat(findMemberDto.email())
                .isEqualTo(email);
    }

}