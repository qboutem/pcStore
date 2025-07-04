<script setup>
const emit=defineEmits(['onClickRemove'])
import { RouterLink } from 'vue-router'

defineProps({
  id: Number,
  imageUrl: String,
  title: String,
  price: Number,

})
</script>

<template>
  <div class="flex items-center border border-slate-200 p-4 rounded-xl gap-4">
    <img class="w-16 h-16" :src="imageUrl" alt="Peripheral">
    <div class="flex flex-col flex-1">
      <p>{{title}}</p>
      <div class="flex justify-between  mt-2">
        <b class="flex-1">{{price}} UAH.</b>
        <img @click="emit('onClickRemove')" class="opacity-40 hover:op-100 cursor-pointer" src="/close.svg" alt="close"/>
      </div>
    </div>
  </div>
</template>

