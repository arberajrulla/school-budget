import java.util.ArrayList;
import java.util.List;

public class School {
    private List<Student> students;
    private List<Teacher> teachers;
    private double totalMoneyEarned;
    private double totalMoneySpent;

    public School(){
    }
    public School(List<Student> students, List<Teacher> teachers){
        this.students = students;
        this.teachers = teachers;
    }

    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> students){
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }
    public void setTeachers(List<Teacher> teachers){
        this.teachers = teachers;
    }

    public double getTotalMoneyEarned() {
        return totalMoneyEarned;
    }
    public void setTotalMoneyEarned(double totalMoneyEarned){
        this.totalMoneyEarned = totalMoneyEarned;
    }

    public double getTotalMoneySpent() {
        return totalMoneySpent;
    }
    public void setTotalMoneySpent(double totalMoneySpent){
        this.totalMoneySpent = totalMoneySpent;
    }


    public void addTeacher(Teacher newTeacher){
        if (newTeacher != null){
            if (teachers != null){
                for (Teacher teacher : teachers){
                    if (teacher.getId() == newTeacher.getId()){
                        System.err.println("Teacher already exists in the list!");
                        return;
                    }
                }
                teachers.add(newTeacher);
            }else {
                teachers = new ArrayList<>();
                teachers.add(newTeacher);
            }
        } else {
            System.err.println("Teacher can not be null!");
        }
    }

    public void addStudent(Student newStudent){
        if (newStudent != null){
            if (students != null){
                for (Student student : students){
                    if (student.getId() == newStudent.getId()){
                        System.err.println("Student already exists in the list!");
                        return;
                    }
                }
                students.add(newStudent);
            }else {
                students = new ArrayList<>();
                students.add(newStudent);
            }
        } else {
            System.err.println("Student can not be null!");
        }
    }

}
