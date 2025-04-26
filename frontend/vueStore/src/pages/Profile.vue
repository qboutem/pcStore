p<script setup>
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/logoutacc');
    localStorage.removeItem('token');
    await router.push('/');
  } catch (error) {
    console.error(error);
    alert('AAA');
  }
};
</script>

<template>
  <h2 @click="logout">LOX</h2>
</template>
