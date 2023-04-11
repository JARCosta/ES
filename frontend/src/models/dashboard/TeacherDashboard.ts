import QuestionStats from '../management/QuestionStats';
import QuizStats from '../management/QuizzStats';
import StudentStats from '../management/StudentStats';

export default class TeacherDashboard {
  id!: number;
  questionStats!: QuestionStats[];
  quizStats!: QuizStats[];
  studentStats!: StudentStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.questionStats = jsonObj.questionStats.map((stats) => new QuestionStats(stats));
      this.quizStats = jsonObj.quizStats.map((stats) => new QuizStats(stats));
      this.studentStats = jsonObj.studentStats.map((stats) => new StudentStats(stats));
    }
  }
}
