package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

// MyBatis 매핑 xml 을 호출해주는 Mapper 인터페이스
@Mapper
public interface ItemMapper {
    // 인터페이스에서 뒤 xml 파일을 부른다.
    // 인터페이스의 메서드를 호출하면 xml 에 해당하는 SQL 을 실행하고 결과를 돌려준다.

    void save(Item item);

    // 파라미터가 하나인 경우에는 @Param 안넣어줘도 되는데 2개 이상이면 넣어준다.
    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);
}
