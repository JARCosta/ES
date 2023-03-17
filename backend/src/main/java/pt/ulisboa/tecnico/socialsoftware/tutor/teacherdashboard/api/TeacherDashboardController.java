package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services.TeacherDashboardService;

import java.security.Principal;

@RestController
public class TeacherDashboardController {
    @Autowired
    private TeacherDashboardService teacherDashboardService;

    TeacherDashboardController(TeacherDashboardService teacherDashboardService) {
        this.teacherDashboardService = teacherDashboardService;
    }

    @GetMapping("/teachers/dashboards/executions/{courseExecutionId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#courseExecutionId, 'EXECUTION.ACCESS')")
    public TeacherDashboardDto getTeacherDashboard(Principal principal, @PathVariable int courseExecutionId) {
        int teacherId = ((AuthUser) ((Authentication) principal).getPrincipal()).getUser().getId();

        return teacherDashboardService.getTeacherDashboard(courseExecutionId, teacherId);
    }

    @DeleteMapping("/teachers/dashboards/{dashboardId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#dashboardId, 'TEACHER_DASHBOARD.ACCESS')")
    public void removeTeacherDashboard(@PathVariable int dashboardId) {
        teacherDashboardService.removeTeacherDashboard(dashboardId);
    }

    @GetMapping("/teachers/dashboards/executions/{courseExecutionId}/update/teacher")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#courseExecutionId, 'EXECUTION.ACCESS')")
    public void updateTeacherDashboard(Principal principal, @PathVariable int courseExecutionId) {
        int teacherId = ((AuthUser) ((Authentication) principal).getPrincipal()).getUser().getId();
        Integer dashboardId = teacherDashboardService.getTeacherDashboard(courseExecutionId, teacherId).getId();
        teacherDashboardService.updateTeacherDashboard(dashboardId);
    }

    @GetMapping("/teachers/dashboards/executions/{courseExecutionId}/update/admin")
    @PreAuthorize("hasRole('ADMIN') and hasPermission(#courseExecutionId, 'EXECUTION.ACCESS')")
    public void updateAllTeacherDashboard(Principal principal, @PathVariable int courseExecutionId) {
        teacherDashboardService.updateAllTeacherDashboards();;
    }
}
