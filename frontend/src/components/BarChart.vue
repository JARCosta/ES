<template>
  <Bar :chart-data="chartData" :chart-options="chartOptions" />
</template>

<script lang="ts">
import { Bar } from 'vue-chartjs/legacy';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';

import type { ChartData, ChartOptions } from 'chart.js';
import { Component, Prop, Vue } from 'vue-property-decorator';

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);

@ Component({
  components: { Bar },
})

export default class BarChart extends Vue {
  @Prop() chartData: ChartData<'bar'> | undefined;
  @Prop() chartOptions: ChartOptions<'bar'> | undefined;

  data() {
    return {
      chartData: this.chartData,
      chartOptions: this.chartOptions,
    };
  }
}
</script>
