package hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import hw.aspect.Profiler;
import hw.example.Example;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HWApp {

    private static void logic(ConfigurableApplicationContext a) {
        for (int i = 0; i < 2; i++) {
            a.getBean(Example.class).getCurrentWeather();
        }

        for (int i = 0; i < 10; i++) {
            a.getBean(Example.class).getCurrentTime();
        }
    }


    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(HWApp.class, args);
        logic(applicationContext);

        Profiler.statistics();
    }
}
