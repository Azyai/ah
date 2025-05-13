import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { guestOnlyGuard } from './guard/authGuard'

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
      component: () => import('@/views/user/RegisterView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/user/LoginView.vue')
    },
    {
      path: "/forgot-password",
      name: "forgot-password",
      component: () => import('@/views/user/ForgotPasswordView.vue')
    },

  ],
})
router.beforeEach(guestOnlyGuard)

export default router
