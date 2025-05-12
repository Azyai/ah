import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import RegisterView from "@/views/user/RegisterView.vue";
import LoginView from "@/views/user/LoginView.vue";
import ForgotPasswordView from "@/views/user/ForgotPasswordView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: "/forgot-password",
      name: "forgot-password",
      component: ForgotPasswordView
    },

  ],
})

export default router
