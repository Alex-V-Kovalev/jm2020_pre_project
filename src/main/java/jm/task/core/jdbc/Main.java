package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        /*
        * Создание таблицы User(ов)
        * Добавление 4 User(ов) в таблицу с данными на свой выбор. После каждого добавления должен быть вывод в консоль ( User с именем – name добавлен в базу данных )
        * Получение всех User из базы и вывод в консоль ( должен быть переопределен toString в классе User)
        * Очистка таблицы User(ов)
        * Удаление таблицы
        */
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        for (int i = 0; i < 4; i++) {
            int j = i + 1;
            userService.saveUser("UserName" + j, "LastName" + j, (byte) (Math.random() * 128));
        }
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
