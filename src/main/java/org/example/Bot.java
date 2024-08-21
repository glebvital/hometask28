package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "botbot";
    }

    @Override
    public String getBotToken() {
        return "7268739901:AAHHRkWTifPrdx-J0suvcoBz0_HjNUmuxE8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        long idFromUser = update.getMessage().getFrom().getId();
        String nameFromUser = update.getMessage().getFrom().getFirstName();
        String UsersText = update.getMessage().getText();
        if (UsersText.equals("/start")){
            sendText(idFromUser,"Hi this is format msg=text, date=20/05/24, time=12:00");
            return;
        }
        alertMaker(idFromUser,UsersText,nameFromUser);
    }

    private void alertMaker(long idFromUser, String usersText, String nameFromUser) {
        String[] data = usersText.split(",");
        String msg = data[0].split("=")[1];
        String date = data[1].split("=")[1];
        String time = data[2].split("=")[1];
        long futureTime = LocalDateTime.of(Integer.parseInt(date.split("/")[2]), Month.valueOf(date.split("/")[1]), Integer.parseInt(date.split("/")[0]), Integer.parseInt(time.split(":")[1]),Integer.parseInt(time.split(":")[0])).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        TimeThread thread = new TimeThread();
        thread.setInfo(futureTime,msg,idFromUser);
        thread.start();
    }


    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public class TimeThread extends Thread{
        private long futureTime;
        private String massage;
        private long idFromUser;

        public void setFutureTime(long futureTime){
            this.futureTime = futureTime;
        }

        private void setMassage(String  massage){
            this.massage = massage;
        }

        private void setIdFromUser(long idFromUser){
            this.idFromUser = idFromUser;
        }

        private void setInfo(long futureTime,String  massage,long idFromUser){
            this.futureTime = futureTime;
            this.massage = massage;
            this.idFromUser = idFromUser;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(futureTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendText(idFromUser,massage);
        }
    }

}
