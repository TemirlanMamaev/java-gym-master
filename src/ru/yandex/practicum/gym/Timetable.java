package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private final Map<DayOfWeek, List<TrainingSession>> sessionsByDay = new HashMap<>();
    public Timetable() {

    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        timetable
                .computeIfAbsent(day, d -> new TreeMap<>())
                .computeIfAbsent(time, t -> new ArrayList<>())
                .add(trainingSession);

        sessionsByDay
                .computeIfAbsent(day, d -> new ArrayList<>())
                .add(trainingSession);

        sessionsByDay.get(day)
                .sort(Comparator.comparing(TrainingSession::getTimeOfDay));
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return sessionsByDay.getOrDefault(dayOfWeek, Collections.emptyList());
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
