<!-- 日历组件初始 -->
<template>
  <el-calendar ref="calendar" v-model="value" class="calendar-container">
    <template #header="{ date }">
      <span></span>
      <span>{{ date }}</span>
      <el-button-group>
        <el-button size="small" @click="selectDate('prev-year')">
          先岁
        </el-button>
        <el-button size="small" @click="selectDate('prev-month')">
          先月
        </el-button>
        <el-button size="small" @click="selectDate('today')">今旦</el-button>
        <el-button size="small" @click="selectDate('next-month')">
          来月
        </el-button>
        <el-button size="small" @click="selectDate('next-year')">
          来岁
        </el-button>
      </el-button-group>
    </template>

    <!-- 自定义日期单元格内容 -->
    <template #date-cell="{ data }">
      <el-tooltip
        :content="`${getTaskCount(data.day)}个任务`"
        placement="top"
        effect="light"
      >
        <div calss="calendar-cell-color" :style="getCellStyle(data.day)">
          <span class="calendar-day">
            {{ data.day.split("-")[2] }}
          </span>
        </div>
      </el-tooltip>
    </template>
  </el-calendar>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue";
import { type CalendarDateType, type CalendarInstance } from "element-plus";
import calendarApi from "@/api/calendar-api";

//初始化和日期跳转
const calendar = ref<CalendarInstance>();
const selectDate = (val: CalendarDateType) => {
  if (!calendar.value) return;
  calendar.value.selectDate(val);

  if (val === "prev-year") {
    value.value.setFullYear(value.value.getFullYear());
  } else if (val === "prev-month") {
    value.value.setMonth(value.value.getMonth());
  } else if (val === "next-month") {
    value.value.setMonth(value.value.getMonth());
  } else if (val === "next-year") {
    value.value.setFullYear(value.value.getFullYear());
  } else if (val === "today") {
    value.value = new Date();
  }
  // console.log(value.value);
  fetchTasks();
};

const tasks = ref<Task[]>([]);
const value = ref(new Date());
// console.log(value);

//调用api获取任务数组
const fetchTasks = async () => {
  try {
    const response = calendarApi.getCalendarTasks(
      value.value.getFullYear(),
      value.value.getMonth() + 1 //从0开始计数，故加1
    );

    // console.log(value.value.getFullYear(), value.value.getMonth() + 1);

    const tasksData = await response;

    if (tasksData === null) {
      console.warn("获取任务列表失败");
      tasks.value = [];
    } else if (Array.isArray(tasksData) && tasksData.length === 0) {
      console.log("任务列表为空", tasksData);
      tasks.value = [];
    } else {
      tasks.value = tasksData;
    }
  } catch (error) {
    console.error("获取日历任务列表失败:", error);
    tasks.value = [];
  }
};

//监听日期变化，重新获取数据

// 根据日期获取当天的任务
const getTodosByDate = (date: string) => {
  if (!tasks.value || tasks.value.length === 0) {
    return [];
  }

  return tasks.value.filter((task) => {
    if (!task) return false;

    if (task.type === "single") {
      return task.ddl === date;
    } else if (task.type === "recurring") {
      try {
        const taskStartDate = new Date(task.startDate! as string);
        const taskEndDate = new Date(task.endDate! as string);
        const value = new Date(date);

        return value >= taskStartDate && value <= taskEndDate;
      } catch (e) {
        console.error("日期解析错误:", e);
        return false;
      }
    }
    return false;
  });
};

import { ElTooltip } from "element-plus";

const getCellStyle = (date: string) => {
  const count = getTodosByDate(date).length;

  // 渐变绿色（任务多颜色深）
  //   let bgColor = '#e0f3e0'; // 0任务：超浅绿
  let bgColor = "transparent"; // 0任务

  if (count >= 1) bgColor = "rgba(178, 224, 178, 0.4)"; // 轻绿色
  if (count >= 3) bgColor = "rgba(127, 207, 127, 0.5)";
  if (count >= 5) bgColor = "rgba(76, 175, 80, 0.6)";
  if (count >= 8) bgColor = "rgba(46, 125, 50, 0.7)"; // 最深绿

  return {
    backgroundColor: bgColor,
    borderRadius: "6px",
    height: "100%",
    width: "100%",
    // padding: '4px',
    boxSizing: "border-box",
    transition: "background-color 0.3s ease",
  };
};

const getTaskCount = (date: string) => {
  return getTodosByDate(date).length;
};

onMounted(async () => {
  await fetchTasks();
});
</script>

<style scoped>
.calendar-container {
  /* backdrop-filter: blur(2px);
  background: rgba(255, 255, 255, 0.3); */
  background: transparent;
  border-radius: 10px;
  --el-calendar-cell-width: 45px;
  
}

.loading-state,
.error-state {
  padding: 20px;
  min-height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.error-state p {
  color: var(--el-text-color-secondary);
  margin-bottom: 16px;
}

.el-calendar {
  width: 100%;
}

.calendar-day {
  text-align: right;
  padding: 2px 5px;
  font-weight: bold;
}

.todo-list {
  flex: 1;
  overflow-y: auto;
  padding: 2px;
}



/* 自定义选中日期背景色 */
::v-deep(.el-calendar-table td.is-selected) {
  background-color: rgba(24, 88, 9, 0.15);
  border-radius: 6px;
  /* padding: 4px; */
}

::v-deep(.el-calendar-table td:hover) {
  background-color: rgba(4, 97, 12, 0.1);
  cursor: pointer;
}

::v-deep(.el-calendar-table .el-calendar-day) {
  padding: 3px;
}

::v-deep(.calendar-cell-color) {
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  /* padding: 4px; */
  font-size: 10px;
  color: #333;
  font-weight: bold;
  height: 100%;
  width: 100%;
}

::v-deep(.calendar-cell-color:hover) {
  outline: 2px solid #097a58;
  cursor: pointer;
}

::v-deep(.el-calendar__body),
::v-deep(.el-calendar-table) {
  height: 100%;
}

::v-deep(.el-calendar-day) {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  padding: 2px;
}

::v-deep(.calendar-day-number) {
  z-index: 1;
}
</style>
