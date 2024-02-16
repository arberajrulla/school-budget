import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOFileOperations {

    private Scanner scanner = new Scanner(System.in);

    public List<Teacher> readTeachers(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/teachers.txt");
        List<Teacher> teachers = new ArrayList<>();

        for (String[] data : rows){
            teachers.add(new Teacher(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2])));
        }
        return teachers;
    }

    public List<Student> readStudents(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/students.txt");
        List<Student> students = new ArrayList<>();

        for (String[] data : rows){
            students.add(new Student(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2])));
        }
        return students;
    }

    public List<Subject> readSubjects(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/subjects.txt");
        List<Subject> subjects = new ArrayList<>();

        for (String[] row : rows){
            List<Teacher> allTeachers = readTeachers();
            Teacher assignedTeacher = new Teacher();
            for(Teacher teacher : allTeachers){
                if(teacher.getId() == Integer.parseInt(row[0])){
                    assignedTeacher = teacher;
                    break;
                }
            }
            List<Student> allStudents = readStudents();
            List<Student> subjectStudents = readStudents();
            for (int i = 3; i < row.length; i++){
                for (Student student : allStudents){
                    if (Integer.parseInt(row[i]) == student.getId()){
                        subjectStudents.add(student);
                        break;
                    }
                }
            }
            subjects.add(new Subject(Integer.parseInt(row[0]), row[1], assignedTeacher, subjectStudents));
        }
        return subjects;
    }


    private List<String[]> readFromFile(String filename){
        File file = new File(filename);
        List<String[]> fileRows = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String row;
            while ((row = bufferedReader.readLine()) != null) {
                fileRows.add(row.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileRows;
    }


    public void addTeacher(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/teachers.txt");
        boolean key = true;
        while(key){
            int id = generateId(rows);
            scanner.nextLine();
            System.out.println("Please enter the name of the teacher:");
            String name = scanner.nextLine();

            System.out.println("Please enter the salary of the teacher(decimal number):");
            if (scanner.hasNextDouble()){
                double salary = scanner.nextDouble();
                writeNewLineOnFile(System.getProperty("user.dir") + "/src/files/teachers.txt", new String[]{String.valueOf(id), name, String.valueOf(salary)});
                System.out.println("Teacher registered successfully!\n");
                key = false;
            } else {
                System.err.println("Salary must be a decimal value!");
            }
        }
    }

    public void addStudent(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/students.txt");
        int id = generateId(rows);
        scanner.nextLine();
        System.out.println("Please enter the name of the student: ");
        String name = scanner.nextLine();
        writeNewLineOnFile(System.getProperty("user.dir") + "/src/files/students.txt", new String[]{String.valueOf(id), name, String.valueOf(0.0)});
        System.out.println("Student registered successfully!");
    }

    public void addSubject(){
        List<String[]> rows = readFromFile(System.getProperty("user.dir") + "/src/files/subjects.txt");
        List<String[]> students = readFromFile(System.getProperty("user.dir") + "/src/files/students.txt");
        List<String[]> teachers = readFromFile(System.getProperty("user.dir") + "/src/files/teachers.txt");

        int id = generateId(rows);
        System.out.println("Please enter the name of the subject:");
        String name = scanner.nextLine();

        int selectedTeacherId = getSelectedTeacherId(teachers);
        List<String> selectedStudents = getSelectedStudentsIds(students);

        String[] newSubject = new String[selectedStudents.size() + 3];
        newSubject[0] = String.valueOf(id);
        newSubject[1] = name;
        newSubject[2] = String.valueOf(selectedTeacherId);
        int index = 3;
        for (String studentId : selectedStudents){
            newSubject[index] = studentId;
            index++;
        }
        writeNewLineOnFile(System.getProperty("user.dir") + "/src/files/subjects.txt", newSubject);
        System.out.println("Subject registered successfully!\n");
    }


    private int getSelectedTeacherId(List<String[]> teachers){
        while(true) {
            for (String[] teacher : teachers) {
                System.out.println(String.format("Teacher: Id=%s | Name=%s ", teacher[0], teacher[1]));
            }
            System.out.println("Please select a teacher and type the id:");
            if (scanner.hasNextInt()) {
                int selectedTeacher = scanner.nextInt();
                if (existsId(selectedTeacher, teachers)) {
                    System.out.println("Teachers id accepted!\n");
                    return selectedTeacher;
                } else {
                    System.err.println("Teacher with this id does not exist!");
                }
            } else {
                System.err.println("Please enter numeric value!");
            }
        }
    }


    private List<String> getSelectedStudentsIds(List<String[]> students){
        List<String> selectedStudents = new ArrayList<>();
        for (String[] student : students){
            System.out.println(String.format("Student: Id=%s | Name=%s ", student[0], student[1]));
        }
        int stopValue = 1;
        while (stopValue!=0){
            System.out.println("Please type the student id's you want to add and press Enter, to finish type 0!");
            if (scanner.hasNextInt()){
                int selectedStudentsId = scanner.nextInt();
                if (selectedStudentsId == 0){
                    stopValue = 0;
                } else if (existsId(selectedStudentsId, students)){
                    selectedStudents.add(String.valueOf(selectedStudentsId));
                    System.out.println(String.format("Student with id %s added successfully!", selectedStudentsId));
                } else {
                    System.err.println("Please enter a correct student id");
                }
            } else {
                System.out.println("Please enter only numeric value!");
            }
        }
        return selectedStudents;
    }

    private boolean existsId(int id, List<String[]> objects){
        for (String[] object : objects){
            if (Integer.parseInt(object[0]) == id){
                return true;
            }
        }
        return false;
    }

    private int generateId(List<String[]> objects){
        int maxIndex = 0;
        for (String[] object : objects){
            if (Integer.parseInt(object[0]) > maxIndex){
                maxIndex = Integer.parseInt(object[0]);
            }
        }
        return maxIndex + 1;
    }

    private void writeNewLineOnFile(String filename, String[] newRow){
        File file = new File(filename);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.write(String.join(",", newRow));
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStudentsOnFile(List<Student> students){
        File file = new File(System.getProperty("user.dir") + "/src/files/students.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Student student : students ) {
                bufferedWriter.write(String.join(",", String.valueOf(student.getId()), student.getName(), String.valueOf(student.getFeesPaid())));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStudentsOnFile(List<Student> students){
        File file = new File(System.getProperty("user.dir") + "/src/files/student.text");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Student student : students ){
                bufferedWriter.write(String.join(",", String.valueOf(student.getId()), student.getName(), String.valueOf(student.getFeesPaid())));
                bufferedWriter.newLine();
            }
            System.out.println("List exported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
