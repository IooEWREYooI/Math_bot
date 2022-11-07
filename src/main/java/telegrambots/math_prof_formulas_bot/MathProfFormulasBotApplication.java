package telegrambots.math_prof_formulas_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambots.service.Math_Prof_Formuls_bot;

@SpringBootApplication
public class MathProfFormulasBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathProfFormulasBotApplication.class, args);
		try {
			TelegramBotsApi TelegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			Math_Prof_Formuls_bot bot = new Math_Prof_Formuls_bot();
			TelegramBotsApi.registerBot(bot);
			System.out.println("Бот "+bot.getBotUsername()+" -> успешно запущен");
		} catch(TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
