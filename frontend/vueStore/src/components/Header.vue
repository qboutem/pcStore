<script setup>
import {inject} from "vue";

const emit=defineEmits(['openDrawer']);
const {closeDrawer}=inject('cart');

defineProps({
  totalPrice: Number
})
</script>


<template>
  <header class="flex justify-between border-b border-slate-200 px-8 py-8">
    <RouterLink to="/home">
    <div class="flex items-center gap-4">
      <img src="/logo.svg" alt="Logo" class="w-10"/>
      <div>
        <h2 class="text-xl font-bold uppercase">PC Store</h2>
        <p class="text-slate-400">Магазин комп'ютерної периферії</p>
      </div>
    </div>
    </RouterLink>
    <ul class="flex items-center gap-10">
      <li @click="()=>emit('openDrawer')" class="flex items-center cursor-pointer gap-3 text-gray-500 hover:text-black">
        <img src="/cart.svg" alt="Cart"/>
        <span>{{totalPrice}} UAH</span>
      </li>
      <RouterLink to="/favorites">
      <li class="flex items-center cursor-pointer gap-3 text-gray-500 hover:text-black">
        <img src="/heart.svg" alt="Heart"/>
        <span>Обране</span>
      </li>
      </RouterLink>
      <RouterLink to="/profile">
      <li class="flex items-center cursor-pointer gap-3 text-gray-500 hover:text-black">
        <img src="/profile.svg" alt="Profile"/>
        <span>Профіль</span>
      </li>
      </RouterLink>
    </ul>
  </header>
</template>
