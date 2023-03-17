package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuizStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.TeacherRepository;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class TeacherDashboardService {

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherDashboardRepository teacherDashboardRepository;

    @Autowired
    private StudentStatsRepository studentStatsRepository;

    @Autowired
    private QuizStatsRepository quizStatsRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TeacherDashboardDto getTeacherDashboard(int courseExecutionId, int teacherId) {
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TutorException(USER_NOT_FOUND, teacherId));

        if (!teacher.getCourseExecutions().contains(courseExecution))
            throw new TutorException(TEACHER_NO_COURSE_EXECUTION);

        Optional<TeacherDashboard> dashboardOptional = teacher.getDashboards().stream()
                .filter(dashboard -> dashboard.getCourseExecution().getId().equals(courseExecutionId))
                .findAny();

        return dashboardOptional.
                map(TeacherDashboardDto::new).
                orElseGet(() -> createAndReturnTeacherDashboardDto(courseExecution, teacher));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TeacherDashboardDto createTeacherDashboard(int courseExecutionId, int teacherId) {
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TutorException(USER_NOT_FOUND, teacherId));

        if (teacher.getDashboards().stream().anyMatch(dashboard -> dashboard.getCourseExecution().equals(courseExecution)))
            throw new TutorException(TEACHER_ALREADY_HAS_DASHBOARD);

        if (!teacher.getCourseExecutions().contains(courseExecution))
            throw new TutorException(TEACHER_NO_COURSE_EXECUTION);

        return createAndReturnTeacherDashboardDto(courseExecution, teacher);
    }

    private TeacherDashboardDto createAndReturnTeacherDashboardDto(CourseExecution courseExecution, Teacher teacher) {
        TeacherDashboard teacherDashboard = new TeacherDashboard(courseExecution, teacher);
        
        List<CourseExecution> coursesFromLast3Years = courseExecutionRepository.findAll().stream()
                                            .filter( ce -> ce.getCourse() == courseExecution.getCourse())
                                            .sorted((ss1, ss2) -> ss2.getAcademicTerm().compareTo(ss1.getAcademicTerm()))
                                            .limit(3).collect(Collectors.toList());
        for(CourseExecution ce : coursesFromLast3Years){
            if(ce.getStudentStats() != null)
                teacherDashboard.addStudentStats(ce.getStudentStats());
            else
                studentStatsRepository.save(new StudentStats(teacherDashboard, ce));
            }
        
        setQuizStatsForLast3CourseExecutions(teacherDashboard);
        teacherDashboardRepository.save(teacherDashboard);
        return new TeacherDashboardDto(teacherDashboard);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeTeacherDashboard(Integer dashboardId) {
        if (dashboardId == null)
            throw new TutorException(DASHBOARD_NOT_FOUND, -1);

        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId).orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, dashboardId));

        Iterator<StudentStats> iterator = teacherDashboard.getStudentStats().iterator();
        
        while(iterator.hasNext()){
            StudentStats studentStats = iterator.next();
            iterator.remove();
            studentStats.remove();
            studentStatsRepository.delete(studentStats);
        }
        
        quizStatsRepository.deleteAll(teacherDashboard.getQuizStats());
        
        
        teacherDashboard.remove();
        teacherDashboardRepository.delete(teacherDashboard);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateTeacherDashboard(Integer dashboardId) {
        if (dashboardId == null)
            throw new TutorException(DASHBOARD_NOT_FOUND, -1);

        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId).orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, dashboardId));
        teacherDashboard.update();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateAllTeacherDashboards() {
        teacherRepository.findAll().forEach(teacher -> {
            teacher.getCourseExecutions().forEach(courseExecution -> {
                getTeacherDashboard(courseExecution.getId(), teacher.getId());
            });
        });

        List<TeacherDashboard> teacherDashboards = teacherDashboardRepository.findAll();
        
        for (TeacherDashboard teacherDashboard : teacherDashboards) {
            teacherDashboard.update();
        }
    }

    private List<CourseExecution> getLast3CourseExecutions(CourseExecution courseExecution){
        Course course = courseExecution.getCourse();
        List<CourseExecution> cexList = new ArrayList<CourseExecution>(course.getCourseExecutions().stream()
                                        .filter((CourseExecution cex) -> cex.getEndDate() != null 
                                        && (cex.getEndDate() == courseExecution.getEndDate() || cex.getEndDate().compareTo(courseExecution.getEndDate()) < 0))
                                        .collect(Collectors.toList()));
        // order list by enddate of course execution (most recent 1st)
        cexList.sort((cex1, cex2) -> cex2.getEndDate().compareTo(cex1.getEndDate()));
        return cexList;
    }

    private void setQuizStatsForLast3CourseExecutions(TeacherDashboard teacherDashboard){
        List<CourseExecution> cexList = getLast3CourseExecutions(teacherDashboard.getCourseExecution());
        for (int i = 0; i < Math.min(cexList.size(), 3); i++) {
            new QuizStats(teacherDashboard, cexList.get(i));
        }
        quizStatsRepository.saveAll(teacherDashboard.getQuizStats());
    }
}

