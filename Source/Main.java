package Source;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        AvailabilityDAO availabilityDAO = new AvailabilityDAO();
        SessionDAO sessionDAO = new SessionDAO();
        MatchService matchService = new MatchService();

        while (true) {
            try {
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

                if (!sc.hasNextInt()) {
                    System.out.println("Error: Please enter a number.");
                    sc.nextLine();
                    continue;
                }

                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1: // Create profile
                        System.out.print("Enter name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Enter email: ");
                        String email = sc.nextLine().trim();

                        if (name.isEmpty() || email.isEmpty()) {
                            System.out.println("Error: Name and email cannot be empty.");
                            break;
                        }

                        studentDAO.addStudent(new Student(0, name, email));
                        System.out.println("Profile created.");
                        break;

                    case 2: // View all students
                        List<Student> students = studentDAO.getAllStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                        break;

                    case 3: // Enroll in a course
                        int sid = safeReadInt(sc, "Enter student ID: ");
                        System.out.print("Enter course code: ");
                        String code = sc.nextLine().trim();
                        System.out.print("Enter course name: ");
                        String cname = sc.nextLine().trim();

                        if (code.isEmpty() || cname.isEmpty()) {
                            System.out.println("Error: Course code and name cannot be empty.");
                            break;
                        }

                        Course course = new Course(0, code, cname);
                        courseDAO.addCourse(course);
                        courseDAO.enrollStudentInCourse(sid, course.getId());
                        System.out.println("Student enrolled in course.");
                        break;

                    case 4: // View my courses
                        int sid2 = safeReadInt(sc, "Enter student ID: ");
                        List<Course> myCourses = courseDAO.getCoursesByStudent(sid2);
                        if (myCourses.isEmpty()) {
                            System.out.println("No courses found for this student.");
                        } else {
                            myCourses.forEach(System.out::println);
                        }
                        break;

                    case 5: // Add availability
                        int sid3 = safeReadInt(sc, "Enter student ID: ");
                        LocalDateTime start = safeReadDate(sc, "Enter start time (yyyy-MM-dd HH:mm): ");
                        LocalDateTime end = safeReadDate(sc, "Enter end time (yyyy-MM-dd HH:mm): ");

                        if (end.isBefore(start)) {
                            System.out.println("Error: End time must be after start time.");
                            break;
                        }

                        availabilityDAO.addAvailability(new Availability(0, sid3, start, end));
                        System.out.println("Availability added.");
                        break;

                    case 6: // View availability
                        int sid4 = safeReadInt(sc, "Enter student ID: ");
                        List<Availability> availList = availabilityDAO.getAvailabilityByStudent(sid4);
                        if (availList.isEmpty()) {
                            System.out.println("No availability found.");
                        } else {
                            availList.forEach(System.out::println);
                        }
                        break;

                    case 7: // Search classmates
                        System.out.print("Enter course code: ");
                        String code2 = sc.nextLine().trim();
                        List<Student> classmates = courseDAO.getStudentsByCourse(code2);
                        if (classmates.isEmpty()) {
                            System.out.println("No classmates found for this course.");
                        } else {
                            classmates.forEach(System.out::println);
                        }
                        break;

                    case 8: // Suggest matches
                        int sid5 = safeReadInt(sc, "Enter student ID: ");
                        List<Student> matches = matchService.suggestMatches(sid5);
                        if (matches.isEmpty()) {
                            System.out.println("No matches found.");
                        } else {
                            matches.forEach(System.out::println);
                        }
                        break;

                    case 9: // Schedule session
                        List<Course> courses = courseDAO.getAllCourses();
                        if (courses.isEmpty()) {
                            System.out.println("No courses available. Please add a course first.");
                            break;
                        }
                        System.out.println("Available Courses:");
                        courses.forEach(System.out::println);

                        int studentId = safeReadInt(sc, "Enter your student ID: ");
                        int cid = safeReadInt(sc, "Enter course ID: ");
                        LocalDateTime time = safeReadDate(sc, "Enter session time (yyyy-MM-dd HH:mm): ");
                        System.out.print("Enter location: ");
                        String loc = sc.nextLine().trim();

                        if (loc.isEmpty()) {
                            System.out.println("Error: Location cannot be empty.");
                            break;
                        }

                        Session session = new Session(0, cid, time, loc);
                        sessionDAO.addSession(session, studentId);
                        System.out.println("Session scheduled.");
                        break;

                    case 10: // View sessions
                        int sid6 = safeReadInt(sc, "Enter student ID: ");
                        List<Session> sessions = sessionDAO.getSessionsByStudent(sid6);
                        if (sessions.isEmpty()) {
                            System.out.println("No sessions found.");
                        } else {
                            sessions.forEach(System.out::println);
                        }
                        break;

                    case 11: // Cancel session
                        int sessId = safeReadInt(sc, "Enter session ID to cancel: ");
                        sessionDAO.cancelSession(sessId);
                        System.out.println("Session canceled.");
                        break;

                    case 0:
                        System.out.println("Goodbye.");
                        sc.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input type.");
                sc.nextLine(); // clear buffer
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use yyyy-MM-dd HH:mm");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Helper to safely read integers
    private static int safeReadInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int val = sc.nextInt();
                sc.nextLine(); // consume newline
                return val;
            } else {
                System.out.println("Error: Please enter a valid number.");
                sc.nextLine();
            }
        }
    }

    // Helper to safely read dates
    private static LocalDateTime safeReadDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                return LocalDateTime.parse(input, FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Use yyyy-MM-dd HH:mm");
            }
        }
    }
}
