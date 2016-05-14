package org.telegram.guessnum.bot;


import org.apache.log4j.Logger;
import org.telegram.guessnum.bot.client.Client;
import org.telegram.guessnum.bot.model.Message;
import org.telegram.guessnum.bot.request.UpdatesRequest;
import org.telegram.guessnum.bot.response.UpdatesResponse;
import org.telegram.guessnum.bot.response.UpdatesResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UpdatesReceiver extends Thread {

    private long lastUpdateID;
    private Client client;
    private int longPollingTimeout;
    static Logger log = Logger.getLogger(UpdatesReceiver.class.getName());
    private BlockingQueue<Message> messageQueue;

    public UpdatesReceiver(BlockingQueue<Message> messageQueue, Client client, int longPollingTimeout) {
        this.client = client;
        this.longPollingTimeout = longPollingTimeout;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!interrupted()){
            try {
                UpdatesResponse response = client.execute(new UpdatesRequest(lastUpdateID + 1, longPollingTimeout), UpdatesResponse.class);
                if(response == null || !response.isOk()){
                    log.error("Error in updates response: " + response);
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                for(UpdatesResult updatesResult:response.getResult()){
                    if(updatesResult.getUpdateId() > lastUpdateID){
                        lastUpdateID = updatesResult.getUpdateId();
                    }
                    messageQueue.put(updatesResult.getMessage());
                }

            } catch (Exception e){
                log.error(null, e);
            }
        }
    }

}
