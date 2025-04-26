<script setup>
import { ref, reactive } from 'vue'
import axios from "axios";
import {useRouter} from "vue-router";

const isLogin = ref(true)
const router = useRouter();

const form = reactive({
  email: '',
  password: '',
  confirmPassword: '',
  name: '',
  surname: '',
  location: '',
  birthDate: '',
  phoneNumber: '',
})

async function handleSubmit() {
  if (!isLogin.value && form.password !== form.confirmPassword) {
    alert('Паролі не співпадають')
    return
  }
  const url = isLogin.value ? 'http://localhost:8080/auth' : 'http://localhost:8080/register'

  const payload = isLogin.value
      ? {
        email: form.email,
        password: form.password,
      }
      : {
        email: form.email,
        password: form.password,
        name: form.name,
        surname: form.surname,
        location: form.location,
        birthDate: form.birthDate,
        phoneNumber: form.phoneNumber,
      }

  try {
    const response = await axios.post(url, payload)
    console.log(response.data)

    if (response.data.token) {
      localStorage.setItem('token', response.data.token)
    }
    await router.push('/home');
  } catch (error) {
    console.error(error)
    alert(`Помилка: ${error.response?.data?.message || error.message}`)
  }
}

</script>

<template>
  <div class=" absolute inset-0 bg-[#faffeb] flex justify-center items-center z-50">
    <div class="bg-white shadow-lg rounded-2xl w-full max-w-md p-6">
      <h2 class="text-2xl font-semibold text-center mb-6">
        {{ isLogin ? 'Авторизація' : 'Реєстрація' }}
      </h2>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium mb-1">Email</label>
          <input type="email" v-model="form.email" required class="input outline-1 w-full" />
        </div>

        <div>
          <label class="block text-sm font-medium mb-1">Пароль</label>
          <input type="password" v-model="form.password" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Підтвердіть пароль</label>
          <input type="password" v-model="form.confirmPassword" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Ім’я</label>
          <input type="text" v-model="form.name" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Прізвище</label>
          <input type="text" v-model="form.surname" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Локація</label>
          <input type="text" v-model="form.location" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Дата народження</label>
          <input type="date" v-model="form.birthDate" required class="input outline-1 w-full" />
        </div>

        <div v-if="!isLogin">
          <label class="block text-sm font-medium mb-1">Номер телефону</label>
          <input
              type="tel"
              v-model="form.phoneNumber"
              placeholder="+380XXXXXXXXX"
              class="input"
          />
        </div>

        <button
            type="submit"
            class="w-full bg-green-500 hover:bg-green-600 text-white font-semibold py-2 rounded-xl transition duration-300"
        >
          {{ isLogin ? 'Увійти' : 'Зареєструватися' }}
        </button>
      </form>

      <div class="text-center mt-4">
        <button @click="isLogin = !isLogin" class="text-sm text-gray-600 hover:underline">
          {{ isLogin ? 'Ще не маєте акаунту? Зареєструватися' : 'Вже маєте акаунт? Увійти' }}
        </button>
      </div>
    </div>
  </div>
</template>

