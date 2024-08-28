package lltw.scopyright;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sakura
 */
@SpringBootApplication
@MapperScan("lltw.scopyright.mapper")
public class ScopyrightApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScopyrightApplication.class, args);
    }

}
