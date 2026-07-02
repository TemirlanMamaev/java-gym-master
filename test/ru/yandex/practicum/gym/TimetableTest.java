package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {
    public static void main(String[] args) {
        TimetableTest test = new TimetableTest();
        test.testGetTrainingSessionsForDaySingleSession();
        test.testGetTrainingSessionsForDayMultipleSessions();
        test.testGetTrainingSessionsForDayAndTime();
    }
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(0, tuesdaySessions.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        TimeOfDay firstTime = thursdaySessions.get(0).getTimeOfDay();
        TimeOfDay secondTime = thursdaySessions.get(1).getTimeOfDay();
        Assertions.assertEquals(13, firstTime.getHours());
        Assertions.assertEquals(20, secondTime.getHours());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> time13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        List<TrainingSession> time14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertEquals(1, time13.size());
        Assertions.assertEquals(0, time14.size());
    }

    @Test
    void testGetCountByCoachesEdgeCases() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> emptyResult = timetable.getCountByCoaches();
        Assertions.assertEquals(0, emptyResult.size());
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        timetable.addNewTrainingSession(new TrainingSession(
                new Group("Акробатика для детей", Age.CHILD, 60), coach,
                DayOfWeek.SUNDAY, new TimeOfDay(23, 59)));
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesEqualCounts() {
        Timetable timetable = new Timetable();
        Coach coachA = new Coach("Алексеев", "Алексей", "Алексеевич");
        Coach coachB = new Coach("Иванов", "Иван", "Иванович");

        // Каждый по 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(new Group
                ("Акробатика для детей1", Age.CHILD, 60), coachA, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(new Group
                ("Акробатика для детей2", Age.CHILD, 60), coachA, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(new Group
                ("Акробатика для детей3", Age.ADULT, 90), coachB, DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(new Group
                ("Акробатика для детей4", Age.ADULT, 90), coachB, DayOfWeek.FRIDAY, new TimeOfDay(19, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.get(0).getCount());
        Assertions.assertEquals(2, result.get(1).getCount());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Сидоров", "Петр", "Петрович");
        Coach coach2 = new Coach("Петров", "Алексей", "Алексеевич");

        for (int i = 0; i < 3; i++) {
            timetable.addNewTrainingSession(new TrainingSession(
                    new Group("Акробатика для детей", Age.CHILD, 60), coach1, DayOfWeek.MONDAY, new TimeOfDay(10 + i, 0)));
        }

        timetable.addNewTrainingSession(new TrainingSession(
                new Group("Акробатика для взрослых", Age.ADULT, 90), coach2, DayOfWeek.TUESDAY, new TimeOfDay(17, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(3, result.get(0).getCount());
        Assertions.assertEquals(1, result.get(1).getCount());
    }

}

