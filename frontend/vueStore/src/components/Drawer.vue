<script setup>
import DrawerHead from "@/components/DrawerHead.vue";
import CartItemList from "@/components/CartItemList.vue";
import InfoBlock from "@/components/infoBlock.vue";
import {computed, inject, ref} from "vue";
import axios from "axios";

const props=defineProps({
  totalPrice: Number,
  vatPrice: Number,
})

const {cart, closeDrawer}=inject('cart')
const isCreating = ref(false)
const orderId=ref(null)

const createOrder = async () => {
  try {
    isCreating.value = true;
    const itemIds = cart.value.map(item => item.id);
    const response = await axios.post('http://localhost:8080/orders', {
      items: itemIds,
      totalPrice: props.totalPrice
    },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        }
    );
    cart.value = [];
    console.log(response.data)
    orderId.value= response.data.id;
    return response.data;
  } catch (err) {
    console.log(err)
  } finally {
    isCreating.value = false
  }
}

const ButtonDisabled=computed(
    ()=>isCreating.value || cartIsEmpty.value
)
const cartIsEmpty=computed(
    ()=>cart.value.length===0
)
</script>

<template>
  <div @click="closeDrawer" class="fixed top-0 left-0 h-full w-full bg-black z-10 opacity-50"></div>
  <div class="bg-white w-96 h-full fixed right-0 top-0 z-20 p-8">
    <DrawerHead/>
    <div v-if="cartIsEmpty || orderId" class="flex h-full items-center">
      <InfoBlock
          v-if="cartIsEmpty && !orderId"
          title="Кошик порожній"
          description="Додайте товар, щоб зробити замовлення"
          image-url="/package-icon.png"/>
      <InfoBlock
          v-if="orderId"
          title="Замовлення оформлено"
          :description="`Ваше замовлення #${orderId} прийняте! Очікуйте найближчим часом.`"
          image-url="/order-success-icon.png"/>
    </div>

    <CartItemList v-if="totalPrice"/>

    <div v-if="totalPrice"
         class="flex flex-col gap-2 m-1 mt-7">
      <div class="flex gap-2 items-center">
        <span>Всього:</span>
        <div class="flex-1"></div>
        <b>{{ totalPrice }} UAH</b>
      </div>

      <div class="flex gap-2 items-center">
        <span>Комісія:</span>
        <div class="flex-1"></div>
        <b>{{ vatPrice }} UAH</b>
      </div>

      <button :disabled="ButtonDisabled"
              @click="createOrder"
              class="transition bg-lime-500 w-full rounded-xl py-3 disabled:bg-slate-300 text-white hover:bg-lime-600 active:bg-lime-700 cursor-pointer">
        Оформить заказ
      </button>
    </div>
  </div>
</template>

