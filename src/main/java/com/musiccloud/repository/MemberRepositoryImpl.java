package com.musiccloud.repository;

import com.musiccloud.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

//    @Transactional
    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Member findByEmail(String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() );
            return null;
        }
    }

    @Override
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
}
