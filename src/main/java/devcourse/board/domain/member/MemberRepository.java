package devcourse.board.domain.member;

import devcourse.board.domain.member.model.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public Optional<Member> findByEmail(String email) {
        try {
            return Optional.of(
                    em.createQuery("select m from Member m where m.email = :email", Member.class)
                            .setParameter("email", email)
                            .getSingleResult()
            );
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            return Optional.of(
                    em.createQuery("select m from Member m " +
                                    "where m.email=:email and m.password=:password", Member.class)
                            .setParameter("email", email)
                            .setParameter("password", password)
                            .getSingleResult()
            );
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
