// src/router/authGuard.ts
import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/stores/counter.ts'
import { ElMessage } from 'element-plus'


export async function guestOnlyGuard(to: RouteLocationNormalized, from: any, next: NavigationGuardNext) {
    const userStore = useUserStore()

    console.log(userStore.isAuthenticated+ " ***")
    console.log(userStore.userInfo + " ---")

    if (['login', 'register', 'forgot-password'].includes(to.name as string)) {
        if (userStore.isAuthenticated && userStore.userInfo) {
            ElMessage.info('您已登录，无需重复操作')
            return next({name: 'home'})
        }
    }

    next()
}
