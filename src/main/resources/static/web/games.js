 new Vue({
    el:'#myVue',
    data:{
        games : [],
        scores :[],
        state : 0 ,
        loginResult :{},
        username : "jack@gmail.com",
        password : "jack123",
        principal : {},
    },
    methods:{
        init : async function(){
           this.games =  await  fetch('http://localhost:8080/api/games',{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)

           this.scores =  await  fetch('http://localhost:8080/api/leaderboard',{
                             methods: "GET",
                         })
                         .then(res => res.json())
                         .then(data => data)
                         .catch(err => err)

             this.principal =  await  fetch('http://localhost:8080/api/principal',{
                                        methods: "GET",
                                    })
                                    .then(res => res.json())
                                    .then(data => data)
                                    .catch(err => err)

             if(!this.principal.noPrincipal)  {
                this.state = 2 ;
             }
        },
        convertTimestamp:function(t) {
            var date = new Date(t * 1000);
            var formattedDate = ('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2) + '/' + date.getFullYear() + ' ' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            return formattedDate
        },
        logIn: async function(){
           this.state = 1;
           var loginObject = {"username": this.username , "password" : this.password};
           this.loginResult = await  fetch('http://localhost:8080/api/login',{
                                    method: "POST",
                                    body: JSON.stringify(loginObject),
                                    headers: {
                                          'Content-Type': 'application/json'
                                        },
                                })
                                .then(res => res.json())
                                .then(data => data)
                                .catch(err => err)
           console.log(JSON.stringify(this.loginResult));
           if(this.loginResult.result == true){
                 this.state = 2;
           }

        },
    },
    created: function() {
      this.init();
    }
});