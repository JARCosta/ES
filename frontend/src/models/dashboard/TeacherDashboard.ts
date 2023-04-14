import QuestionStats from '../management/QuestionStats';
import QuizStats from '../management/QuizStats';
import StudentStats from '../management/StudentStats';

export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;
  years!: number[];
  numStudents!: number;
  numMore75CorrectQuestions!: number;
  numAtLeast3Quizzes!: number;
  numQuizzes!: number;
  numUniqueAnsweredQuizzes!: number;
  averageQuizzesSolved!: number;
  numAvailable!: number;
  answeredQuestionsUnique!: number;
  averageQuestionsAnswered!: number;
  numCorrect!: number;
  questionStats!: QuestionStats[];
  quizStats!: QuizStats[];
  studentStats!: StudentStats[];


  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.years = jsonObj.years
      this.numStudents = jsonObj.numStudents;
      this.numMore75CorrectQuestions = jsonObj.numMore75CorrectQuestions;
      this.numAtLeast3Quizzes = jsonObj.numAtLeast3Quizzes;
      this.numQuizzes = jsonObj.numQuizzes;
      this.numUniqueAnsweredQuizzes = jsonObj.numUniqueAnsweredQuizzes;
      this.averageQuizzesSolved = jsonObj.averageQuizzesSolved;
      this.numAvailable = jsonObj.numAvailable;
      this.answeredQuestionsUnique = jsonObj.answeredQuestionsUnique;
      this.averageQuestionsAnswered = jsonObj.averageQuestionsAnswered;
      this.numCorrect = jsonObj.numCorrect;
      this.questionStats = jsonObj.questionStats.map((stats) => new QuestionStats(stats));
      this.quizStats = jsonObj.quizStats.map((stats) => new QuizStats(stats));
      this.studentStats = jsonObj.studentStats.map((stats) => new StudentStats(stats));
    }
  }
}
