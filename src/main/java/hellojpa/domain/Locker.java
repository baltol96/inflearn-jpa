package hellojpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Locker extends BaseEntity {

    @Id @GeneratedValue
    private int id;

    private String name;

    @OneToOne(mappedBy = "locker")
    private Member member;
}
