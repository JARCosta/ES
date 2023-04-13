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
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import BarChart from '@/components/BarChart.vue';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';

@Component({
  components: { AnimatedNumber, Chart },
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
        labels: ["teacherDashboard.years[0]", "teacherDashboard.years[1]", "teacherDashboard.years[2]"],
        datasets: [
          {
            label: "teacherDashboard.numQuestAvail",
            backgroundColor: "rgba(255,99,132,0.2)",
            data: teacherDashboard.numQuestAvail
          },
          {
            label: "teacherDashboard.numQuestSolvedUnique",
            backgroundColor: "rgba(255,99,132,0.2)",
            data: teacherDashboard.numQuestSolvedUnique
          },
          {
            label: "teacherDashboard.averageQuestSolved",
            backgroundColor: "rgba(255,99,132,0.2)",
            data: teacherDashboard.averageQuestSolved
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
