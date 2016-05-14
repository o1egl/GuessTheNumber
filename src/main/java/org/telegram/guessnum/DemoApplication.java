package org.telegram.guessnum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.guessnum.game.Game;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        context.getBean(Game.class).start();
    }
}
