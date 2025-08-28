package Source;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        AvailabilityDAO availabilityDAO = new AvailabilityDAO();
        SessionDAO sessionDAO = new SessionDAO();
        MatchService matchService = new MatchService();

        while (true) {
            System.out.println("\n==== StudyBuddy Menu ====");
            System.out.println("1. Create Profile");
            System.out.println("2. View All Students");
            System.out.println("3. Enroll in a Course");
            System.out.println("4. View My Courses");
            System.out.println("5. Add Availability");
            System.out.println("6. View My Availability");
            System.out.println("7. Search Classmates by Course");
            System.out.println("8. Suggest Matches");
            System.out.println("9. Schedule a Study Session");
            System.out.println("10. View My Scheduled Sessions");
            System.out.println("11. Cancel a Session");
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

                case 3:
                    System.out.print("Enter student ID: ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter course code: ");
                    String code = sc.nextLine();
                    System.out.print("Enter course name: ");
                    String cname = sc.nextLine();
                    Course course = new Course(0, code, cname);
                    courseDAO.addCourse(course);
                    courseDAO.enrollStudentInCourse(sid, course.getId());
                    System.out.println("Student enrolled in course!");
                    break;

                case 4:
                    System.out.print("Enter student ID: ");
                    int sid2 = sc.nextInt();
                    sc.nextLine();
                    List<Course> myCourses = courseDAO.getCoursesByStudent(sid2);
                    myCourses.forEach(System.out::println);
                    break;

                case 5:
                    System.out.print("Enter student ID: ");
                    int sid3 = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter start time (yyyy-MM-dd HH:mm): ");
                    String startStr = sc.nextLine();
                    System.out.print("Enter end time (yyyy-MM-dd HH:mm): ");
                    String endStr = sc.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime start = LocalDateTime.parse(startStr, formatter);
                    LocalDateTime end = LocalDateTime.parse(endStr, formatter);
                    availabilityDAO.addAvailability(new Availability(0, sid3, start, end));
                    System.out.println("Availability added!");
                    break;

                case 6:
                    System.out.print("Enter student ID: ");
                    int sid4 = sc.nextInt();
                    sc.nextLine();
                    List<Availability> availList = availabilityDAO.getAvailabilityByStudent(sid4);
                    availList.forEach(System.out::println);
                    break;

                case 7:
                    System.out.print("Enter course code: ");
                    String code2 = sc.nextLine();
                    List<Student> classmates = courseDAO.getStudentsByCourse(code2);
                    classmates.forEach(System.out::println);
                    break;

                case 8:
                    System.out.print("Enter student ID: ");
                    int sid5 = sc.nextInt();
                    sc.nextLine();
                    matchService.suggestMatches(sid5).forEach(System.out::println);
                    break;

                case 9:
                    // Show all courses first
                    List<Course> courses = courseDAO.getAllCourses();
                    if (courses.isEmpty()) {
                        System.out.println("No courses available. Please add a course first.");
                        break;
                    }
                    System.out.println("Available Courses:");
                    courses.forEach(System.out::println);

                    System.out.print("Enter course ID: ");
                    int cid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter session time (yyyy-MM-dd HH:mm): ");
                    String timeStr = sc.nextLine();
                    System.out.print("Enter location: ");
                    String loc = sc.nextLine();

                    LocalDateTime time = LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-M-d HH:mm"));
                    Session session = new Session(0, cid, time, loc);
                    sessionDAO.addSession(session);

                    System.out.println("Session scheduled!");
                    break;

                case 10:
                    System.out.print("Enter student ID: ");
                    int sid6 = sc.nextInt();
                    sc.nextLine();
                    List<Session> sessions = sessionDAO.getSessionsByStudent(sid6);
                    sessions.forEach(System.out::println);
                    break;

                case 11:
                    System.out.print("Enter session ID to cancel: ");
                    int sessId = sc.nextInt();
                    sc.nextLine();
                    sessionDAO.cancelSession(sessId);
                    System.out.println("Session canceled!");
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    sc.close();
                    System.exit(0);
            }
        }
    }
}
