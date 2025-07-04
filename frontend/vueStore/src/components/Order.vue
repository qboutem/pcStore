<template>
  <div class="bg-white shadow rounded-2xl p-6 w-full sm:w-[48%] lg:w-[31%] flex flex-col">
    <div class="flex-1 flex flex-col justify-between">
      <div>
        <h3 class="text-xl font-bold mb-2">Замовлення №{{ order.id }}</h3>
        <p class="text-gray-500 mb-4">Дата: {{ formattedDate }}</p>

        <div class="grid grid-cols-1 gap-3">
          <div
              v-for="item in order.orderItems"
              :key="item.id"
              class="flex items-center gap-3 bg-gray-50 p-3 rounded-lg"
          >
            <img :src="item.peripheral.imageUrl" alt="Товар" class="w-20 h-20 object-contain" />
            <div>
              <p class="font-semibold">{{ item.peripheral.model }}</p>
              <p class="text-green-600 font-bold">{{ item.peripheral.price }} грн</p>
            </div>
          </div>
        </div>
      </div>

      <div class="text-right mt-4 pt-4 border-t border-gray-200">
        <span class="text-gray-600 font-semibold">Загальна сума:</span>
        <span class="text-xl font-bold text-green-700 ml-2">{{ order.totalPrice }} грн</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  order: {
    id: number;
    createdAt: string;
    totalPrice: number;
    orderItems: {
      id: number;
      peripheral: {
        model: string;
        price: number;
        imageUrl: string;
      };
    }[];
  };
}>();

const order = props.order;
const formattedDate = new Date(order.createdAt).toLocaleString();
</script>