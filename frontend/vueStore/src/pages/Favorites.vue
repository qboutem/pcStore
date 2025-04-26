<script setup>
  import axios from "axios";
  import {onMounted, ref} from "vue";
  import CardList from "@/components/CardList.vue";

  const emit= defineEmits(['addToFavorite', 'addToCart']);

  const favorites=ref([])
  onMounted(async ()=>{
    try{
      const {data} = await axios.get('http://localhost:8080/favoritesPeripheral',
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`
            }
          });
      favorites.value=data
      console.log(data);
    } catch (err){
      console.log(err)
    }
  })
</script>

<template>
  <h2 class="text-3xl font-bold mb-8">Моє обране</h2>
  <CardList :items="favorites" is-favorites/>
</template>

