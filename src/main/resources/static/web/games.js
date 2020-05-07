 new Vue({
    el:'#myVue',
    data:{
        games : [],
        scores :[],
        state : "mainState",
        loginResult :{},
        signUpResult : {},
        username : "",
        password : "",
        firstName : "",
        lastName : "",
        currentPlayer : {},
        loginErrorMessage : "",
        signUpErrorMessage : "",
        currentGamePlayerId : "",
        gameCreated : {},
        status : "homeStatus",
    },
    methods:{
        getGames:async function(){
           this.games =  await  fetch('/api/games',{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)

            this.games.sort(function(a, b) {
                if (a.creationDate == b.creationDate) { return 0; }
                if (a.creationDate < b.creationDate) { return 1; }
                if (a.creationDate > b.creationDate) { return -1; }
            });
        },
        init : async function(){
          
            this.getGames();
           var arr=  await  fetch('/api/leaderboard',{
                             methods: "GET",
                         })
                         .then(res => res.json())
                         .then(data => data)
                         .catch(err => err)
            this.scores = arr.sort((a, b) => (a.totalScore < b.totalScore) ? 1 : -1);             

             this.currentPlayer =  await  fetch('/api/principal',{
                                                              methods: "GET",
                                                          })
                                                          .then(res => res.json())
                                                          .then(data => data)
                                                          .catch(err => err)

               if(!this.currentPlayer.noPrincipal)  {
                  this.state = "loginedState" ;
               }
        },
        convertTimestamp: function(t) {
            return new Date(t).toLocaleString("en-US");
        },
        logIn: async function(){
           var loginObject = {"username": this.username , "password" : this.password};
           this.loginResult = await  fetch('/api/login',{
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
             this.currentPlayer =  await  fetch('/api/principal',{
                                                   methods: "GET",
                                               })
                                               .then(res => res.json())
                                               .then(data => data)
                                               .catch(err => err)

                        if(!this.currentPlayer.noPrincipal)  {
                           this.state = "loginedState" ;
                           this.status = "gamesStatus";
                        }

            setInterval(function () {  this.getGames(); }.bind(this) ,4000);

        },
        logOut : async function(){
            await  fetch('/api/logout',{
                                        methods: "GET",
                                    })
                                    .then(res => res.json())
                                    .then(data => data)
                                    .catch(err => err)


            this.currentPlayer =  await  fetch('/api/principal',{
                                       methods: "GET",
                                   })
                                   .then(res => res.json())
                                   .then(data => data)
                                   .catch(err => err)

            this.state = "mainState";
        },
        signUp : async function(){
            var signUpObject = {"username": this.username , "password" : this.password , "firstName" : this.firstName, "lastName": this.lastName};
            this.signUpResult = await  fetch('/api/signup',{
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
                 this.status = "loginStatus";
           }else{
            this.signUpErrorMessage = this.signUpResult.message;
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
                    return "/web/game.html?gp="+ gamePlayers[i].id;
                }
            }
            return "";
        },

        createGame : async function(){
            this.gameCreated = await  fetch('/api/games',{
                                method: "POST",
                                body: {},
                                headers: {
                                      'Content-Type': 'application/json'
                                    },
                            })
                            .then(res => res.json())
                            .then(data => data)
                            .catch(err => err)

             this.getGames();

            this.scores =  await  fetch('/api/leaderboard',{
                             methods: "GET",
                         })
                         .then(res => res.json())
                         .then(data => data)
                         .catch(err => err)
        },

        joinGame : async function(gameId){
            await  fetch('/api/game/'+gameId+'/players',{
                            method: "POST",
                            body:{},
                            headers: {
                                  'Content-Type': 'application/json'
                                },
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)

            this.getGames();
        },
    },
    created: function() {
      this.init();
    }
});