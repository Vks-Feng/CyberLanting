<template>  
     <form @submit.prevent="onSubmit">  <!-- prevent default behavior of form submission(to server) -->
        <h2 class="label-wrapper">
            <label for="new-to-do-input">New To-Do:</label>
        </h2>
        <input type="text" id="new-todo-input" name="new-todo" autocomplete="off" v-model.trim.lazy="label" class="input__lg"/>   <!-- v-model绑定到data中的label变量,保持同步-->
        <button type="submit" class="btn btn__primary btn__lg">Add</button>
    </form>
</template>

<script>
export default {
    methods: {
        onSubmit() {
            if(this.label === "") {
                alert("Please enter a to-do item");
                return;
            }
            this.$emit("todo-added", this.label);   // 触发父组件的add-todo事件,并传入label作为参数},
            this.label = "";   // 清空输入框
        },
},
    data() {
        return{
            label:" ",
        };
    },

};
</script>

<style>
.input__lg {
  border-radius: 5px; /* 圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}
.btn {
  background-color: #007bff; /* 现代蓝色 */
  color: white;
  border-radius: 5px; /* 圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}
</style>