import {createStore} from 'vuex'

declare let SessionStorage: any; //seesion-storage.js
const USER = "USER";
//全局变量  异步 同步
const store = createStore({
    state: {
        user: SessionStorage.get(USER) || {} //避免空指针
    },
    mutations: {
        setUser(state, user) {
            console.log("store user：", user);
            state.user = user;
            SessionStorage.set(USER, user);
        }
    },
    actions: {},
    modules: {}
});

export default store;
