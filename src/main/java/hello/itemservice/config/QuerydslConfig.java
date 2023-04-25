package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;


@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

    private final EntityManager em;

    @Bean
    public ItemService itemService() {
        // ItemService 는 ItemRepository 에 의존하기 때문에
        // ItemService 에서 SpringDataJpaItemRepository 를 그대로 사용할 수 없다.
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        // ItemRepository 에 대한 의존을 유지하면서 DI 를 통해 구현기술 변경
        return new JpaItemRepositoryV3(em);
    }

}