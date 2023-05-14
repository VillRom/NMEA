package ru.pegas_agro;

import ru.pegas_agro.implement.CalculateWayImpl;
import ru.pegas_agro.service.CalculateWay;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        CalculateWay calculate = new CalculateWayImpl();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            if (command == 1) {
                System.out.println(calculate.calculateWay());
            }else if (command == 0) {
                System.out.println("Выход");
                break;
            } else {
                System.out.println("Такой команды пока не предусмотренно. Пожалуйста, выберите одну из списка.");
            }
        }
    }

    public static void printMenu(){
        System.out.println("1 - Рассчитать пройденный путь");
        System.out.println("0 - Завершить работу");
    }
}
