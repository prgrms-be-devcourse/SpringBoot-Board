package devcourse.board.domain.member;

import devcourse.board.domain.member.model.Member;
import devcourse.board.domain.member.model.MemberJoinRequest;
import devcourse.board.domain.member.model.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void join(MemberJoinRequest joinRequest) {
        if (memberRepository.existsByEmail(joinRequest.email())) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Email ''{0}'' is already in use", joinRequest.email()
            ));
        }

        memberRepository.save(joinRequest.toEntity());
    }

    public MemberResponse getMember(Long memberId) {
        return new MemberResponse(findById(memberId));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Member doesn't exist for memberId={0}", memberId
                )));
    }
}
