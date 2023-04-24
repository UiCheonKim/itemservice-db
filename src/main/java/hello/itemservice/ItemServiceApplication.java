package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Slf4j
//@Import(MemoryConfig.class) // MemoryConfig.class 를 설정 파일로 사용
//@Import(JdbcTemplateV3Config.class)
@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(MyBatisConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
// 컴포넌트 스캔을 hello.itemservice.web 범위로 한정
// 컨트롤러만 컴포넌트 스캔을 사용하고, 나머지는 수동 등록
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// TestDataInit 를 빈으로 등록 해줘야 함 - 스프링에서 발생하는 이벤트이기 때문에 스프링 빈이여야 영향을 줄 수 있음
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

/*	@Bean
	@Profile("test")
	public DataSource dataSource() { // DataSource 를 직접 스프링 빈으로 등록 // 테스트 환경에서는 메모리 DB 사용
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver"); // h2 데이터베이스 드라이버 설정
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"); // 메모리 데이터베이스 설정
		// jdbc:h2:mem:db - 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스 사용할 수 있다.
		// 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끓어지면 데이터베이스도 종료되는데, 그것을 방지하는 설정
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
		// JVM 내에 데이터베이스를 만들어서 사용하고, 프로그램이 종료되면 데이터베이스도 함께 삭제
	}*/

}
