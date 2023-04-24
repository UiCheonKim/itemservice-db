package hello.itemservice.repository;

import hello.itemservice.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    // 이 itemId에 대한걸 이렇게(ItemUpdateDto) 업데이트 해라
    // 이 itemId에 변경할 데이터는 ItemUpdateDto - ItemUpdateDto 에 Id 합쳐도 무관함
    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id); // 한건 조회

    List<Item> findAll(ItemSearchCond cond); // 전체 조회 - 검색조건이 넘어감(상품명, 가격제한 검색조건)

}
