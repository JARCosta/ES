export default class QuestionStats {
  numAvaible!: number;
  answeredQuestionsUnique!: number;
  averageQuestionsAnswered!: number;
  courseExecutionYear!: number;
  constructor(jsonObj?: QuestionStats) {
    if (jsonObj) {
      this.numAvaible = jsonObj.numAvaible;
      this.answeredQuestionsUnique = jsonObj.answeredQuestionsUnique;
      this.averageQuestionsAnswered = jsonObj.averageQuestionsAnswered;
      this.courseExecutionYear = jsonObj.courseExecutionYear;
    }
  }
}
