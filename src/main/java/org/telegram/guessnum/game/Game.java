package org.telegram.guessnum.game;


import org.apache.log4j.Logger;
import org.telegram.guessnum.bot.Postman;
import org.telegram.guessnum.bot.UpdatesReceiver;
import org.telegram.guessnum.bot.client.Client;
import org.telegram.guessnum.bot.request.GetMeRequest;
import org.telegram.guessnum.bot.response.GetMeResponse;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private GameContext gameContext;
    private Client client;
    private List<GameWorker> workers;
    private List<Postman> postmen;
    private UpdatesReceiver updatesReceiver;
    private List<Thread> gameThreads;

    private static Logger log = Logger.getLogger(Game.class.getName());

    public Game(GameContext gameContext, Client client, List<GameWorker> workers, List<Postman> postmen, UpdatesReceiver updatesReceiver) {
        this.gameContext = gameContext;
        this.client = client;
        this.workers = workers;
        this.postmen = postmen;
        this.updatesReceiver = updatesReceiver;
        gameThreads = new LinkedList<>();
        gameThreads.addAll(workers);
        gameThreads.addAll(postmen);
        gameThreads.add(updatesReceiver);
    }

    public void start(){
        GetMeResponse resp = client.execute(new GetMeRequest(), GetMeResponse.class);
        if(resp == null || !resp.isOk()){
            throw new RuntimeException("Incorrect api response");
        }
        gameContext.setBotId(resp.getResult().getId());

        gameThreads.forEach(Thread::start);
    }
}
