<template>
    <img :src="avatarUrl" :alt="altText" class="rounded-full" />
</template>

<script>
export default {
name: 'DummyAvatar',
props: {
    text: {
    type: String,
    default: 'V'
    },
    size: {
    type: Number,
    default: 50
    },
    bgColor: {
    type: String,
    default: '666'
    },
    textColor: {
    type: String,
    default: 'fff'
    }
},
computed: {
    avatarUrl() {
    return `https://dummyimage.com/${this.size}x${this.size}/${this.bgColor}/${this.textColor}.png&text=${this.text}`;
    },
    altText() {
    return `Avatar with text ${this.text}`;
    }
}
};
</script>

<style scoped>
img {
display: block;
}
</style>

<!--使用示例-->
<!-- <DummyAvatar text="A" size="100" bgColor="333" textColor="fff" /> -->
 
