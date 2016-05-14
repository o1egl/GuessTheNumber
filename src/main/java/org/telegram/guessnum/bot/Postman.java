package org.telegram.guessnum.bot;


import org.apache.log4j.Logger;
import org.telegram.guessnum.bot.client.Client;
import org.telegram.guessnum.bot.request.Request;
import org.telegram.guessnum.bot.response.Response;

import java.util.concurrent.BlockingQueue;

public class Postman extends Thread {

    private static Logger log = Logger.getLogger(Postman.class.getName());

    private BlockingQueue<Request> requests;
    private Client client;

    public Postman(BlockingQueue<Request> requests, Client client) {
        this.requests = requests;
        this.client = client;
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            try{
                Request request = requests.take();
                Response req = client.execute(request, Response.class);
                log.info(req.toString());
            }
            catch (Throwable e){
                e.printStackTrace();
            }
        }
    }
}
