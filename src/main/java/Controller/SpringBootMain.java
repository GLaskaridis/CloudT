package Controller;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@RestController
//@EnableAutoConfiguration
@SpringBootApplication
public class SpringBootMain {
    
    @RequestMapping("/")
    String hello(){
        return"Welcome to my site";
    }
    @RequestMapping("/info")
    String info(){
        return "Giorgos Laskaridis";
    }
    public static void main(String[] args){
        SpringApplication.run(SpringBootMain.class,args);
    }
   
}
