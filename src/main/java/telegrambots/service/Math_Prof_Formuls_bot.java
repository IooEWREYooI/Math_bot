package telegrambots.service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambots.HTML.ParsingMath;

import java.io.*;
import java.util.*;

import static telegrambots.BotConfig.*;
import static telegrambots.PhotoDirectory.*;

@Component
public class Math_Prof_Formuls_bot extends TelegramLongPollingBot {
    final static public String path = (new File("C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_bot\\Формулы\\").exists()) ? "C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_bot\\Формулы\\" : "/root/bot/Формулы/" ;
    @Override
    public String getBotUsername() {

        return (new File("C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\").exists()) ? BOT_NAME_TEST : BOT_NAME;
    }
    @Override
    public String getBotToken() {
        return (new File("C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\").exists()) ? BOT_TOKEN_TEST : BOT_TOKEN;
    }
    @Async
    @Override
    public void onUpdateReceived(Update update) {
        boolean hasMessage = update.hasMessage() && update.getMessage().hasText();
        String command = update.getMessage().getText();
            dataForUsersOnFormuls = initData(randomIndex());
            long chatId = update.getMessage().getChatId();
            boolean isFormulaAnswer = hasMessage && !userOnScanByFormuls.containsKey(chatId) && command.equalsIgnoreCase("/formula");
            boolean isUserInFormulsMap = hasMessage && userOnScanByFormuls.containsKey(chatId);
            boolean isTaskAnswer = hasMessage && !userOnScanByTasks.containsKey(chatId) && command.equalsIgnoreCase("/task");
            boolean isUserInTaskMap = hasMessage && userOnScanByTasks.containsKey(chatId);
            if (isFormulaAnswer) {
                formulaAnswer(update);
            }
            else if (isUserInFormulsMap) {
                clearForFormulaAnswer(update);
            }
            else if (isTaskAnswer) {
                taskAnswer(update);
            }
            else if (isUserInTaskMap) {
                clearForTaskAnswer(update);
            }
    }
    private void formulaAnswer(Update update) {
        long chatId = update.getMessage().getChatId();
        sendMessage(update, dataForUsersOnFormuls[0]);
        userOnScanByFormuls.put(chatId, dataForUsersOnFormuls);
    }
    private void taskAnswer(Update update){
        long chatId = update.getMessage().getChatId();
        ArrayList<String> list = new ParsingMath().getListQuestionsAndAnswers();
        Collections.shuffle(list);
        String[] arrayQuestion = list.get(0).split(" \\| ");
        File SVG = new File(path + chatId + ".svg");
        File PNG = new File(path + chatId + ".png");
        convertURL2Svg(arrayQuestion[0], SVG);
        convertSvg2Png(SVG, PNG);
        try {
            sendPhoto(update, PNG, "*Решите пример:*");
        } finally {
            userOnScanByTasks.put(chatId, arrayQuestion[1]);
            deleteSvgAndPng(chatId);
        }
    }
    private void clearForTaskAnswer(Update update){
        long chatId = update.getMessage().getChatId();
        String command = update.getMessage().getText();
        String answer = userOnScanByTasks.get(chatId).replaceAll("\\*","x");
        try {
            sendMessage(update, (command.equals(answer)) ? "Правильно! "+ answer : "Не правильно "+ answer);
        } finally {
            userOnScanByTasks.remove(chatId);
        }
    }
    private void clearForFormulaAnswer(Update update){
        long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equals(userOnScanByFormuls.get(chatId)[1]))
            sendPhoto(update, new File(path + userOnScanByFormuls.get(chatId)[userOnScanByFormuls.get(chatId).length - 1]), "Правильно " + userOnScanByFormuls.get(chatId)[1]);
        else
            sendPhoto(update, new File(path + userOnScanByFormuls.get(chatId)[userOnScanByFormuls.get(chatId).length - 1]), "Не правильно " + userOnScanByFormuls.get(chatId)[1]);
        userOnScanByFormuls.remove(chatId);
        dataForUsersOnFormuls = initData(randomIndex());
    }
    private String[] dataForUsersOnFormuls = initData(randomIndex());
    private HashMap<Long, String> userOnScanByTasks = new HashMap<>();
    private HashMap<Long, String[]> userOnScanByFormuls = new HashMap<>();
    private String[] initData(int index){
        ArrayList<String[]> list = getMapPhotos();
        String[] dataForUsersOnFormuls = list.get(index);
        return dataForUsersOnFormuls;
    }
    private int randomIndex(){
        return (int) (Math.random() * getMapPhotos().size());
    }
    private void sendPhoto(Update update, File photo, String caption){
        try {
            execute(SendPhoto.builder()
                    .chatId(update.getMessage().getChatId())
                    .photo(new InputFile(photo))
                    .caption(caption)
                    .parseMode("Markdown")
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessage(Update update, String text){
        try {
            execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(text)
                    .parseMode("Markdown")
                    .disableWebPagePreview(true)
                    .build());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}