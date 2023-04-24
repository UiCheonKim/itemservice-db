package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
//@Repository
@Transactional
// Jpa 의 모든 데이터 변경은 트랜잭션 안에서 이뤄진다
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;
    // jpa 에서는 의존관계 주입을 하나 받아야 한다
    // 여기다 대고 저장하고 조회하고 하게 됨
    // EntityManager 는 스프링에서 자동으로 만들어 줌 -> 그냥 주입받아서 쓰면 된다.

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId); // Item.class 에서 itemId 를 찾음
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        // update 저장 코드.. 없다!
        // JPA 가 내부에 조회 스냅샷을 만들어 놓고 변경 감지(트랜잭션이 커밋되는 시점)를 통해 update 쿼리를 날린다.
    }

    // 하나를 조회 할 때는 식별자(PK) 기반으로 사용하면 됨
    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id); // type(Item.class), pk(id)
        return Optional.ofNullable(item); // 혹시 null 일 수 있으니 ofNullable
    }

    // 여러가지 조건으로 복잡한 쿼리를 사용해야 할 때 JPQL 를 사용
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        //  jqpl 객체 쿼리 언어라고 함
        String jpql = "select  i from Item i"; // i 는 Item Entity 자체를 말함

        // jpql 을 넣어주고 반환타입은 class 야 선언
/*
        em.createQuery(jpql, Item.class)
                .getResultList();
        return result;
*/

        // JPQL 이 근데 동적 쿼리에 약함
        // 동적 쿼리 Code 시작
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')";
            param.add(itemName);
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
            param.add(maxPrice);
        }

        log.info("jpql={}", jpql);
        // jpql 을 넣어주고 반환타입은 class 야 선언
        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList();
    }
}
