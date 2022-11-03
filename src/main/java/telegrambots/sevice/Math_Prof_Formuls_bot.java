package telegrambots.sevice;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.*;
@Component
public class Math_Prof_Formuls_bot extends TelegramLongPollingBot {
    String path = (new File("C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\").exists()) ? "C:\\Users\\Яков Мануилов\\IdeaProjects\\TelegramBots\\Math_prof_formulas_bot\\Формулы\\" : "/root/bot/Формулы/" ;
    @Override
    public String getBotUsername() {
        return "";
    }
    @Override
    public String getBotToken() {
        return "";
    }
    @Override
    public void onUpdateReceived(Update update) {
            data = initData(randomIndex());
            long chatId = update.getMessage().getChatId();

            if (update.hasMessage() && update.getMessage().hasText() && !userOnScan.containsKey(chatId)) {
                sendMessage(update, data[0]);
                userOnScan.put(chatId, data);
            }
            else {
                if (update.getMessage().getText().equals(userOnScan.get(chatId)[1]))
                    sendPhoto(update, new File(path + userOnScan.get(chatId)[userOnScan.get(chatId).length - 1]),"Правильно "+ userOnScan.get(chatId)[1]);
                else
                    sendPhoto(update, new File(path + userOnScan.get(chatId)[userOnScan.get(chatId).length - 1]),"Не правильно "+ userOnScan.get(chatId)[1]);
                userOnScan.remove(chatId);
                data = initData(randomIndex());
            }
    }
    private String[] data = initData(randomIndex());
    private HashMap<Long, String[]> userOnScan = new HashMap<>();
    private String[] initData (int index){
        ArrayList<String[]> list = initList();
        String[] data = list.get(index);
        return data;
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
                    .build());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

