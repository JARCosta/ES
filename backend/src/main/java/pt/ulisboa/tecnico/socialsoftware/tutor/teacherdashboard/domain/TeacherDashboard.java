package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
//import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
public class TeacherDashboard implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CourseExecution courseExecution;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherDashboard", orphanRemoval = true)
    private final List<QuizStats> quizzesStats = new ArrayList<>();

    @ManyToOne
    private Teacher teacher;


    public TeacherDashboard(CourseExecution courseExecution, Teacher teacher) {
        setCourseExecution(courseExecution);
        setTeacher(teacher);
        this.quizzesStats.add(new QuizStats(this.courseExecution));       
    }

    public void update() {
        for (QuizStats QuizStats : quizzesStats) {
            QuizStats.update();
        }
    }

    public void remove() {
        teacher.getDashboards().remove(this);
        teacher = null;
    }

    public Integer getId() {
        return id;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        this.teacher.addDashboard(this);
    }

   // public void createQuizzesStats() {
        //quizzesStats.add(new QuizStats(this.courseExecution));
        /* //get a list of the last 3 course executions (sprint2)
        Course course = courseExecution.getCourse();
        List<CourseExecution> cexList = new ArrayList<CourseExecution>(course.getCourseExecutions());
        // order list by enddate of course execution (most recent first)
        cexList.sort((cex1, cex2) -> cex2.getEndDate().compareTo(cex1.getEndDate()));
        for (int i = 0; i < 3; i++) {
            if (i >= cexList.size()) break;
            if (cexList.get(i).getId() != courseExecution.getId()) {
                QuizzesStats.add(new QuizStats(cexList.get(i)));
            }
        }
        */
   // }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacher=" + teacher +
                '}';
    }

}
