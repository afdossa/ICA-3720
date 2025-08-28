package edu.clemson.studybuddy;

import edu.clemson.studybuddy.dao.StudentDAO;
import edu.clemson.studybuddy.model.Student;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO();

        while (true) {
            System.out.println("\n==== StudyBuddy Menu ====");
            System.out.println("1. Create Profile");
            System.out.println("2. View All Students");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    studentDAO.addStudent(new Student(0, name, email));
                    System.out.println("Profile created!");
                    break;

                case 2:
                    studentDAO.getAllStudents().forEach(System.out::println);
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    sc.close();
                    System.exit(0);
            }
        }
    }
}
