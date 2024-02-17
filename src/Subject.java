import java.util.List;

public class Subject {
    private int id;
    private String name;
    private Teacher teacher;
    private List<Student> studentList;

    public Subject() {
    }

    public Subject(int id, String name, Teacher teacher, List<Student> studentList) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.studentList = studentList;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher){
        this.teacher = teacher;
    }
    public List<Student> getStudentList() {
        return studentList;
    }
    public void setStudentList(List<Student> studentList){
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        StringBuilder studentsListString = new StringBuilder();
        if (studentList.size()==0){
            studentsListString = new StringBuilder("No students");
        }else {
            studentsListString = new StringBuilder();
            for (Student student : studentList){
                studentsListString.append(student).append(" | ");
            }
        }
        return "Subject: " +
                "Id=" + id +
                ", Name=" + name +
                ", Teacher: [" + teacher +
                "], StudentList: [" + studentsListString + "]";
    }
}
