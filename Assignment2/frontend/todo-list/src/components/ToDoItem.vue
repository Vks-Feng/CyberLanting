<template>
    <div class="stack-small" v-if="!isEdititing">
        <div class="custom-checkbox">
            <input type="checkbox" :id="id" :checked="isDone" class="checkbox" @change="$emit('checkbox-changed')" /> <!-- 按下按钮触发更新 -->
            <label :for="id" class="checkbox-label"> {{ label }} </label>
        </div>
        <div class="btn-group">
            <button type="button" class="btn" @click="toggleToItemEditForm" > <!-- 点击按钮触发编辑 -->
                Edit
                <span class="visually-hidden">{{ label }}</span>
            </button>
            <button type="button" class="btn btn__danger" @click ="deleteToDo" >
                Delete
                <span class="visually-hidden">{{ label }}</span>
            </button>
        </div>
    </div>

    <to-do-item-edit-form v-else :id="id" :label="label" @item-edited="itemEdited" @edit-cancelled="editCancelled"> </to-do-item-edit-form>

    
</template>


<script>
import ToDoItemEditForm from './ToDoItemEditForm.vue';


export default {
components: {
        ToDoItemEditForm,
    },  

props: {
    label: {
        type: String,
        required: true
    },
    done:{
        default: false,
        type: Boolean
    },
    id: {
        required: true,
        type: String,
    },
},
    data() {  //返回包含单个属性的对象；将所有props绑定到组件实例上
            // 需要保持键的唯一性，不能与其他组件的属性名重复
        return {
            isEdititing: false,
        };
    },

    computed: { //利用计算属性保持反应性
        isDone() {
            return this.done;
        },
    },

    methods: {
        toggleToItemEditForm() {
            this.isEdititing = true;
            },
        deleteToDo() {
            this.$emit('item-deleted');
        },
        itemEdited(newLabel){
            this.$emit('item-edited', newLabel);
            this.isEdititing = false;
        },
        editCancelled(){
            this.isEdititing = false;
        },
    },
};
</script>

<style scoped>
.custom-checkbox > .checkbox-label {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 400;
  font-size: 16px;
  font-size: 1rem;
  line-height: 1.25;
  color: #333;
  display: block;
  margin-bottom: 5px;
}
.custom-checkbox > .checkbox {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 400;
  font-size: 16px;
  font-size: 1rem;
  line-height: 1.25;
  box-sizing: border-box;
  width: 100%;
  height: 40px;
  height: 2.5rem;
  margin-top: 0;
  padding: 5px;
  border: 2px solid #ccc;
  border-radius: 5px;
  appearance: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.custom-checkbox > input:focus {
  outline: 3px dashed #fd0;
  outline-offset: 0;
  box-shadow: inset 0 0 0 2px;
}
.custom-checkbox {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  font-weight: 400;
  font-size: 1.6rem;
  line-height: 1.25;
  display: block;
  position: relative;
  min-height: 40px;
  margin-bottom: 10px;
  padding-left: 40px;
  clear: left;
}
.custom-checkbox > input[type="checkbox"] {
  -webkit-font-smoothing: antialiased;
  cursor: pointer;
  position: absolute;
  z-index: 1;
  top: -2px;
  left: -2px;
  width: 44px;
  height: 44px;
  margin: 0;
  opacity: 0;
}
.custom-checkbox > .checkbox-label {
  font-size: inherit;
  font-family: inherit;
  line-height: inherit;
  display: inline-block;
  margin-bottom: 0;
  padding: 8px 15px 5px;
  cursor: pointer;
  touch-action: manipulation;
}
.custom-checkbox > label::before {
  content: "";
  box-sizing: border-box;
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  border: 2px solid currentcolor;
  background: transparent;
}
.custom-checkbox > input[type="checkbox"]:focus + label::before {
  border-width: 4px;
  outline: 3px dashed #228bec;
}
.custom-checkbox > label::after {
  box-sizing: content-box;
  content: "";
  position: absolute;
  top: 11px;
  left: 9px;
  width: 18px;
  height: 7px;
  transform: rotate(-45deg);
  border: solid;
  border-width: 0 0 5px 5px;
  border-top-color: transparent;
  opacity: 0;
  background: transparent;
}
.custom-checkbox > input[type="checkbox"]:checked + label::after {
  opacity: 1;
}
@media only screen and (min-width: 40rem) {
  label,
  input,
  .custom-checkbox {
    font-size: 19px;
    font-size: 1.9rem;
    line-height: 1.31579;
  }
}
.btn {
  background-color: #007bff;
  color: white;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.btn__danger {
  background-color: #dc3545;
}
</style>