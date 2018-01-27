package controllers.ApplicationComponents;

import com.google.inject.Singleton;
import controllers.Databases.AppointmentsDB;
import models.AppointmentsModel;
import play.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class EmailScheduler {
    public EmailScheduler() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 17);
        today.set(Calendar.MINUTE, 18);
        today.set(Calendar.SECOND, 0);
        Timer timer = new Timer();
        Logger.info("Email Reminder Service Started for - " + today.getTime().toString());
        timer.schedule(new AppointmentEmailTask(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    private class AppointmentEmailTask extends TimerTask {
        @Override
        public void run() {
            Calendar startDay = Calendar.getInstance();
            startDay.set(Calendar.HOUR_OF_DAY, 0);
            Calendar endDay = Calendar.getInstance();
            endDay.set(Calendar.HOUR_OF_DAY, 24);
            Logger.info("Checking for daily appointments.");
            List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(startDay.getTime(), endDay.getTime());
            MailerService.sendAppointmentReminder(appointments);
        }
    }
}
