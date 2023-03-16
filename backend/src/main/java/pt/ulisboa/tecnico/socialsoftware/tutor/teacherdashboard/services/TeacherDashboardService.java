package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuestionStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.TeacherRepository;

import java.util.*;

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
    private QuestionStatsRepository questionStatsRepository;

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

        teacherDashboard.setQuestionStats(questionStatsRepository.findAllById(null));

        List<QuestionStats> questionStats = new ArrayList<>();
        for(QuestionStats questionStat : questionStatsRepository.findAll()){
            if(questionStat.getCourseExecution().getId().equals(courseExecution.getId()))
                questionStats.add(questionStat);
            else if(questionStat.getCourseExecution().getYear() == courseExecution.getYear() - 1 )
                questionStats.add(questionStat);
            else if(questionStat.getCourseExecution().getYear() == courseExecution.getYear() - 2 )
                questionStats.add(questionStat);
        }
        teacherDashboard.setQuestionStats(questionStats);


        teacherDashboardRepository.save(teacherDashboard);

        return new TeacherDashboardDto(teacherDashboard);
    }

    public void updateTeacherDashboard(int dashboardId){
        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, dashboardId));
        teacherDashboard.update();
    }

    public void updateAllTeacherDashboards(){
        teacherRepository.findAll().forEach(teacher -> {
            teacher.getCourseExecutions().forEach(courseExecution -> {
                getTeacherDashboard(courseExecution.getId(), teacher.getId());
            });
        });
        for (TeacherDashboard teacherDashboard : teacherDashboardRepository.findAll()) {
            teacherDashboard.update();
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeTeacherDashboard(Integer dashboardId) {
        if (dashboardId == null)
            throw new TutorException(DASHBOARD_NOT_FOUND, -1);

        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId).orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, dashboardId));
        
        Iterator<StudentStats> iterator = teacherDashboard.getStudentStats().iterator();
        Iterator<QuestionStats> iterator2 = teacherDashboard.getQuestionStats().iterator();

        while(iterator.hasNext()){
            StudentStats studentStats = iterator.next();
            iterator.remove();
            studentStats.remove();
            studentStatsRepository.delete(studentStats);
        }
        while(iterator2.hasNext()){
            QuestionStats questionStats = iterator2.next();
            iterator2.remove();
            questionStats.remove();
            questionStatsRepository.delete(questionStats);
        }
        
        teacherDashboard.remove();
        teacherDashboardRepository.delete(teacherDashboard);
    }

}
