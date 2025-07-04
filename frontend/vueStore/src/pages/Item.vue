<script setup>
import {ref, onMounted, inject} from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const product = ref({});

const { cart,toggleCartItem } = inject('cart');

const handleAddToFavorite = async () => {
  try {
    await toggleFavorite(product.value);
  } catch (err) {
    console.error(err);
  }
};

const toggleFavorite = async (item) => {
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
      console.log(item.favoriteId)
      return {
        isFavorite: item.isFavorite,
        favoriteId: item.favoriteId
      };
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
    console.log(err);
  }
};

onMounted(async () => {
  try {
    const favoritesResponse = await axios.get('http://localhost:8080/favorites',
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        })
    const favorites = favoritesResponse.data;

    const { data } = await axios.get(`http://localhost:8080/${route.params.id}`);

    const isInCart = cart.value.some(i => i.id === data.id);

    const favoriteItem = favorites.find(fav => Number(fav.parentId) === Number(data.id));

    Object.assign(product.value, {
      ...data,
      isAdded: isInCart,
      isFavorite: !!favoriteItem,
      favoriteId: favoriteItem?.id || null
    });
  } catch (err) {
    console.error(err);
  }
});

const handleAddToCart = () => {
  toggleCartItem(product.value);
};


</script>

<style scoped>
img {
  transition: transform 0.2s ease;
}
img:hover {
  transform: scale(1.05);
}
</style>

<template>
  <div class="max-w-7xl ml-0 p-8">
    <div class="flex flex-col md:flex-row gap-8">

      <div class="flex-1">
        <img :src="product.imageUrl" alt="Product Image" class="w-full rounded-xl shadow-lg" />
      </div>

      <div class="flex-1 space-y-4">
        <h1 class="text-3xl font-bold">{{ product.brand }} {{ product.model }}</h1>
        <p class="text-gray-600">{{ product.additionalInfo }}</p>

        <div class="text-2xl font-semibold text-green-600">
          {{ product.price }} грн
        </div>

        <div class="flex gap-4 mt-4">
          <button @click="handleAddToCart"
                  :class="product.isAdded ? 'bg-red-500 hover:bg-red-600' : 'bg-blue-600 hover:bg-blue-700'"
                  class="text-white px-6 py-2 rounded-xl">
            {{ product.isAdded ? 'Видалити з кошика' : 'Додати до кошика' }}
          </button>
          <button @click="handleAddToFavorite"
                  :class="product.isFavorite ? 'bg-gray-400 hover:bg-gray-500' : 'bg-pink-500 hover:bg-pink-600'"
                  class="text-white px-6 py-2 rounded-xl">
            {{ product.isFavorite ? 'Видалити з обраного' : 'Додати до обраного' }}
          </button>
        </div>

        <div class="mt-6">
          <h2 class="text-xl font-semibold mb-2">Характеристики</h2>
          <ul class="list-disc list-inside text-gray-700 space-y-1">
            <li><strong>Бренд:</strong> {{ product.brand }}</li>
            <li><strong>Модель:</strong> {{ product.model }}</li>
            <li><strong>Колір:</strong> {{ product.color }}</li>
          </ul>
        </div>

      </div>
    </div>
  </div>
</template>

