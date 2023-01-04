package devcourse.board.web.api.v1;

import devcourse.board.domain.member.MemberService;
import devcourse.board.domain.member.model.MemberJoinRequest;
import devcourse.board.domain.member.model.MemberResponse;
import devcourse.board.global.errors.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;

import static devcourse.board.web.authentication.AuthenticationUtil.getLoggedInMemberId;

@RestController
@RequestMapping("/api/v1/members")
public class MemberApiV1 {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MemberService memberService;

    public MemberApiV1(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(
            HttpServletRequest request,
            @PathVariable Long memberId
    ) {
        Long loggedInMemberId = getLoggedInMemberId(request);

        if (!loggedInMemberId.equals(memberId)) {
            log.warn(
                    "An unauthorized member '{}' attempted to access information of member '{}'.",
                    loggedInMemberId,
                    memberId
            );
            throw new ForbiddenException(MessageFormat.format(
                    "Refused to access information of member ''{0}''.", memberId
            ));
        }

        return ResponseEntity.ok()
                .body(memberService.getMember(loggedInMemberId));
    }

    @PostMapping
    public ResponseEntity<Void> join(
            @Valid @RequestBody MemberJoinRequest joinRequest
    ) {
        memberService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
