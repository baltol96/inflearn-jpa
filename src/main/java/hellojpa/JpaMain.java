package hellojpa;

import hellojpa.domain.Book;
import hellojpa.jpql.Member;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //JPQL 리스트 조회
            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            //검색 JPQL
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + result.getUsername());

            //페이징
            for(int i = 0; i < 100; i++){
                Member member2 = new Member();
                member2.setUsername("member"+i);
                member2.setAge(i);
                em.persist(member2);
            }

            em.flush();
            em.clear();

            List<Member> pagingList = em.createQuery("select m from Member order by m.age desc", Member.class)
                            .setFirstResult(1)
                            .setMaxResults(10)
                            .getResultList();

            System.out.println("result.size() = " + pagingList.size());
            for(Member m : pagingList){
                System.out.println("m" + m.getUsername());
            }


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
