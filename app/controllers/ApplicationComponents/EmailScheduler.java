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
import java.util.concurrent.TimeUnit;

@Singleton
public class EmailScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public EmailScheduler() {
        schedule(new AppointmentEmailTask());
    }
    public void schedule(Runnable command) {
        fixDatabase();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime executionDate = LocalDateTime.of(currentTime.getYear(),
                currentTime.getMonth(),
                currentTime.getDayOfMonth(),
                8,0);
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

    void fixDatabase() {
        for(AppointmentsModel a : AppointmentsDB.getAppointmentsByDate(new Date(1520744400000L), new Date(1533960000000L))){
            if(a.isWeekly()){
                List<AppointmentsModel> weeklyList = AppointmentsDB.getWeeklyAppointmentsByWeeklyId(a.getWeeklyId());
                for ( AppointmentsModel app : weeklyList ) {
                    if ( app.getStartDate().before(new Date(1520744400000L)) ) {
                        Calendar appCalendarEnd = Calendar.getInstance();
                        appCalendarEnd.setTime(app.getEndDate());
                        Calendar aCalendarEnd = Calendar.getInstance();
                        aCalendarEnd.setTime(a.getEndDate());
                        aCalendarEnd.set(Calendar.HOUR, appCalendarEnd.get(Calendar.HOUR));
                        aCalendarEnd.set(Calendar.MINUTE, appCalendarEnd.get(Calendar.MINUTE));

                        Calendar appCalendarStart = Calendar.getInstance();
                        appCalendarStart.setTime(app.getStartDate());
                        Calendar aCalendarStart = Calendar.getInstance();
                        aCalendarStart.setTime(a.getStartDate());
                        aCalendarStart.set(Calendar.HOUR, appCalendarStart.get(Calendar.HOUR));
                        aCalendarStart.set(Calendar.MINUTE, appCalendarStart.get(Calendar.MINUTE));
                        a.setStartDate(aCalendarStart.getTime());
                        a.setEndDate(aCalendarEnd.getTime());
                        AppointmentsDB.addAppointment(a);
                        break;
                    }
                }
            }
        }
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
            HashMap<String, ArrayList<AppointmentsModel>> coachAppointments = new HashMap<>();
            HashMap<String, ArrayList<AppointmentsModel>> studentAppointments = new HashMap<>();
            for(AppointmentsModel a : appointments){
                coachAppointments.computeIfAbsent(a.getCoachId(), k -> new ArrayList<>()).add(a);
                studentAppointments.computeIfAbsent(a.getStudentId(), k -> new ArrayList<>()).add(a);
            }
            MailerService.sendAppointmentReminder(studentAppointments, "Student");
            MailerService.sendAppointmentReminder(coachAppointments, "Coach");
        }
    }
}
