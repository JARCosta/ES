export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;
  numQuestAvail!: number[];
  numQuestSolvedUnique!: number[];
  averageQuestSolved!: number[];
  years!: number[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.numQuestAvail = jsonObj.numQuestAvail;
      this.numQuestSolvedUnique = jsonObj.numQuestSolvedUnique;
      this.averageQuestSolved = jsonObj.averageQuestSolved;
      this.years = jsonObj.years
    }
  }
}
