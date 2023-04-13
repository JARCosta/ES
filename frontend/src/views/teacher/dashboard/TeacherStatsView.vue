<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="numStudents" class="icon-wrapper" data-cy="numStudents">
          <animated-number
            :number="teacherDashboard.studentStats[0].numStudents"
          />
        </div>
        <div class="project-name">
          <p>Number of Students</p>
        </div>
      </div>
      <div class="items">
        <div ref="numMore75CorrectQuestions" class="icon-wrapper" data-cy="numMore75CorrectQuestions">
          <animated-number
            :number="teacherDashboard.studentStats[0].numMore75CorrectQuestions"
          />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 75% Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="numAtLeast3Quizzes" class="icon-wrapper" data-cy="numAtLeast3Quizzes">
          <animated-number
            :number="teacherDashboard.studentStats[0].numAtLeast3Quizzes"
          />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 3 Quizzes</p>
        </div>
      </div>
      <div class="items">
        <div ref="numQuizzes" class="icon-wrapper">
          <animated-number :number="teacherDashboard.quizStats[0].numQuizzes" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes</p>
        </div>
      </div>
      <div class="items">
        <div ref="numUniqueAnsweredQuizzes" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.quizStats[0].numUniqueAnsweredQuizzes"
          />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageQuizzesSolved" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.quizStats[0].averageQuizzesSolved"
          />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique, Average Per Student)</p>
        </div>
      </div>
      <div class="items">
        <div ref="numAvailable" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].numAvaible"
          />
        </div>
        <div class="project-name">
          <p>Number of Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="answeredQuestionsUnique" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].answeredQuestionsUnique"
          />
        </div>
        <div class="project-name">
          <p>Number of Questions Solves (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageQuestionsAnswered" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].averageQuestionsAnswered"
          />
        </div>
        <div class="project-name">
          <p>
            Number of Questions Correctly Solved (Unique, Average Per Student)
          </p>
        </div>
      </div>
    </div>
    <h2> Comparison with previous course executions </h2>
    <div class = "stats-container">
      <div v-if="teacherDashboard != null" class="bar-chart" data-cy="studentStatsChart">
      <bar-chart
        :chartData="{
          labels : teacherDashboard.studentStats.map(
            (x) => x.courseExecutionYear),
            
        datasets: [
          {
            label: 'Number of Students',
            data: teacherDashboard.studentStats.map((x) => x.numStudents),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
          },
          {
            label: 'Number of Students who Solved >= 75% Questions',
            data: teacherDashboard.studentStats.map((x) =>
              x.numMore75CorrectQuestions
            ),
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
          },
          {
            label: 'Number of Students who Solved >= 3 Quizzes',
            data: teacherDashboard.studentStats.map((x) =>
              x.numAtLeast3Quizzes
            ),
            backgroundColor: 'rgba(255, 206, 86, 0.2)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1,
          },
        ],
      }"
        :title="'Student Stats'"
      />       
    </div> 

    <div v-if="teacherDashboard != null" class="bar-chart">
      <bar-chart
        :chartData="{
          labels : teacherDashboard.quizStats.map(
            (x) => x.courseExecutionYear),
            
        datasets: [
          {
            label: 'Quizzes: Total Available',
            data: teacherDashboard.quizStats.map((x) => x.numQuizzes),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
          },
          {
            label: 'Quizzes: Solved (Unique)',
            data: teacherDashboard.quizStats.map((x) =>
              x.numUniqueAnsweredQuizzes
            ),
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
          },
          {
            label: 'Quizzes: Solved (Unique, Average Per Student)',
            data: teacherDashboard.quizStats.map((x) =>
              x.averageQuizzesSolved
            ),
            backgroundColor: 'rgba(255, 206, 86, 0.2)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1,
          },
        ],
      }"
        :title="'Quiz Stats'"
      />       
    </div> 

    <div v-if="teacherDashboard != null" class="bar-chart">
      <bar-chart
        :chartData="{
          labels : teacherDashboard.questionStats.map(
            (x) => x.courseExecutionYear),
            
        datasets: [
          {
            label: 'Questions: Total Available',
            data: teacherDashboard.questionStats.map((x) => x.numAvaible),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
          },
          {
            label: 'Questions: Total Solved (Unique)',
            data: teacherDashboard.questionStats.map((x) =>
              x.answeredQuestionsUnique
            ),
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
          },
          {
            label: 'Questions: Correctly Solved (Unique, Average Per Student)',
            data: teacherDashboard.questionStats.map((x) =>
              x.averageQuestionsAnswered
            ),
            backgroundColor: 'rgba(255, 206, 86, 0.2)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1,
          },
        ],
      }"
        :title="'Questions Stats'"
      />       
    </div> 

    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';
import BarChart from '@/components/BarChart.vue';

@Component({
  components: { AnimatedNumber, BarChart }
})

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}
</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    background-color: rgba(255, 255, 255, 0.90);
    height: 400px;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
