import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import {useUserStore} from "@/stores/counter.ts";
import {ElMessage} from "element-plus";
import ActivityDetailView from "@/views/ActivityDetailView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      children:[
        {
          path: '/test',
          name: 'test',
          component: () => import('@/views/home/TestView.vue'),
          children:[
              {
                path: '/test/test2',
                name: 'test2',
                component: () => import('@/views/home/TestView2.vue')
              }
          ]
        }
      ]
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
    {
      path: "/activity/:id",
      name: "ActivityDetailView",
      component: ActivityDetailView,
      props: true,
    }

  ],
})

router.beforeEach((to,from,next) =>{
  const userStore = useUserStore()
  console.log(userStore.loading)
  if(!userStore.userInfo && localStorage.getItem('token')){
    console.log("等待获取数据中----")
  }

  console.log(userStore.isAuthenticated+ " ***")
  console.log(userStore.userInfo + " ---")

  const routeName = to.name ? String(to.name) : '';
  console.log(routeName + " to.name")

  if (['login', 'register', 'forgot-password'].includes(to.name as string)) {

    if (userStore.isAuthenticated && userStore.userInfo) {

      ElMessage.info('您已登录，无需重复操作')
      return next({name: 'home'})
    }
  }

  next()
})

export default router
