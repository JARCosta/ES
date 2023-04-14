describe('TeacherDashboard', () => {

    before(() => {

    cy.demoTeacherLogin();
    cy.logout();

    cy.createCourseExecutionDB('1st Semester 2020/2021');

    });


    afterEach(() => {
        cy.logout();
    });

    it('teacher gets 2020/2021 dashboard', () => {

        cy.intercept('GET', '**/students/dashboards/executions/*').as(
            'getTeacherDashboard'
        );

        // cy.demoTeacherLogin();

        cy.get('a[href*="/courses"]').contains('span', 'change course').click();

        cy.get('[data-cy="1st Semester 2020/2021"]').click();

        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getTeacherDashboard');


        cy.get('[data-cy="numStudents"]').contains(0);
        cy.get('[data-cy="numMore75CorrectQuestions"]').contains(0);
        cy.get('[data-cy="numAtLeast3Quizzes"]').contains(0);

        cy.get('a[href*="/"]').contains('demo course').click();

        cy.updateStudentStatsInDashboard(88, 99, 111, '1st Semester 2019/2020');

        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getTeacherDashboard');

        
        cy.get('[data-cy="numStudents"]').contains(88);
        cy.get('[data-cy="numMore75CorrectQuestions"]').contains(99);
        cy.get('[data-cy="numAtLeast3Quizzes"]').contains(111);

        cy.get('[data-cy="studentStatsChart"]').should('not.exist');

    });

});
