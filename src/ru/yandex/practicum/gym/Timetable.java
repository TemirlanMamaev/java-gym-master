package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public Timetable() {

    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        timetable.putIfAbsent(day, new TreeMap<>());
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        dayMap.putIfAbsent(time, new ArrayList<>());
        List<TrainingSession> sessionsTime = dayMap.get(time);
        sessionsTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();
        if (dayMap == null || dayMap.isEmpty()) {
            return Collections.emptyList();
        }
        for (List<TrainingSession> list : dayMap.values()) {
            result.addAll(list);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(sessions);

    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countMap = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    countMap.put(coach, countMap.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));

        return result;
    }
}
