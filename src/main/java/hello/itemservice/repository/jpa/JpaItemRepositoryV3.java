package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.item;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory; // JPA 쓰려면 JPAQueryFactory 를 주입받아야 한다.
    // 스프링 빈으로 주입받아 사용해도 된다.

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        // JPAQueryFactory 는 querydsl 꺼
        // querydsl 은 결과적으로 jpa 의 jpql 을 만들어주는 빌더 역할 공장 역할(Factory)
    }

    @Override
    public Item save(Item item) {
        em.persist(item); // 그냥 JPA 쓴것
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

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id); // type(Item.class), pk(id)
        return Optional.ofNullable(item); // 혹시 null 일 수 있으니 ofNullable
    }

    public List<Item> findAllOld(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        QItem item = new QItem("i"); // QItem 은 Item 을 대상으로 쿼리를 만들어 준다.
        QItem item = QItem.item; // static import 를 통해 QItem.item 으로 사용 가능
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            booleanBuilder.and(item.itemName.like("%" + itemName + "%"));
        }

        if (maxPrice != null) {
            booleanBuilder.and(item.price.loe(maxPrice)); // loe - 작거나 같다
        }

//        List<Item> result = queryFactory
//                .select(item)
//                .from(item)
//                .where(booleanBuilder)
//                .fetch();

        return queryFactory
                .select(item)
                .from(item)
                .where(booleanBuilder)
                .fetch();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

         return queryFactory
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private Predicate maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice); // loe - 작거나 같다
        }
        return null;
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }
}
