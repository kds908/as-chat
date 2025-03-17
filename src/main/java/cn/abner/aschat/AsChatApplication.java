package cn.abner.aschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AsChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsChatApplication.class, args);
    }

}
