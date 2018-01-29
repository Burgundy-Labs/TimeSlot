package controllers.ApplicationComponents;

import com.google.inject.Singleton;
import controllers.Databases.AppointmentsDB;
import models.AppointmentsModel;
import play.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class EmailScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public EmailScheduler() {
        schedule(new AppointmentEmailTask());
    }
    public void schedule(Runnable command) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime executionDate = LocalDateTime.of(currentTime.getYear(),
                currentTime.getMonth(),
                currentTime.getDayOfMonth(),
                8, 0);
        Logger.info("Email Reminder Service Started for - " + executionDate.getHour() + " : " + executionDate.getMinute());
        long initialDelay;
        if(currentTime.isAfter(executionDate)){
            initialDelay = currentTime.until(executionDate.plusDays(1), ChronoUnit.MILLIS);
        } else {
            initialDelay = currentTime.until(executionDate, ChronoUnit.MILLIS);
        }
        long delay = TimeUnit.HOURS.toMillis(24); // repeat after 24 hours
        scheduler.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    private class AppointmentEmailTask implements Runnable {
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
