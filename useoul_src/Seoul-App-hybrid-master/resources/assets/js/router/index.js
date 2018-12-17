/*********************************

*********************************/
import Vue from 'vue';
import Router from 'vue-router';

import Home from '../components/Home';
import Servicies from '../components/Servicies';
import Service from '../components/Service';
import Bookmark from '../components/Bookmark';

Vue.use(Router);

export default new Router({
  mode: 'history',
        routes: [
            {
              path: '/',
              name: 'Home',
              component: Home

            },
            {
              path: '/services',
              name: 'Services',
              component: Servicies
            },
            {
                path: '/service',
                name: 'service',
                component: Service
            },
            {
                path: '/bookmark',
                name: 'bookmark',
                component: Bookmark
            }
        ],
        scrollBehavior (to, from, savedPosition) {
            return { x: 0, y: 0 }
        }
});
