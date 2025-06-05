import { createRouter, createWebHistory } from 'vue-router'
import LandingView from '../views/LandingView.vue' 
import { useAuth } from '@/api/useAuth'
// import HomeView from '../views/HomeView.vue'
import HomeViewNew from '@/views/HomeView.vue' 
import ObjMainPage from '@/components/ObjHub/ObjMainPage.vue'
import AnalysisView from '@/views/AnalysisView.vue'
import UserCenterView from '@/views/UserCenterView.vue'
import PeerMainPage from '@/components/PeerHub/PeerMainPage.vue'
import ResourceHubView from '@/views/ResourceHubView.vue'
import DashBoard from '@/views/DashBoard.vue'
import InkedCursor from '@/components/Common/InkedCursor.vue'
import test from '@/components/Common/test.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "landing",
      component: LandingView,
    },
    {
      path: "/login",
      name: "login",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/LoginView.vue"),
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
    {
      path: "/nothing",
      name: "nothing",
      component: () => import("../views/NothingView.vue"),
    },
    {
      path: "/home",
      name: "home",
      // component: HomeView,
      component: HomeViewNew,
      meta: { requiresAuth: false },
      children: [
        {
          path: "dashboard",
          name: "homePage",
          component: DashBoard
        },
        {
          path: "objMain",
          name: "objMain",
          component: ObjMainPage,
        },
        {
          path: "analysis",
          name: "analysis",
          component: AnalysisView,
        },
        {
          path: "peerhub",
          name: "peerhub",
          component: PeerMainPage,
        },
        {
          path: "resourcehub",
          name: "resourcehub",
          component: ResourceHubView,
        },
      ],
    },
    {
      path: "/user",
      name: "user",
      component: UserCenterView,
      meta: { requiresAuth: true },
    },
    {
      path: "/test",
      name: "test",
      component: test,
      meta: { requiresAuth: false },
    },  
    {
      path: "/ink",
      name: "ink",
      component: InkedCursor,
      meta: { requiresAuth: false },
    }  
  ],
});

router.beforeEach((to, from, next) => {
  const { loading, errorMessage } = useAuth();
  const isAuthenticated = !!localStorage.getItem("token");

  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/");
  } else {
    next();
  }
});

export default router;
