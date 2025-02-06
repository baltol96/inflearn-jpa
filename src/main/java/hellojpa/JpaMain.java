package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            /*
            1차 캐시
            - 데이터 조회 시 영속 컨텍스트 안의 1차 캐시를 데이터베이스보다 먼저 조회
            - 한 트랜젝션이 끝나면 영속 컨텍스트가 지워지기 때문에 한 트랜젝션 이내에서만 사용 가능
             */
            Member member = new Member();
            member.setId(200L);
            member.setName("member1");

            //1차 캐시에 저장
            em.persist(member);

            //1차 캐시에서 조회
            Member findMember = em.find(Member.class, 200L);
            //영속 컨텍스트에 없으면 데이터베이스에서 조회
            Member findMember2 = em.find(Member.class, 300L);

            System.out.println("===============================");

            /*
            영속 엔티티의 동일성 보장
             */
            Member a = em.find(Member.class, 200L);
            Member b = em.find(Member.class, 200L);
            System.out.println(a == b);

            System.out.println("===============================");

            /*
            트랜잭션을 지원하는 쓰기 지연
            - 쓰기 지연 SQL 저장소에 저장해두었다가 commit()이 발생하면 쿼리 실행
             */
            Member memberA = new Member();
            memberA.setId(200L);
            memberA.setName("memberA");

            Member memberB = new Member();
            memberB.setId(200L);
            memberB.setName("memberB");

            em.persist(memberA);
            em.persist(memberB);
            //여기까지 INSERT SQL을 데이터베이스에 보내지 않는다

            //커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다
            tx.commit();

            System.out.println("===============================");

            /*
            변경 감지(Dirty Checking)
             */
            Member findMemberA = em.find(Member.class, 200L);
            findMemberA.setName("findMemberA");

            //영속 엔티티 수정 후 별도 작업이 없어도 1차 캐시의 스냅샷과 비교하여 변경되었으면 UPDATE SQL 실행
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
