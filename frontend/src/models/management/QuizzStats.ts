export default class QuizStats {
  numQuizzes!: number;
  numUniqueAnsweredQuizzes!: number;
  averageQuizzesSolved!: number;
  courseExectuion!: number;
  constructor(jsonObj?: QuizStats) {
    if (jsonObj) {
      this.numQuizzes = jsonObj.numQuizzes;
      this.numUniqueAnsweredQuizzes = jsonObj.numUniqueAnsweredQuizzes;
      this.averageQuizzesSolved = jsonObj.averageQuizzesSolved;
      this.courseExectuion = jsonObj.courseExectuion;
    }
  }
}
