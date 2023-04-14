export default class QuestionStats {
    numAvailable!: number;
    answeredQuestionsUnique!: number;
    averageQuestionsAnswered!: number;
    courseExecutionYear!: number;
    constructor(jsonObj?: QuestionStats) {
        if (jsonObj) {
            this.numAvailable = jsonObj.numAvailable;
            this.answeredQuestionsUnique = jsonObj.answeredQuestionsUnique;
            this.averageQuestionsAnswered = jsonObj.averageQuestionsAnswered;
            this.courseExecutionYear = jsonObj.courseExecutionYear;
        }
    }
}

// Path: es23-30/frontend/src/models/management/QuizStats.ts
// Compare this snippet from es23-30/frontend/src/models/dashboard/TeacherDashboard.ts:
