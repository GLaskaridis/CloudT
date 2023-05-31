

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    public DataSource getDataSource(){
        System.out.println(System.getenv("SPRING_DB_URL"));
        return DataSourceBuilder.create()
        .driverClassName("com.mysql.cj.jdbc.Driver")
        .url("jdbc:mysql://db:3306/dvddb?autoReconnect=true&useSSL=false")
        .username("Giorgos")
        .password("1234")
        .build();
    }
}
