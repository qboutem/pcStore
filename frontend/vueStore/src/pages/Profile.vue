p<script setup>
import axios from "axios";
import {useRouter} from "vue-router";
import {onMounted, reactive, ref} from "vue";
import Order from "@/components/Order.vue";

const router = useRouter();

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/logoutacc');
    localStorage.removeItem('token');
    await router.push('/');
  } catch (error) {
    console.error(error);
  }
};

  const user = reactive({
    name: '',
    surname:'',
    email: '',
    avatar: '',
    phoneNumber: '',
    location: '',
    birthDate:'',
});

  const orders = ref([]);

  onMounted(async () => {
  try {
    const token = localStorage.getItem('token');
    const responseUser = await axios.get('http://localhost:8080/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log(responseUser);
    user.name=responseUser.data.name;
    user.email=responseUser.data.email;
    user.surname=responseUser.data.surname;
    user.phoneNumber=responseUser.data.phoneNumber;
    user.location=responseUser.data.location;
    user.birthDate=responseUser.data.birthDate;
    user.avatar='https://i.pravatar.cc/150?img=3';

    const responseOrders = await axios.get('http://localhost:8080/getOrders', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log(responseOrders);

    orders.value = responseOrders.data;
  } catch (err) {
  console.error(err);
}
});
</script>

<template>
  <div class="p-8 flex flex-col md:flex-row gap-8 w-full">
    <div class="bg-white shadow rounded-2xl p-6 flex flex-col md:w-1/4 w-full">
      <div class="flex justify-center mb-4">
        <img src="/kudi.jpg" alt="Avatar" class="w-32 h-32 rounded-full object-cover shadow mb-4 text-align: center" />
      </div>
      <h1 class="text-2xl font-bold">Привіт, {{ user.name }} {{ user.surname }} !</h1>
      <p class="text-gray-500">Email: {{ user.email }}</p>
      <p class="text-gray-500">Номер телефону: {{ user.phoneNumber }}</p>
      <p class="text-gray-500">Дата: {{ user.birthDate }}</p>
      <p class="text-gray-500">Локація: {{ user.location }}</p>
      <button class="mt-4 transition hover:bg-red-900 bg-red-500 hover:bg-red-600 text-white font-semibold py-2 px-4 rounded-xl transition duration-300 hover" @click="logout">Вийти</button>
    </div>
    
    <div class="flex-1 flex flex-wrap gap-6">
      <Order
          v-for="order in orders"
          :key="order.id"
          :order="order"
      />
    </div>
  </div>
</template>

<style scoped>
body {
  background-color: #f9f9f9;
}
</style>
