package hello.itemservice.domain;

import lombok.Data;

import javax.annotation.processing.Generated;
import javax.persistence.*;

@Data
@Entity
// @Entity - 테이블과의 매핑 정보, jpa 에서 관리하는구나, jpa 객체로 인정 됨
// @Table(name = "item") // 객체명과 같으면 생략 가능
public class Item {

    @Id // 얘가 PK 라는걸 알려줘야 함
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // id는 PK 인데 값이 generate 되는데 IDENTITY 전략은 db 에서 값을 넣어 주는 전략
    // PK 생성값을 데이터베이스에서 생성하는 IDENTITY 방식을 사용한다. ex) MYSQL auto increment
    private Long id;

    @Column(name = "item_name", length = 10) // itemName 은 db item_name 과 매핑이 된다
    // 언더 스코어로 자동 변환 해주기 때문에 생략해도 된다
    private String itemName;
    private Integer price;
    private Integer quantity;

    // JPA 는 public 또는 protected 의 기본 생성자가 필수.
    // 기본생성자 꼭 넣어주자
    // spec 이구나 지켜야 되는구나 정도만 이해하고 넘어가면 됨
    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
