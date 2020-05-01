 new Vue({
    el:'#myVue',
    data:{
        games : [],
        scores :[],
        state : "mainState",
        loginResult :{},
        signUpResult : {},
        username : "jack@gmail.com",
        password : "jack123",
        firstName : "",
        lastName : "",
        currentPlayer : {},
        loginErrorMessage : "",
        currentGamePlayerId : "",
        gameCreated : {},
        status : "homeStatus",
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

             this.currentPlayer =  await  fetch('http://localhost:8080/api/principal',{
                                                              methods: "GET",
                                                          })
                                                          .then(res => res.json())
                                                          .then(data => data)
                                                          .catch(err => err)

               if(!this.currentPlayer.noPrincipal)  {
                  this.state = "loginedState" ;
               }
        },
        convertTimestamp:function(t) {
            var date = new Date(t *1000);
            var formattedDate = ('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2) + '/' + date.getFullYear() + ' ' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            return formattedDate
        },
        logIn: async function(){
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
                 this.state = "loginedState";
           }else{
            this.loginErrorMessage = this.loginResult.message;
           }
             this.currentPlayer =  await  fetch('http://localhost:8080/api/principal',{
                                                   methods: "GET",
                                               })
                                               .then(res => res.json())
                                               .then(data => data)
                                               .catch(err => err)

                        if(!this.currentPlayer.noPrincipal)  {
                           this.state = "loginedState" ;
                           this.status = "gamesStatus";
                        }
        },
        logOut : async function(){
            this.state = "mainState";
            await  fetch('http://localhost:8080/api/logout',{
                                        methods: "GET",
                                    })
                                    .then(res => res.json())
                                    .then(data => data)
                                    .catch(err => err)


            this.currentPlayer =  await  fetch('http://localhost:8080/api/principal',{
                                                               methods: "GET",
                                                           })
                                                           .then(res => res.json())
                                                           .then(data => data)
                                                           .catch(err => err)
        },
        signUp : async function(){
//            this.state = "loginState";
            var signUpObject = {"username": this.username , "password" : this.password , "firstName" : this.firstName, "lastName": this.lastName};
            this.signUpResult = await  fetch('http://localhost:8080/api/signup',{
                                    method: "POST",
                                    body: JSON.stringify(signUpObject),
                                    headers: {
                                          'Content-Type': 'application/json'
                                        },
                                })
                                .then(res => res.json())
                                .then(data => data)
                                .catch(err => err)
           console.log(JSON.stringify(this.signUpResult));
           if(this.signUpResult.result == true){
                 this.state = "loginState";
           }
        },

        isCurrentUser : function(gamePlayers){
            if(!this.currentPlayer.id){
                return false;
            }
            for(var i = 0 ; i < gamePlayers.length ; i++){
                console.log(this.currentPlayer.id +" " + gamePlayers[i].player.id );
                if(this.currentPlayer.id == gamePlayers[i].player.id){
                    this.currentGamePlayerId = gamePlayers[i].id;
                   return true;
                }
            }
            return false;
        },

        getGamePlayerLink : function(gamePlayers){
            for(var i = 0 ; i < gamePlayers.length ; i++){
                if(gamePlayers[i].player.email == this.currentPlayer.username) {
                    return "http://localhost:8080/web/game.html?gp="+ gamePlayers[i].id;
                }
            }
            return "";
        },

        createGame : async function(){
            this.gameCreated = await  fetch('http://localhost:8080/api/games',{
                                method: "POST",
                                body: {},
                                headers: {
                                      'Content-Type': 'application/json'
                                    },
                            })
                            .then(res => res.json())
                            .then(data => data)
                            .catch(err => err)

            this.games =  await  fetch('http://localhost:8080/api/games',{
                                    methods: "GET",
                                })
                                .then(res => res.json())
                                .then(data => data)
                                .catch(err => err)

        },

        joinGame : async function(gameId){
            await  fetch('http://localhost:8080/api/game/'+gameId+'/players',{
                            method: "POST",
                            body:{},
                            headers: {
                                  'Content-Type': 'application/json'
                                },
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)

            this.games =  await  fetch('http://localhost:8080/api/games',{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)
        },
    },
    created: function() {
      this.init();
    }
});