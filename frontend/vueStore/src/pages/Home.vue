<script setup>
import CardList from "../components/CardList.vue";
import axios from "axios";
import {onMounted, reactive, ref, watch} from "vue";
import {inject} from "vue";


const {cart, addToCart, removeFromCart} = inject('cart')
const items = ref([]);

const filters = reactive({
  sortBy: 'additionalInfo',
  order: '',
})

const onChangeSelect = event => {
  filters.sortBy = event.target.value;
}

const onChangeSearchInput = event => {
  filters.order = event.target.value;
}

const onClickAddPlus = (item) => {
  if (!item.isAdded) {
    addToCart(item);
  } else {
    removeFromCart(item);
  }
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
      console.log(data);
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

const fetchItems = async () => {
  try {
    const params = {
      sortBy: filters.sortBy,
    }
    if (filters.order) {
      params.additionalInfo = `${filters.order}`;
    }

    const {data} = await axios.get('http://localhost:8080/getAllPeripheral', {
      params
    })

    items.value = data.map(obj => ({
      ...obj,
      isFavorite: false,
      favoriteId: null,
      isAdded: false
    }))
  } catch (err) {
    console.log(err)
  }
}

const fetchFavorites = async () => {
  try {
    const {data: favorites} = await axios.get('http://localhost:8080/favorites',
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        })
    items.value = items.value.map(item => {
      const favorite = favorites.find(fav => fav.parentId === item.id);

      if (!favorite) {
        return item;
      }

      return {
        ...item,
        isFavorite: true,
        favoriteId: favorite.id,
      };
    })
  } catch (err) {
    console.log(err)
  }
}

onMounted(async () => {
  const localCart = localStorage.getItem('cart')
  cart.value = localCart ? JSON.parse(localCart) : []

  await fetchItems();
  await fetchFavorites();

  items.value = items.value.map((item) => ({
    ...item,
    isAdded: cart.value.some((cartItem) => cartItem.id === item.id)
  }))
})

watch(filters, async () => {
  const localCart = localStorage.getItem('cart')
  cart.value = localCart ? JSON.parse(localCart) : []

  await fetchItems();
  await fetchFavorites();

  items.value = items.value.map((item) => ({
    ...item,
    isAdded: cart.value.some((cartItem) => cartItem.id === item.id)
  }))
});

watch(cart, () => {
  items.value = items.value.map((item) => ({
    ...item,
    isAdded: false
  }))
})

</script>

<template>
  <div class="flex justify-between items-center">
    <h2 class="text-3xl font-bold mb-8">Каталог</h2>
    <div class="flex gap-4">
      <select @change="onChangeSelect"
              class="py-2 px-3 border rounded-md outline-none">
        <option value="additionalInfo">Назва</option>
        <option value="price">Від дешевих..</option>
        <option value="-price">Від дорогих..</option>
      </select>
      <div class="relative">
        <img class="absolute left-3 top-3"
             src="/search.svg"
             alt="Search"/>
        <input
            @input="onChangeSearchInput"
            class="border rounded-md py-2 pl-11 pr-4 outline-none focus:border-gray-400"
            type="text"
            placeholder="Пошук..."
        >
      </div>
    </div>
  </div>
  <div class="mt-10">
    <CardList :items="items"
              @add-to-favorite="addToFavorite"
              @add-to-cart="onClickAddPlus"/>
  </div>
</template>
