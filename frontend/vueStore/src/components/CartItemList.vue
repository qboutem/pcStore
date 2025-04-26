<script setup>
import CartItem from "@/components/CartItem.vue";
import {inject} from "vue";

const {cart, removeFromCart} = inject('cart')


</script>

<template>
  <div class="flex flex-col flex-1 gap-4 justify-between" v-auto-animate>
    <CartItem v-for="item in cart"
              :key="item.id"
              :title="item.additionalInfo"
              :price="item.price"
              :imageUrl="item.imageUrl"
              @on-click-remove="()=>removeFromCart(item)"
    />
  </div>
</template>

