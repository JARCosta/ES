<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="numQuestAvail" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numQuestAvail[0]" />
          <BarChart :chartData="barChartData" :chartOptions="barChartOptions" />
        </div>
        <div class="project-name">
          <p>Number of Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="numQuestSolvedUnique" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numQuestSolvedUnique[0]" />
          <BarChart :chartData="barChartData" :chartOptions="barChartOptions" />
        </div>
        <div class="project-name">
          <p>Number of Unique Questions Solved</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageQuestSolved" class="icon-wrapper">
          <animated-number :number="teacherDashboard.averageQuestSolved[0]" />
          <BarChart :chartData="barChartData" :chartOptions="barChartOptions" />
        </div>
        <div class="project-name">
          <p>Average number of Unique Questions Solved per Student</p>
        </div>
      </div>
        <div class="items">
            <div ref="numMore75CorrectQuestions" class="icon-wrapper">
                <animated-number :number="teacherDashboard.numMore75CorrectQuestions" />
            </div>
            <div class="project-name">
                <p>Number of Students who Solved >= 75% Questions</p>
            </div>
        </div>
        <div class="items">
            <div ref="numAtLeast3Quizzes" class="icon-wrapper">
                <animated-number :number="teacherDashboard.numAtLeast3Quizzes" />
            </div>
            <div class="project-name">
                <p>Number of Students who Solved >= 3 Quizzes</p>
            </div>
        </div>
        <div class="items">
            <div ref="numQuizzes" class="icon-wrapper">
                <animated-number :number="teacherDashboard.numQuizzes" />
            </div>
            <div class="project-name">
                <p>Number of Quizzes</p>
            </div>
        </div>
        <div class="items">
            <div ref="numUniqueAnsweredQuizzes" class="icon-wrapper">
                <animated-number :number="teacherDashboard.numUniqueAnsweredQuizzes" />
            </div>
            <div class="project-name">
                <p>Number of Quizzes Solved (Unique)</p>
            </div>
        </div>
        <div class="items">
            <div ref="averageQuizzesSolved" class="icon-wrapper">
                <animated-number :number="teacherDashboard.averageQuizzesSolved" />
            </div>
            <div class="project-name">
                <p>Number of Quizzes Solved (Unique, Average Per Student)</p>
            </div>
        </div>
        <div class="items">
            <div ref="numAvailable" class="icon-wrapper">
                <animated-number :number="teacherDashboard.numAvailable" />
            </div>
            <div class="project-name">
                <p>Number of Questions</p>
            </div>
        </div>
        <div class="items">
            <div ref="answeredQuestionsUnique" class="icon-wrapper">
                <animated-number :number="teacherDashboard.answeredQuestionsUnique" />
            </div>
            <div class="project-name">
                <p>Number of Questions Solves (Unique)</p>
            </div>
        </div>
        <div class="items">
            <div ref="averageQuestionsAnswered" class="icon-wrapper">
                <animated-number :number="teacherDashboard.averageQuestionsAnswered" />
            </div>
            <div class="project-name">
                <p>Number of Questions Correctly Solved (Unique, Average Per Student)</p>
            </div>
        </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import BarChart from '@/components/Chart.vue';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';

@Component({
  components: { AnimatedNumber, BarChart },
})

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;
  barChartData: object = {}; // Pass the data object for BarChart
  barChartOptions: object = {}; // Pass the options object for BarChart

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();

      // Assign the data object and options object for BarChart
      this.barChartData = {
        labels: ['teacherDashboard.years[0]', 'teacherDashboard.years[1]', 'teacherDashboard.years[2]'],
        datasets: [
          {
            label: 'teacherDashboard.numQuestAvail',
            backgroundColor: 'rgba(255,99,132,0.2)',
            data: this.teacherDashboard.numQuestAvail
          },
          {
            label: 'teacherDashboard.numQuestSolvedUnique',
            backgroundColor: 'rgba(255,99,132,0.2)',
            data: this.teacherDashboard.numQuestSolvedUnique
          },
          {
            label: 'teacherDashboard.averageQuestSolved',
            backgroundColor: 'rgba(255,99,132,0.2)',
            data: this.teacherDashboard.averageQuestSolved
          }
        ]
      };
      
      this.barChartOptions = {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
      };
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
