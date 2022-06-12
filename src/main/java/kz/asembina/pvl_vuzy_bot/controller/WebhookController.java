package kz.asembina.pvl_vuzy_bot.controller;

import kz.asembina.pvl_vuzy_bot.model.Step;
import kz.asembina.pvl_vuzy_bot.service.memory.LocaleService;
import kz.asembina.pvl_vuzy_bot.service.memory.StepServiceImpl;
import kz.asembina.pvl_vuzy_bot.telegram.PvlVuzyBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Deque;
import java.util.Map;


@RestController
public class WebhookController {

    private final PvlVuzyBot bot;
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    private final StepServiceImpl stepService;
    private final LocaleService localeService;

    public WebhookController(PvlVuzyBot bot, StepServiceImpl stepService, LocaleService localeService) {
        this.bot = bot;
        this.stepService = stepService;
        this.localeService = localeService;
    }
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        if(update.hasMessage()){
            logger.info("New update. Text: "+update.getMessage().getText()+", chatId: "+update.getMessage().getChatId());
        }
        if(update.hasCallbackQuery()){
            logger.info("New update. Callback: "+update.getCallbackQuery().getData()+", chatId: "+update.getCallbackQuery().getMessage().getChatId());
        }
        return bot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/steps")
    public String getSteps(){
        Map<Long, Deque<Step>> map = stepService.getMap();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Long, Deque<Step>> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(entry.getValue().toString());
        }
        return stringBuilder.toString();
    }
}
