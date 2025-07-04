<script setup>
import {ref, watch, provide, computed} from "vue";

import Header from "@/components/Header.vue";
import Drawer from "@/components/Drawer.vue";
import axios from "axios";

const cart = ref([]);

const drawerOpen = ref(false);

const totalPrice = computed(
    () => cart.value.reduce((acc, item) => acc + item.price, 0)
)
const vatPrice = computed(
    () => Math.round((totalPrice.value * 5) / 100)
)


const closeDrawer = () => {
  drawerOpen.value = false
}

const openDrawer = () => {
  drawerOpen.value = true
}

const addToCart = (item) => {
  cart.value.push(item)
  item.isAdded = true
}

const addToFavorite = async (item) => {
  try {
    item.isFavorite = !item.isFavorite;
    if (item.isFavorite) {
      const obj = {
        parentId: item.id,
      };
      const {data} = await axios.post('http://localhost:8080/favorites', obj,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`
            }
          })
      item.favoriteId = data.id;
    } else {
      await axios.delete(`http://localhost:8080/favorites/${item.favoriteId}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`
            }
          })
      item.favoriteId = null;
    }
  } catch (err) {
    console.log(err)
  }
}

const removeFromCart = (item) => {
  cart.value.splice(
      cart.value.indexOf(item), 1
  )
  item.isAdded = false
}

const toggleCartItem = (item) => {
  const index = cart.value.findIndex(cartItem => cartItem.id === item.id)
  if (index === -1) {
    cart.value.push(item)
    item.isAdded = true
  } else {
    cart.value.splice(index, 1)
    item.isAdded = false
  }
}

watch(cart, ()=>{
      localStorage.setItem('cart', JSON.stringify(cart.value))
    },
    {deep:true}
)

provide('cart', {
  cart,
  closeDrawer,
  openDrawer,
  addToCart,
  removeFromCart,
  toggleCartItem,
})

provide('favorites', {
  addToFavorite,
});

</script>

<template>
  <Drawer v-if="drawerOpen"
          :total-price="totalPrice"
          :vat-price="vatPrice"
  />
  <div class="bg-white w-4/5 m-auto rounded-xl shadow-xl mt-14">
    <Header
        v-if="!$route.meta.hideHeader"
        :total-price="totalPrice"
        @open-drawer="openDrawer"/>
    <div class="p-10">
      <router-view></router-view>
    </div>
  </div>
</template>
