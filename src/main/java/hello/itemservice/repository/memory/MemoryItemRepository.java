package hello.itemservice.repository.memory;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        // 값이 없을수 있기 때문에 Optional 설정
        // Optional - java8 기본 문법 - 값이 있을수도 있고 없을수도 있다
        // 통 안에 값이 있을 수도 있고 없을 수도 있다 통안에 item 있고 그걸 감싸안은것
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        // store 에 저장되어 있는걸 stream 으로 다 찾아본다
        // 모든 Item 객체를 stream 으로 변환하고, filter() 메서드를 이용하여 조건에 맞는 객체들만 걸러냄
        return store.values().stream()
                // 첫번째 filter() 메서드에서는 itemName 값이 비어있는 경우 모든 Item 객체를 반환하고,
                // 그렇지 않은 경우 itemName 을 포함하는 객체들만 반환
                .filter(item -> {
                    if (ObjectUtils.isEmpty(itemName)) {
                        // itemName 을 안 쓸경우 비어있는거니깐 무조건 true - 무조건 가져옴
                        return true;
                    }
                    return item.getItemName().contains(itemName);
                // 두 번째 filter() 메서드에서는 maxPrice 값이 null 인 경우 모든 Item 객체를 반환하고,
                // 그렇지 않은 경우 가격이 maxPrice 이하인 객체들만 반환
                }).filter(item -> {
                    if (maxPrice == null) {
                        // 데이터 가져옴
                        return true;
                    }
                    return item.getPrice() <= maxPrice;
                })
                // 이렇게 걸러진 Item 객체들은 collect() 메서드를 이용하여 List 로 반환
                .collect(Collectors.toList());
    }

    public void clearStore() {
        // 메모리에 저장된 Item 모두 삭제해서 초기화 한다 - 테스트 용도
        store.clear();
    }

}
