package org.telegram.guessnum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.guessnum.bot.Postman;
import org.telegram.guessnum.bot.UpdatesReceiver;
import org.telegram.guessnum.bot.client.Client;
import org.telegram.guessnum.bot.client.SyncClient;
import org.telegram.guessnum.bot.model.Message;
import org.telegram.guessnum.bot.request.Request;
import org.telegram.guessnum.game.Game;
import org.telegram.guessnum.game.GameContext;
import org.telegram.guessnum.game.GameWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
public class BotConfig {

    @Value("${bot.access_token}")
    String accessToken;

    @Value("${bot.api_host}")
    String apiHost;

    @Value("${bot.worker_count}")
    int workerCount;

    @Value("${bot.request_senders_count}")
    int requestSendersCount;

    @Value("${bot.requests_queue}")
    int requestsQueue;

    @Value("${bot.messages_queue}")
    int messagesQueue;

    @Value("${bot.long_polling_timeout}")
    int longPollingTimeout;


    @Bean
    public BlockingQueue<Request> getOutRequestsQueue(){
        return new ArrayBlockingQueue<>(requestsQueue);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Client getClient(){
        return new SyncClient(apiHost, accessToken, longPollingTimeout + 20);
    }

    @Bean
    public BlockingQueue<Message> getInMessagesQueue(){
        return new ArrayBlockingQueue<>(messagesQueue);
    }

    @Bean
    public List<Postman> getPostmen(){
        List<Postman> postmen = new ArrayList<>();
        for (int i = 0; i < requestSendersCount; i++) {
            postmen.add(new Postman(getOutRequestsQueue(), getClient()));
        }
        return postmen;
    }

    @Bean
    public List<GameWorker> getWorkers(){
        List<GameWorker> workers = new ArrayList<>();
        for (int i = 0; i < workerCount; i++) {
            workers.add(new GameWorker(getInMessagesQueue(), getOutRequestsQueue(), getGameContext()));
        }
        return workers;
    }

    @Bean
    public GameContext getGameContext(){
        return new GameContext();
    }

    @Bean
    public UpdatesReceiver getUpdatesReceiver(){
        return new UpdatesReceiver(getInMessagesQueue(), getClient(), longPollingTimeout);
    }

    @Bean
    public Game getGame(){
        return new Game(getGameContext() ,getClient(), getWorkers(), getPostmen(), getUpdatesReceiver());
    }

}
