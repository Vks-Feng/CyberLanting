<!-- 日历组件初始 -->
<template>
  <el-calendar
    ref="calendar"
    v-model="value"
    class="calendar-container"

  >
    <template #header="{ date }">
      <span>Custom header content</span>
      <span>{{ date }}</span>
      <el-button-group>
        <el-button size="small" @click="selectDate('prev-year')">
          Previous Year
        </el-button>
        <el-button size="small" @click="selectDate('prev-month')">
          Previous Month
        </el-button>
        <el-button size="small" @click="selectDate('today')">Today</el-button>
        <el-button size="small" @click="selectDate('next-month')">
          Next Month
        </el-button>
        <el-button size="small" @click="selectDate('next-year')">
          Next Year
        </el-button>
      </el-button-group>
    </template>

    <!-- 自定义日期单元格内容 -->
    <template #date-cell="{ data }">
      <div class="calendar-cell">
        <!-- 日期提取 -->
        <div class="calendar-day">
          {{ data.day.split("-").slice(2).join("-") }}
        </div>
        <!--获取当天任务，显示任务标题  -->
        <div class="todo-list" v-if="getTodosByDate(data.day).length > 0">
          <div
            v-for="(todo, index) in getTodosByDate(data.day)"
            :key="index"
            class="todo-item"
            :class="{ completed: todo.status === 'completed' }"
          >
            <div class="todo-title">{{ todo.name }}</div>
          </div>
        </div>
      </div>
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

// //重新获取数据
// const retryFetch = async () => {
//   await fetchTasks();
// }

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

onMounted(async () => {
  await fetchTasks();
});
</script>

<style scoped>
.calendar-container {

  backdrop-filter: blur(2px);
  background: rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  --el-calendar-cell-width: 40px;

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

.todo-item {
  margin-bottom: 2px;
  padding: 2px 4px;
  border-radius: 3px;
  background-color: #e6f7ff;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.todo-item.completed {
  background-color: #f6ffed;
  text-decoration: line-through;
  color: #999;
}

.todo-title {
  font-weight: 500;
}

.no-todos {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  opacity: 0.5;
}

/* 控制日历单元格的宽高 */
/* ::v-deep(.el-calendar-table td) {
  height: 20px  !important; 
  width: 10px; 
  padding: 4px;
} */


/* 自定义选中日期背景色 */
::v-deep(.el-calendar-table td.is-selected) {
  background-color: rgba(24, 88, 9, 0.15); /* 示例蓝色高亮 */
  border-radius: 6px;
}

::v-deep(.el-calendar-table td:hover) {
  background-color: rgba(4, 97, 12, 0.1);
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
}
.no-todos-text {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}
</style>
