package telegrambots.service;

import org.apache.commons.io.FileUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static telegrambots.BotConfig.*;

@Component
public class Math_Prof_Formuls_bot extends TelegramLongPollingBot {
    String path = (new File("C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\").exists()) ? "C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\" : "/root/bot/Формулы/" ;
    @Override
    public String getBotUsername() {
        return BOT_NAME_TEST;
    }
    @Override
    public String getBotToken() {
        return BOT_TOKEN_TEST;
    }
    @Async
    @Override
    public void onUpdateReceived(Update update) {
        boolean hasMessage = update.hasMessage() && update.getMessage().hasText();
        String command = update.getMessage().getText();
            dataForUsersOnFormuls = initData(randomIndex());
            long chatId = update.getMessage().getChatId();

            if (hasMessage && !userOnScanByFormuls.containsKey(chatId) && command.equalsIgnoreCase("Формула")) {
                sendMessage(update, dataForUsersOnFormuls[0]);
                userOnScanByFormuls.put(chatId, dataForUsersOnFormuls);
            }
            else if (hasMessage && userOnScanByFormuls.containsKey(chatId)) {
                if (update.getMessage().getText().equals(userOnScanByFormuls.get(chatId)[1]))
                    sendPhoto(update, new File(path + userOnScanByFormuls.get(chatId)[userOnScanByFormuls.get(chatId).length - 1]), "Правильно " + userOnScanByFormuls.get(chatId)[1]);
                else
                    sendPhoto(update, new File(path + userOnScanByFormuls.get(chatId)[userOnScanByFormuls.get(chatId).length - 1]), "Не правильно " + userOnScanByFormuls.get(chatId)[1]);
                userOnScanByFormuls.remove(chatId);
                dataForUsersOnFormuls = initData(randomIndex());
            }
            else if (hasMessage && !userOnScanByTasks.containsKey(chatId) && command.equalsIgnoreCase("Задание")) {
                ArrayList<String> list = new ParsingMath().getListQuestionsAndAnswers();
                Collections.shuffle(list);
                String[] arrayQuestion = list.get(0).split(" \\| ");
                URL url = null;
                File SVG = new File(chatId+".svg");
                try {
                    url = new URL(arrayQuestion[0]);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    FileUtils.copyURLToFile(url, SVG);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sendPhoto(update,SVG,"sadasd");
                userOnScanByTasks.put(chatId, arrayQuestion[1]);
            }
            else if (hasMessage && userOnScanByTasks.containsKey(chatId)) {
                String answer = userOnScanByTasks.get(chatId);
                sendMessage(update, (command.equals(answer)) ? "Правильно! "+answer : "Не правильно "+answer);
                userOnScanByTasks.remove(chatId);
            }


    }
    private String[] dataForUsersOnFormuls = initData(randomIndex());
    private HashMap<Long, String> userOnScanByTasks = new HashMap<>();
    private HashMap<Long, String[]> userOnScanByFormuls = new HashMap<>();
    private String[] initData (int index){
        ArrayList<String[]> list = initList();
        String[] dataForUsersOnFormuls = list.get(index);
        return dataForUsersOnFormuls;
    }
    private int randomIndex(){
        return (int) (Math.random() * initList().size());
    }
    private static ArrayList<String[]> initList(){
        ArrayList<String[]> list = new ArrayList<>();
        String s = ".PNG";
        list.add(new String[]{"А и Б несовместных событий ","Р(А) * Р(Б)", "А и Б несовместных событий" + s});
        list.add(new String[]{"А и Б совместных событий ","Р(А) + Р(Б) * Р(А*Б)", "А и Б совместных событий" + s});
        list.add(new String[]{"А или Б совместных событий ","Р(А) + Р(Б)", "А или Б совместных событий" + s});
        list.add(new String[]{"Косинус суммы ","cosA * cosB - sinA * sinB", "Косинус суммы" + s});
        list.add(new String[]{"Косинус разницы ","cosA * cosB + sinA * sinB", "Косинус суммы" + s});
        list.add(new String[]{"Косинус двойного угла ","cos^2A - sin^2A","2cos^2A - 1","1 - 2sin^2A", "Косинусы двойного угла" + s});
        list.add(new String[]{"Логарифм log_a(x) = b","x = a^b", "Логарифм" + s});
        list.add(new String[]{"Нахождения квадрата косинуса из синуса","1 - sin^2A", "Нахождение косинуса" + s});
        list.add(new String[]{"Нахождения косинуса из синуса","+-sqrt(1 - sin^2A)", "Нахождение косинуса" + s});
        list.add(new String[]{"Нахождения квадрата синуса из косинуса","1 - cos^2A", "Нахождение синуса" + s});
        list.add(new String[]{"Нахождения синуса из косинуса","+-sqrt(1 - cos^2A)", "Нахождение синуса" + s});
        list.add(new String[]{"Синус двойного угла","2sinA * cosA", "Синус двойного угла" + s});
        list.add(new String[]{"Синус двойного угла","2sinA * cosA", "Синус двойного угла" + s});
        list.add(new String[]{"Синус суммы ","sinA * cosB + cosA * sinB", "Синус суммы" + s});
        list.add(new String[]{"Синус разности ","sinA * cosB - cosA * sinB", "Синус суммы" + s});
        list.add(new String[]{"sin0 = ?","0", "Таблица" + s});
        list.add(new String[]{"sin30 = ?","1/2", "Таблица" + s});
        list.add(new String[]{"sin45 = ?","sqrt(2)/2", "Таблица" + s});
        list.add(new String[]{"sin60 = ?","sqrt(3)/2", "Таблица" + s});
        list.add(new String[]{"sin90 = ?","1", "Таблица" + s});
        list.add(new String[]{"cos90 = ?","0", "Таблица" + s});
        list.add(new String[]{"cos60 = ?","1/2", "Таблица" + s});
        list.add(new String[]{"cos45 = ?","sqrt(2)/2", "Таблица" + s});
        list.add(new String[]{"cos30 = ?","sqrt(3)/2", "Таблица" + s});
        list.add(new String[]{"cos0 = ?","1", "Таблица" + s});
        return list;
    }
    private void sendPhoto (Update update, File photo, String caption){
        try {
            execute(SendPhoto.builder()
                    .chatId(update.getMessage().getChatId())
                    .photo(new InputFile(photo))
                    .caption(caption)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessage (Update update, String text){
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

