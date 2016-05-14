package org.telegram.guessnum.game;


import org.telegram.guessnum.bot.model.User;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameContext {

    private final Map<Long, GameRoom> gameStore;
    private final Random random;
    private volatile long botId;


    public GameContext(){
        gameStore = new ConcurrentHashMap<>();
        random = new Random();
    }

    public synchronized GameRoom getOrCreateRoom(long roomId){
        GameRoom gameRoom = gameStore.get(roomId);
        if(gameRoom == null){
            gameRoom = new GameRoom(GameState.IDLE, null, 0);
            gameStore.put(roomId, gameRoom);
        }
        return gameRoom;
    }

    public synchronized void setBotId(long id){
        botId = id;
    }

    public boolean isMe(long id){
        return id == botId;
    }

    public synchronized void setWinner(long roomId, User winner){
        GameRoom gameRoom = gameStore.get(roomId);
        gameRoom.setWinner(winner);
        gameRoom.setGameState(GameState.FINISHED);
        gameStore.put(roomId, gameRoom);
    }

    public synchronized void resetWinner(long roomId){
        GameRoom gameRoom = gameStore.get(roomId);
        gameRoom.setWinner(null);
        gameStore.put(roomId, gameRoom);
    }

    public synchronized void startNewGame(long roomId, int num){
        GameRoom gameRoom = gameStore.get(roomId);
        gameRoom.setGameState(GameState.IN_PROGRESS);
        gameRoom.setWinner(null);
        gameRoom.setNumber(randInt(num));
    }

    private int randInt(int max){
        return random.nextInt(max);
    }

}
