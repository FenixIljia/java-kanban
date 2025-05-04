public class Main {
    public static void main(String[] args) {
/*
        final Path file = Paths.get(
                "D:" +
                        "\\Программирование" +
                        "\\Проекты на JavaScript" +
                        "\\Яндекс Практикум" +
                        "\\Проект Java Practicum" +
                        "\\Test.txt");
        final Path fileHistory = Paths.get(
                "D:" +
                        "\\Программирование" +
                        "\\Проекты на JavaScript" +
                        "\\Яндекс Практикум" +
                        "\\Проект Java Practicum" +
                        "\\TaskHistory.txt");
        Manager.FileBackedTaskManager fileBackedTaskManager = Manager.FileBackedTaskManager.loadFromFile(file, fileHistory);
        Data.Epic epic = new Data.Epic("Data.Epic", "Test", Data.Variety.EPIC);
        fileBackedTaskManager.addEpic(epic);
        Data.SubTask subTask = new Data.SubTask(
                "Subtask1",
                "In Data.Epic",
                epic.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );
        fileBackedTaskManager.addSubTask(subTask);
        Data.SubTask subTask2 = new Data.SubTask(
                "Subtask2",
                "In Data.Epic",
                epic.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2002, 1, 2, 2, 2)
        );
        fileBackedTaskManager.addSubTask(subTask2);
*/
       /* id,type,name,status,description,startTime,duration,endTime,epic
        2,TASK,Test4,NEW,Тестирование API,16.09.2007 14:55,2,16.09.2007 14:57
        5,TASK,Test,NEW,Test API with JSON,13.04.2000 15:45,50,13.04.2000 16:35
        22,TASK,Test,NEW,Test API with JSON,13.04.2002 15:45,50,13.04.2002 16:35
        6,TASK,Name,NEW,Test history,01.01.1000 03:04,50,01.01.1000 03:54
        10,TASK,Test,NEW,Тестирование API обновление задачи,16.09.2001 14:55,50,16.09.2001 15:45
        3,EPIC,Test,Пустой эпик
        4,EPIC,Test,Эпик с подзадачей
*/
/*        Manager.InMemoryTaskManager inMemoryTaskManager = new Manager.InMemoryTaskManager();
        Data.Epic epic = new Data.Epic("Data.Epic", "Test", Data.Variety.EPIC);
        inMemoryTaskManager.addEpic(epic);
        Data.SubTask subTask = new Data.SubTask(
                "Subtask1",
                "In Data.Epic",
                epic.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2,2)
        );
        Data.SubTask subTask2 = new Data.SubTask(
                "Subtask2",
                "In Data.Epic",
                epic.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2002, 1, 2, 2,2)
        );
        inMemoryTaskManager.addSubTask(subTask);
        inMemoryTaskManager.addSubTask(subTask2);
        System.out.println("");*/
    }
}

