

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
        .driverClassName("com.mysql.cj.jdbc.Driver")
        .url(System.getenv("SPRING_DB_URL"))
        .username("Giorgos")
        .password("1234")
        .build();
    }
}
