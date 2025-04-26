<script setup>
import {inject} from 'vue'

const props=defineProps({
  id: Number,
  imageUrl: String,
  title: String,
  price: Number,
  isFavorite: Boolean,
  isAdded: Boolean,
  onClickAdd: Function,
  onClickFavorite: Function
})
const visibleFavoriteButton=Boolean(props.onClickAdd)
</script>

<template>
  <div class="relative m1-0 bg-white border-slate-100 rounded-3xl p-0 cursor-pointer transition hover:-translate-y-2 hover:shadow-xl flex flex-col justify-between h-full">
    <img
        v-if="visibleFavoriteButton"
        :src="!isFavorite ? '/like-1.svg' : '/like-2.svg'"
         alt="Like 1"
         class="absolute"
         @click="onClickFavorite"
    >
    <img :src="imageUrl" alt="Peripheral" class="w-full object-contain"/>
    <p class="mt-2"> {{title}}</p>
      <div class="flex justify-between items-center mt-5">
        <div class="flex flex-col">
          <span class="text-state-400 text-gray-500 ">Ціна:</span>
          <p class="font-bold ">{{ price }} UAH.</p>
        </div>
      <img
          v-if="visibleFavoriteButton"
          :src="isAdded ? '/checked.svg': '/plus.svg'"
           alt="Plus"
           class="cursor-pointer"
           @click="onClickAdd"
      >
      </div>
  </div>
</template>

