package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;
    // service 에서 주입받지 않고 여기서 주입받음

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = repository.findById(itemId).orElseThrow();
        // Optional 이기 때문에 orElseThrow() 를 사용해서 예외처리를 해줘야 한다.
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // Optional 사용이유
    // null 을 반환하는 것보다 Optional 을 반환하는 것이 더 명확하다.
    // Optional 은 null 을 감싸는 Wrapper 클래스
    // Optional 은 null 을 직접 반환하지 않고 Optional.empty() 를 반환한다.
    // findById 는 조회가 될 수도 있고 안될수도 있으니까 Optional 로 감싸서 반환한다.
    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    // 실무에선 동적쿼리를 쓰지 이렇게 따로 만들지는 않는다.
    // 실무에선 예를들어 repository.findItems(itemName, maxPrice); 걸 만들고 그거에 대한 구현체 만들고 거기서 querydsl 로 동적쿼리 해서 끝
    // 조건이 2개밖에 없을 경우는 가끔쓴다.
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // StringUtils - 스프링에서 제공하는 유틸리티 클래스로 null 이나 공백을 체크해주는 기능 등 제공
        if (StringUtils.hasText(itemName) && maxPrice != null){ // itemName, maxPrice 둘 다 값이 있을 때
//            return repository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice); // 너무 길다
            return repository.findItems("%" + itemName + "%", maxPrice);
        } else if (StringUtils.hasText(itemName)) { // itemName 값만 있을 때
            return repository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) { // maxPrice 값만 있을 때
            return repository.findByPriceLessThanEqual(maxPrice);
        } else { // itemName, maxPrice 둘 다 값이 없을 때
            return repository.findAll();
        }
    }
}
