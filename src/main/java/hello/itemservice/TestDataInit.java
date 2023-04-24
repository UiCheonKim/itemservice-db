package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가 - 스프링 뜰 때 자동으로 호출
     */
    @EventListener(ApplicationReadyEvent.class)
    // @EventListener - 스프링이 어떤 타이밍이 되었을 때 이벤트를 발생하여 호출
    // ApplicationReadyEvent.class - 애플리케이션 준비 완료
    // @EventListener(ApplicationReadyEvent.class) - 스프링 컨테이너가 완전히 초기화를 다 끝내고, 실행 준비가 되었을 때 발생하는 이벤트
    // 완전 초기화 후 실행 되기 때문에 타이밍과 관련된 문제 발생 X
    // @PostConstruct 를 쓸 수 있지만 AOP 같은 부분이 다 처리 되지 않은 시점에서 호출되는 문제가 발생할 수 있음
    // ex) @Transactional 과 관련된 AOP 가 적용되지 않은 상태로 호출 될 수 있음
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
