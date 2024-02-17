
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SchoolTest {

    private Scanner scanner = new Scanner(System.in);
    private IOFileOperations ioFileOperations = new IOFileOperations();

    public static void main(String[] args) {
        SchoolTest school = new SchoolTest();
        school.mainMenu();
    }

    public void mainMenu(){
        while(true){
            System.out.println("Welcome to the Main Menu!");
            System.out.println("Enter one of the options below!");
            System.out.println("1- Manage teachers, students and subjects!");
            System.out.println("2- Manage school budget!");
            System.out.println("Any other character to exit!");

            if (scanner.hasNextInt()){
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        getOrganisationalMenu();
                        //I saw that the scanner was taking values automatically, probably by the leftovers in the buffer, so i used the following statement to clean them
                        scanner.nextLine();
                        break;
                    case 2:
                        getBudgetMenu();
                        scanner.nextLine();
                        break;
                    default:
                        return;
                }
            } else {
                return;
            }
        }
    }

    private void getOrganisationalMenu(){
        while (true){
            System.out.println("Welcome to the Teachers, Students and Subjects Menu!");
            System.out.println("1 - View teachers");
            System.out.println("2 - Add teacher");
            System.out.println("3 - View students");
            System.out.println("4 - Add student");
            System.out.println("5 - View subjects");
            System.out.println("6 - Add subject");
            System.out.println("Any other character to exit to main menu!");
            System.out.println("\nEnter one value to continue: ");

            if (scanner.hasNextInt()){
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("The list of Teachers:");
                        ioFileOperations.readTeachers().forEach(teacher -> System.out.println(teacher));
                        System.out.println();
                        break;
                    case 2:
                        ioFileOperations.addTeacher();
                        break;
                    case 3:
                        System.out.println("The list of Students:");
                        ioFileOperations.readStudents().forEach(student -> System.out.println(student));
                        System.out.println();
                        break;
                    case 4:
                        ioFileOperations.addStudent();
                        break;
                    case 5:
                        System.out.println("The list of Subjects:");
                        ioFileOperations.readSubjects().forEach(subject -> System.out.println(subject));
                        System.out.println();
                        break;
                    case 6:
                        ioFileOperations.addSubject();
                        break;
                    default:
                        return;
                }
            } else {
                scanner.nextLine();
                return;
            }
        }
    }



    private void getBudgetMenu(){
        School school = new School(ioFileOperations.readStudents(), ioFileOperations.readTeachers());

        while (true){
            System.out.println("Welcome to the School Budget Menu!");
            System.out.println("1 - Pay fee");
            System.out.println("2 - Check total earnings from tuition fees");
            System.out.println("3 - Check total spending from teachers’ salary");
            System.out.println("4 - Check net earnings/loses");
            System.out.println("5 - Output students in alphabetical order");
            System.out.println("Any other character to exit to main menu!");
            System.out.println("\nEnter one value to continue: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        school.setStudents(makePayment(school.getStudents()));
                        break;
                    case 2:
                        System.out.println(String.format("Total earnings from tuition fees are %s$\n", getTotalEarnings(school)));
                        break;
                    case 3:
                        System.out.println(String.format("Total spending from teachers’ salary are %s$\n", getTotalSpending(school)));
                        break;
                    case 4:
                        checkNetEarningOrLoss(school);
                        break;
                    case 5:
                        exportStudentListOrderedAlphabetically(school.getStudents());
                        break;
                    default:
                        return;
                }
            } else {
                scanner.nextLine();
                return;
            }
        }
    }

    private void exportStudentListOrderedAlphabetically(List<Student> students){
        students.sort(Comparator.comparing(student -> student.getName()));
        ioFileOperations.writeStudentsOnFile(students);
    }

    private void checkNetEarningOrLoss(School school){
        double earningsDifference = getTotalEarnings(school) - getTotalSpending(school);
        if (earningsDifference > 0){
            System.out.println(String.format("Net earnings of the school are %s$\n", earningsDifference));
        } else if (earningsDifference == 0){
            System.out.println("There is no net earning, but all the costs have been covered!\n");
        }  else {
            System.out.println(String.format("Net losses of the school are %s$\n", earningsDifference));
        }
    }

    private double getTotalSpending(School school){
        for (Teacher teacher : school.getTeachers()){
            school.setTotalMoneySpent(school.getTotalMoneySpent() + teacher.getSalary());
        }
        return school.getTotalMoneySpent();
    }

    private double getTotalEarnings(School school){
        for (Student student : school.getStudents()){
            school.setTotalMoneyEarned(school.getTotalMoneyEarned() + student.getFeesPaid());
        }
        return school.getTotalMoneyEarned();
    }

    private List<Student> makePayment(List<Student> students){
        boolean key = true;
        while (key){
            Student updatedStudent = payTuitionFee(selectStudent(students));
            if (updatedStudent != null){
                students = updateStudent(updatedStudent, students);
            } else {
                System.err.println("Student not found!");
            }
            //Check if there is another payment to be made for another student
            key = repeatLastAction();
        }
        ioFileOperations.updateStudentsOnFile(students);
        return students;
    }

    private Student selectStudent(List<Student> students){
        boolean key = true;
        printStudents(students);
        System.out.println("Enter student id from the list to proceed with the payment: ");
        while(key){
            if (scanner.hasNextInt()){
                int selectedId = scanner.nextInt();
                Student selectedStudent = getStudent(selectedId, students);
                if (selectedStudent == null){
                    System.err.println("Student with this id does not exist, please enter a correct ID!");
                } else {
                    return selectedStudent;
                }
            } else {
                System.err.println("Please enter a valid input");
            }
        }
        return null;
    }

    private List<Student> updateStudent(Student updatedStudent, List<Student> students){
        for(Student student : students){
            if (updatedStudent.getId() == student.getId()){
                students.set(students.indexOf(student), updatedStudent);
                break;
            }
        }
        return students;
    }

    private Student getStudent(int id, List<Student> students){
        return students.stream().filter(student -> student.getId() == id).findFirst().orElse(null);
    }

    private void printStudents(List<Student> students){
        students.stream().forEach(student -> System.out.println(student));
    }

    public Student payTuitionFee(Student student){
        if (student != null){
            boolean key = true;
            while(key){
                double remainingFee = 5000 - student.getFeesPaid();
                if(remainingFee == 0){
                    System.err.println(String.format("The tuition is already paid by student %s!", student.getName()));
                    key = false;
                } else if(remainingFee > 0) {
                    double inputValue = getPaymentAsInput();
                    if (inputValue < remainingFee){
                        student.payFee(inputValue);
                        System.out.println(String.format("You added successfully the value of %s$ for student %s. Student in total has paid %s$!\n", inputValue, student.getName(), student.getFeesPaid()));
                        //Check if there is no more value to be added
                        key = repeatLastAction();
                    } else if(inputValue == remainingFee){
                        student.payFee(inputValue);
                        System.out.println(String.format("You added successfully the value of %s$. Full tuition is now paid for student %s!\n", inputValue, student.getName()));
                        key = false;
                    } else {
                        student.payFee(remainingFee);
                        double difference = inputValue - remainingFee;
                        System.out.println(String.format("You added successfully the value of %s$. Full tuition is now paid for student %s. The extra %s$ you paid have been returned to you!\n", remainingFee, student.getName(), difference));
                        key = false;
                    }
                }
            }
        }
        return student;
    }

    public double getPaymentAsInput(){
        while (true){
            System.out.println("Enter the amount you want to pay: ");
            if (scanner.hasNextDouble()){
                double value = scanner.nextDouble();
                if(value >= 0){
                    return value;
                } else {
                    scanner.nextLine();
                    System.err.println("Please enter a valid value!");
                }
            } else {
                scanner.nextLine();
                System.err.println("Please enter only numbers!");
            }
        }
    }

    public boolean repeatLastAction(){
        System.out.println("Do you want to repeat the last action again? 1- Yes, Any other char- No");
        if (scanner.hasNextInt() && scanner.nextInt() == 1){
            scanner.nextLine();
            return true;
        }
        scanner.nextLine();
        return false;
    }


}