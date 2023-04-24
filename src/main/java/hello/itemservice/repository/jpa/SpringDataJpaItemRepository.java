package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// extends JpaRepository<Item, Long> - Item 엔티티를 관리하는 레포지토리, Long 은 엔티티의 식별자 타입(PK)
// 기본적인 CRUD 메서드는 JpaRepository 가 제공해줌
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNameLike(String itemName); // 이름 검색 // findByItemNameLike - ItemName 이 itemName 을 포함하는 것 검색
    List<Item> findByPriceLessThanEqual(Integer price); // 가격 검색 // findByPriceLessThanEqual - Price 가 price 보다 작거나 같은것 검색

    // 쿼리 메서드 (아래 메서드와 같은 기능 수행)
//    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price); // 이렇게 하지 말것

    // 쿼리 직접 실행
    @Query("select i from Item i  where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
    // @Param - 파라미터 바인딩 꼭 해줘야 함 // data.repository.query.Param 사용 O, ibatis 에서 사용하는 @Param 사용 X
}
