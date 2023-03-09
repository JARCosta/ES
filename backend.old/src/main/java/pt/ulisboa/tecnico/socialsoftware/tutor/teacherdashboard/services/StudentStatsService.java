package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.StudentStatsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.TeacherRepository;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class StudentStatsService {

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // @Autowired
    // private StudentStatsRepository studentStatsRepository;

    @Autowired
    private TeacherDashboardRepository teacherDashboardRepository;

    /*
    @Retryable(
        value = {SQLException.class},
        backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<StudentStatsDto> updateDashboardStudentStats(int dashboardId) {
        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId).orElseThrow(() -> new TutorException(ErrorMessage.DASHBOARD_NOT_FOUND, dashboardId));

    }
     */

    public Set<CourseExecution> getLastThreeYears(CourseExecution courseExecution) {
        Set<CourseExecution> ret = new HashSet<>();
        ret.add(courseExecution);
        
        for (CourseExecution year : courseExecution.getCourse().getCourseExecutions()) {
            if(year.getYear() == courseExecution.getYear() - 1)
                ret.add(year);
            else if(year.getYear() == courseExecution.getYear() - 2)
                ret.add(year);
        }
        return ret;
    }

    public Set<StudentStats> getLast3ExecutionsStats(Integer courseExecutionId){
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND));
        
        Set<StudentStats> ret = new HashSet<>();
        for(CourseExecution courseExecutionTemp : getLastThreeYears(courseExecution)){
            courseExecutionTemp.getStudentStats().update();
            ret.add(courseExecutionTemp.getStudentStats());
        }
        return ret;
    }

}
