const { compareScreenshots } = require('../../support/utils');

describe('TeacherDashboard', () => {
    before(() => {
        cy.cleanDb();

        cy.createCourseExecutionOnDemoCourse("2019/2020");
        cy.createCourseExecutionOnDemoCourse("2020/2021");
        cy.createCourseExecutionOnDemoCourse("2021/2022");

        cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
          Cypress.env('token', response.body.token);
          return response;
        });

        cy.demoTeacherLogin();
        
        cy.createTeacherDashboard();

        cy.createQuizStats();
        cy.createStudentStats();
        cy.createQuestionStats();

    });

    beforeEach(() => {

        cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
          Cypress.env('token', response.body.token);
          return response;
        });

        cy.demoTeacherLogin();
    });


    it('teacher accesses dashboard', () => {
        cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
          'getDashboard'
        );
        
        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getDashboard');
    });
    
    it('check if values in the containers are correct', () => {
        cy.get('[data-cy="dashboardMenuButton"]').click();

        cy.get('[data-cy="numStudents"]').contains('13');
        cy.get('[data-cy="numMore75CorrectQuestions"]').contains('13');
        cy.get('[data-cy="numAtLeast3Quizzes"]').contains('13');
        cy.get('[data-cy="numQuizzes"]').contains('13');
        cy.get('[data-cy="numUniqueAnsweredQuizzes"]').contains('13');
        cy.get('[data-cy="averageQuizzesSolved"]').contains('13');
        cy.get('[data-cy="numAvaible"]').contains('13');
        cy.get('[data-cy="answeredQuestionsUnique"]').contains('13');
        cy.get('[data-cy="averageQuestionsAnswered"]').contains('13');
    });

    it('check if bar graphs are correct', () => {
        cy.get('[data-cy="dashboardMenuButton"]').click();

        cy.get('[data-cy="studentStatsGraph"]').eq(0).scrollIntoView().wait(5000).screenshot('studentStatsGraph2-1')
        cy.get('[data-cy="quizStatsGraph"]').eq(0).scrollIntoView().wait(5000).screenshot('quizStatsGraph2-1')
        cy.get('[data-cy="questionStatsGraph"]').eq(0).scrollIntoView().wait(5000).screenshot('questionStatsGraph2-1')
        
        //compareScreenshots('expected-screenshot/studentStatsGraph2-1.png', 'studentStatsGraph2-1.png', 'diff1');
        //compareScreenshots('expected-screenshot/quizStatsGraph2-1.png', 'quizStatsGraph2-1.png', 'diff2');
        //compareScreenshots('expected-screenshot/questionStatsGraph2-1.png', 'questionStatsGraph2-1.png', 'diff3');
    });
});