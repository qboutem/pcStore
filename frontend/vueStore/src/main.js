import './assets/main.css'


import { createApp } from 'vue'
import {createRouter, createWebHistory} from 'vue-router'
import { createPinia } from 'pinia'
import {autoAnimatePlugin} from "@formkit/auto-animate/vue";
import App from './App.vue'

/*import page*/
import Favorites from "@/pages/Favorites.vue";
import Home from "@/pages/Home.vue";
import Profile from "@/pages/Profile.vue";
import Authorization from "@/pages/Authorization.vue";
import NotFound from "@/pages/NotFound.vue";
import Item from "@/pages/Item.vue";

const isAuthenticated = () => {
    return !!localStorage.getItem('token');
};

/*make path to page*/
const routes = [
    { path: '/home', name: 'home', component: Home, beforeEnter: (to, from, next) => {
            if (isAuthenticated()) {
                next();
            } else {
                next('/');
            }
        } },
    { path: '/favorites', name: 'favorites', component: Favorites, beforeEnter: (to, from, next) => {
            if (isAuthenticated()) {
                next();
            } else {
                next('/');
            }
        } },
    { path: '/profile', name: 'profile', component: Profile, beforeEnter: (to, from, next) => {
            if (isAuthenticated()) {
                next();
            } else {
                next('/');
            }
        } },
    { path: '/:id', name: 'item', component: Item, beforeEnter: (to, from, next) => {
            if (isAuthenticated()) {
                next();
            } else {
                next('/');
            }
        } },
    { path: '/', name: 'authorization', component: Authorization, meta: { hideHeader: true},  },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

const app=createApp(App);
app.use(autoAnimatePlugin)
app.use(createPinia())
app.use(router)
app.mount('#app')
