  var router = new VueRouter({
    mode: 'history',
    routes: [],
  });
 const api =  new Vue({
      router,
     el:"#myVue",
     data:{
     game : [],
     rows :['','1','2','3','4','5','6','7','8','9','10'],
     columns :['A','B','C','D','E','F','G','H','I','J'],
     parameter :'',
     currentPlayer : {},
     currentOpponent : {},
     currentGamePlayer : {},
     shipTypeValue : "",
     shipDirection: "",
     startingPoint: "",
     placingShipObject : {},
     shipType : "",
     shipLocations : [],
     shipLength : 0,
     currentGamePlayerId : "",
     exceptionMessage : "",
     state : "choosingShips",
     salvoLocations : [],
     hitsOnMe : [],
     hitsOnOpp: [],
     playerCarrier : 0,
     playerBattleship : 0,
     playerDestroyer : 0,
     playerSubmarine : 0,
     playerPatrolboat : 0,
     opponentCarrier : 0,
     opponentBattleship : 0,
     opponentDestroyer : 0,
     opponentSubmarine : 0,
     opponentPatrolboat : 0,
  },
     methods:{
         updateGrid : async function(){
                this.game = await  fetch('http://localhost:8080/api/game_view/'+this.parameter.gp,{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)


                for( var i = 0 ; i <  this.game[0].ships.length ; i++){
                        var locs = this.game[0].ships[i].shipLocations;
                        for(var j = 0 ; j< locs.length ; j++){
                            this.$refs["P" + locs[j]][0].style.backgroundColor = '#bfe8e9';
                        }
                }
                for(var k = 0 ; k < this.game[0].salvos.length ; k++){
                    var fireLocations = this.game[0].salvos[k].salvoLocations
                    for(var l = 0; l < fireLocations.length ; l++){
                        if(this.game[0].salvos[k].playerId == this.currentPlayer.id){
                            this.$refs["O" + fireLocations[l]][0].style.backgroundColor ='orange';
                            this.$refs["O" + fireLocations[l]][0].innerHTML = this.game[0].salvos[k].turn ;
                        }else{
                            this.$refs["P" + fireLocations[l]][0].style.backgroundColor ='orange';
                            this.$refs["P" + fireLocations[l]][0].innerHTML = this.game[0].salvos[k].turn ;
                        }
                        if(this.game[0].ships.includes(fireLocations[l])){
                            console.log("fireLocation is:"+fireLocations);
                            this.$refs["O" + fireLocations[l]][0].style.backgroundColor ='red';
                            this.$refs["P" + fireLocations[l]][0].style.backgroundColor ='red';
                        }
                    }
                }
                 var gameplayers = this.game[0].gamePlayers;
                for(i = 0; i < gameplayers.length; i++){
                    if(gameplayers[i].id == this.currentGamePlayerId){
                        this.currentGamePlayer = gameplayers[i].player;
                    } else {
                        this.currentOpponent = gameplayers[i].player;
                        console.log(gameplayers[i].player);

                    }
                }

             this.getHits();
         },

         init : async  function(){
            this.updateGrid();
            this.currentPlayer =  await  fetch('http://localhost:8080/api/principal',{
                                       methods: "GET",
                                   })
                                   .then(res => res.json())
                                   .then(data => data)
                                   .catch(err => err);

            console.log(this.currentPlayer.firstName);                       
            this.getState();
            setInterval(function () { this.getState(); }.bind(this) ,10000);
            setInterval(function () { if (this.state == "waiting") this.updateGrid(); }.bind(this) ,2000);

         },

         getShipTypeValue : function(event){
           this.shipTypeValue = event.target.value;
           if(this.shipTypeValue == 1){
                this.shipType = "Carrier";
                this.shipLength = 5;
           }else if(this.shipTypeValue == 2){
                this.shipType = "Battleship";
                this.shipLength = 4;
           }else if(this.shipTypeValue == 3){
                this.shipType = "Submarine";
                this.shipLength = 3;
           }
           else if(this.shipTypeValue == 4){
                 this.shipType = "Destroyer";
                 this.shipLength = 3;
           }else if(this.shipTypeValue == 5){
                 this.shipType = "Patrol Boat";
                 this.shipLength = 2;
           }
         },

         cellSelected : function(startingPoint){
            this.startingPoint = startingPoint;
         },

        getURLParameter : function (sParam){
            var sPageURL = window.location.search.substring(1);
            var sURLVariables = sPageURL.split('&');
            for (var i = 0; i < sURLVariables.length; i++){
                var sParameterName = sURLVariables[i].split('=');
                if (sParameterName[0] == sParam){
                    return sParameterName[1];
                }
            }
        },

         placingShips : async function(){
            this.exceptionMessage = "";
            this.shipLocations = [];
            if(this.shipDirection == "Horizontal"){
                var start = this.startingPoint[1];
                for(var i = this.startingPoint[1] ;i < this.shipLength + Number(start) ; i++){
                    if(Number(start)  + this.shipLength <= this.rows.length){
                        this.shipLocations.push(this.startingPoint[0]+i);
                    }else{
                            this.exceptionMessage = "There ia a ships extending beyond the borders of the grid, Please select another place!";
                    }
                }
            }else if(this.shipDirection == "Vertical"){
                for(var j = 0 ; j < this.columns.length ; j++){
                    if(this.startingPoint[0] == this.columns[j]) {
                        var start = j;
                    }
                    if(j < this.shipLength + start){
                        if(this.shipLength + start <= this.columns.length){
                            if(this.startingPoint.length < 3){
                                this.shipLocations.push(this.columns[j]+this.startingPoint[1]);
                            }else{
                                this.shipLocations.push(this.columns[j]+this.startingPoint[1]+this.startingPoint[2]);
                                 }
                        }else {
                            this.exceptionMessage = "There ia a ships extending beyond the borders of the grid, Please select another place!"
                        }
                    }

                }
            }
            console.log(this.startingPoint);
            console.log(this.shipType);
            console.log(this.shipLocations);
            this.placingShipObject = {shipType: this.shipType, shipLocations: this.shipLocations};
            this.currentGamePlayerId = this.getURLParameter('gp');
            console.log(this.currentGamePlayerId);

            var result = await fetch('http://localhost:8080/api/games/players/'+this.currentGamePlayerId+'/ships',{
                                method: "POST",
                                body:JSON.stringify(this.placingShipObject),
                                headers: {
                                      'Content-Type': 'application/json'
                                    },
                            })
                            .then(res => res.json())
                            .then(data => data)
                            .catch(err => err)

            console.log(result.shipCounter);

            if(result.result){
               this. updateGrid();;
            }else{
                this.exceptionMessage = result.message;
            }

            this.startingPoint = "";
            this.shipDirection = "Select";

            this.getState();

         },

         shotSelected : function(cell){
             for(var i = 0; i <  this.game[0].salvos.length; i++){
                for(var j = 0; j < this.game[0].salvos[i].salvoLocations.length; j++){
                    if(cell == this.game[0].salvos[i].salvoLocations[j] &&
                        this.currentPlayer.id == this.game[0].salvos[i].playerId){
                        return;
                    }
                }
             }
             for(var i=0; i < this.salvoLocations.length; i++){
                if(cell == this.salvoLocations[i]){
                    this.salvoLocations.splice(i,1);
                    return;
                }
             }
             if(this.salvoLocations.length >= 5){
                return;
             }
             if(this.state == "choosingSalvo"){
                this.$refs["O" +cell][0].style.backgroundColor ="#fef59f";
             }
            this.salvoLocations.push(cell);
            this.updateGrid();
         },

         firingSalvos : async function(){

              this.currentGamePlayerId = this.getURLParameter('gp');
              var salvoObject = {salvoLocations : this.salvoLocations}
              var result = await fetch('http://localhost:8080/api/games/players/'+this.currentGamePlayerId+'/salvos',{
                                method: "POST",
                                body:JSON.stringify(salvoObject),
                                headers: {
                                      'Content-Type': 'application/json'
                                    },
                            })
                            .then(res => res.json())
                            .then(data => data)
                            .catch(err => err)

              if(result.result){
                   this. updateGrid();
              }else{
                  this.exceptionMessage = result.message;
              }
              this.salvoLocations = [];
              this.getState();
         },

         getHits : function(){
             this.hitsOnMe = [];
             for(var i = 0; i < this.game[0].playerGameResult.length; i++){
                var x = this.game[0].playerGameResult[i];
                var turn = x.turn;
                var hits = "";
                x.hits.forEach(function(h) {
                    hits += h.shipType + " (" + h.nrOfHits + ")";
                });
                var left = this.game[0].playerGameResult[i].nrOfShipsLeft;
                this.hitsOnMe.push({"turn" : turn, "hits" : hits, "left" : left});
             }
             this.hitsOnMe.sort(function(a, b) {
                if (a.turn == b.turn) { return 0; }
                if (a.turn < b.turn) { return 1; }
                if (a.turn > b.turn) { return -1; }
             });

              this.hitsOnOpp = [];
              for(var i = 0; i < this.game[0].opponentGameResult.length; i++){
                 var x = this.game[0].opponentGameResult[i];
                 var turn = x.turn;
                 var hits = "";
                 x.hits.forEach(function(h) {
                     hits += h.shipType + " (" + h.nrOfHits + ")";
                 });
                 var left = this.game[0].opponentGameResult[i].nrOfShipsLeft;
                 this.hitsOnOpp.push({"turn" : turn, "hits" : hits, "left" : left});
              }
              this.hitsOnOpp.sort(function(a, b) {
                 if (a.turn == b.turn) { return 0; }
                 if (a.turn < b.turn) { return 1; }
                 if (a.turn > b.turn) { return -1; }
              });
         },

         getState : async function(){
            this.currentGamePlayerId = this.getURLParameter('gp');
            var result = await fetch('http://localhost:8080/api/state/'+this.currentGamePlayerId,{
                          methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)
            console.log(result);
            if(result.status == 1){
                this.state = "choosingShips";
            }else if(result.state == 2){
                this.state = "gameOver";
            }else if(result.state == 10){
                this.state = "choosingSalvo";
            }else if (result.state == 20){
                this.state = "waiting";
            }
            console.log("state is :" +this.state);
         }

     },
     created: function() {
        this.parameter = this.$route.query
        this.init();
     },
});