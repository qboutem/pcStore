<script setup>
import Card from "./Card.vue";

defineProps({
  items:Array,
  isFavorites: Boolean
})
const onClickAdd =() =>{
  alert('Add');
}

const emit= defineEmits(['addToFavorite', 'addToCart']);

</script>

<template>
  <div class="grid grid-cols-4 gap-5" v-auto-animate>
    <Card
        v-for="item in items"
        :id="item.id"
        :key="item.id"
        :price="item.price"
        :imageUrl="item.imageUrl"
        :title="item.additionalInfo"
        :on-click-add="isFavorites ? null: ()=>emit('addToCart', item)"
        :on-click-favorite="isFavorites ? null: () => emit('addToFavorite', item)"
        :is-favorite="item.isFavorite"
        :is-added="item.isAdded"
    />
  </div>
</template>

