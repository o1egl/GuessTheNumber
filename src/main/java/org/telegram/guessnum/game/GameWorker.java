package org.telegram.guessnum.game;


import org.apache.log4j.Logger;
import org.telegram.guessnum.bot.model.Message;
import org.telegram.guessnum.bot.request.Request;
import org.telegram.guessnum.bot.request.SendMessageRequest;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameWorker extends Thread {

    private BlockingQueue<Message> messageQueue;
    private BlockingQueue<Request> requestQueue;
    private GameContext gameContext;
    private Pattern pattern;

    private static Logger log = Logger.getLogger(GameWorker.class.getName());

    private final static String HELP_MESSAGE = "Hi. This is a \"Guess My Number\" bot\n"
            + "To start the game use /numbers_game_start [number] command.\n"
            + "It generates random number from 0 to [number].\n"
            + "To try to guess the number use /numbers_game_guess [number].\n"
            + "To restart existing game use /numbers_game_restart [number].\n"
            + "Only winner can restart the game.";

    public GameWorker(BlockingQueue<Message> messageQueue, BlockingQueue<Request> requestQueue, GameContext gameContext) {
        this.messageQueue = messageQueue;
        this.requestQueue = requestQueue;
        this.gameContext = gameContext;
        pattern = Pattern.compile("^[^/]*/([\\w]+)[@\\w]*[\\s]*([\\d]+)?.*");
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                Message message = messageQueue.take();
                if(message.getText() != null){
                    Matcher matcher = pattern.matcher(message.getText());
                    if(matcher.matches()){
                        String command = matcher.group(1);
                        int num = -1;
                        if(matcher.group(2) != null){
                            try{
                                num = Integer.valueOf(matcher.group(2));
                            }
                            catch (NumberFormatException e){
                                sendMessage(message.getChat().getId(), "Number must be between 1 and " + (Integer.MAX_VALUE));
                                continue;
                            }
                        }
                        switch (command){
                            case "numbers_game_start":
                                onGameStartCommand(message, num);
                                break;
                            case "numbers_game_guess":
                                onNumberGuess(message, num);
                                break;
                            case "numbers_game_restart":
                                onGameReStartCommand(message, num);
                                break;
                            default:
                                sendMessage(message.getChat().getId(), HELP_MESSAGE);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

    private void onGameReStartCommand(Message message, int num){
        GameRoom gameRoom = gameContext.getOrCreateRoom(message.getChat().getId());
        switch (gameRoom.getGameState()){
            case IDLE:
            case FINISHED:
                sendMessage(message.getChat().getId(), "The game has not started yet!");
                break;
            case IN_PROGRESS:
                startGame(message, num, "The game has been restarted!");
                break;

        }
    }

    private void onGameStartCommand(Message message, int num){
        GameRoom gameRoom = gameContext.getOrCreateRoom(message.getChat().getId());
        switch (gameRoom.getGameState()){
            case IDLE:
            case FINISHED:
                startGame(message, num, "The game was started!");
                break;
            case IN_PROGRESS:
                sendMessage(message.getChat().getId(), "The game has already started!");
                break;

        }
    }

    private void startGame(Message message, int num, String msg){
        if(num < 1){
            sendMessage(message.getChat().getId(), "Number must be greater than 0!");
            return;
        }
        gameContext.startNewGame(message.getChat().getId(), num);
        sendMessage(message.getChat().getId(), msg);
    }

    private void onNumberGuess(Message message, int guessNum){
        GameRoom gameRoom = gameContext.getOrCreateRoom(message.getChat().getId());
        switch (gameRoom.getGameState()){
            case IDLE:
                sendMessage(message.getChat().getId(), "The game is not started. Please run /numbers_game_start [number] command!");
                break;
            case IN_PROGRESS:
                int gameNum = gameContext.getOrCreateRoom(message.getChat().getId()).getNumber();
                if(gameNum == guessNum){
                    gameRoom.setWinner(message.getFrom());
                    gameContext.setWinner(message.getChat().getId(), message.getFrom());
                    sendMessage(message.getChat().getId(), "Congratulations " +  message.getFrom().getFullName() + "!!! You win! To start new game run /numbers_game_start [number] command!");
                } else if(guessNum > gameNum && guessNum <= gameNum + 50){
                    sendMessage(message.getChat().getId(), "The number is less than " + guessNum);
                } else if(guessNum < gameNum && guessNum >= gameNum - 50){
                    sendMessage(message.getChat().getId(), "The number is greater than " + guessNum);
                } else {
                    sendMessage(message.getChat().getId(), "Wrong number. Try again.");
                }
                break;
            case FINISHED:
                sendMessage(message.getChat().getId(), "The game is finished. Please run /numbers_game_start [number] command!");
        }
    }

    private void sendMessage(long chatId, String message){
        try {
            requestQueue.put(new SendMessageRequest(chatId, message));
        } catch (InterruptedException e) {
            log.error(null, e);
        }
    }
}
