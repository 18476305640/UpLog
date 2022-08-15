import './jquery-3.3.1.min.js'
import './cookie-utils.js'
window.data = {
    state: {},
    methods:{
        isAuth() {
            var user=$.getCookie("user");
            var scode=$.getCookie("scode");
            if(user!=null && scode != null){
                return true
            }else{
                return false
            }
        }
    },
}
