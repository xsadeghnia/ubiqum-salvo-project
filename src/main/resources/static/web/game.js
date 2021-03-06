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
     status : "",
  },
     methods:{
         updateGrid : async function(){
                this.game = await  fetch('/api/game_view/'+this.parameter.gp,{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)


                for( var i = 0 ; i <  this.game[0].ships.length ; i++){
                        var locs = this.game[0].ships[i].shipLocations;
                        for(var j = 0 ; j< locs.length ; j++){
                            this.$refs["P" + locs[j]][0].style.backgroundColor = 'silver';
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

            this.calcHits();
            if( this.state == "gameOver"){
                var pSize = this.game[0].playerGameResult.length;
                var oSize = this.game[0].opponentGameResult.length;
                if(this.game[0].playerGameResult[pSize-1].nrOfShipsLeft == 0){
                    this.status = "lose";
                }else if(this.game[0].opponentGameResult[oSize-1].nrOfShipsLeft == 0){
                    this.status = "win";
                }
                console.log("opnr:"+this.game[0].opponentGameResult[0].nrOfShipsLeft);
                console.log("pnr:"+this.game[0].playerGameResult[0].nrOfShipsLeft);
                console.log("stateeeeeeee:"+this.state);
            }
         },

         init : async  function(){
            this.updateGrid();
            this.currentPlayer =  await  fetch('/api/principal',{
                                       methods: "GET",
                                   })
                                   .then(res => res.json())
                                   .then(data => data)
                                   .catch(err => err);

            console.log(this.currentPlayer.firstName);                       
            this.getState();
            setInterval(function () { this.getState(); }.bind(this), 2000);
            setInterval(function () { this.updateGrid(); }.bind(this), 2000);

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

            var result = await fetch('/api/games/players/'+this.currentGamePlayerId+'/ships',{
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
                    // Check if it is my slavo
                    if(cell == this.game[0].salvos[i].salvoLocations[j] &&
                        this.currentPlayer.id == this.game[0].salvos[i].playerId){
                        return;
                    }
                }
             }
             for(var i=0; i < this.salvoLocations.length; i++){
                if(cell == this.salvoLocations[i]){
                    this.salvoLocations.splice(i,1);
                    if(this.state == "choosingSalvo"){
                        this.$refs["O" +cell][0].style.backgroundColor ="";
                    }
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
              var result = await fetch('/api/games/players/'+this.currentGamePlayerId+'/salvos',{
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

         calcHits : function(){
            this.playerCarrier = 0;
            this.playerBattleship = 0;
            this.playerDestroyer = 0;
            this.playerSubmarine = 0;
            this.playerPatrolboat= 0;
            var pGameResult = this.game[0].playerGameResult;
            for(var i = 0; i < pGameResult.length; i++){
                for(var j = 0; j < pGameResult[i].hits.length; j++){
                    if(pGameResult[i].hits[j].shipType == "Carrier"){
                        this.playerCarrier += pGameResult[i].hits[j].nrOfHits;
                    }else if (pGameResult[i].hits[j].shipType == "Battleship") {
                        this.playerBattleship += pGameResult[i].hits[j].nrOfHits;
                    }else if (pGameResult[i].hits[j].shipType == "Destroyer") {
                        this.playerDestroyer += pGameResult[i].hits[j].nrOfHits;
                    }else if (pGameResult[i].hits[j].shipType == "Submarine") {
                        this.playerSubmarine += pGameResult[i].hits[j].nrOfHits;
                    }else if (pGameResult[i].hits[j].shipType == "Patrol Boat") {
                        this.playerPatrolboat += pGameResult[i].hits[j].nrOfHits;
                    }
                    for (var hitLocCounter = 0; hitLocCounter < pGameResult[i].hits[j].hitLocations.length; hitLocCounter++) {
                        this.$refs["P" + pGameResult[i].hits[j].hitLocations[hitLocCounter]][0].style.backgroundColor ='#ff5200';
                    }
                }
            }
            this.opponentCarrier = 0;
            this.opponentBattleship = 0;
            this.opponentDestroyer = 0;
            this.opponentSubmarine = 0;
            this.opponentPatrolboat = 0;
            var oGameResult = this.game[0].opponentGameResult;
            for(var i = 0; i < oGameResult.length; i++){
                for(var j = 0; j < oGameResult[i].hits.length; j++){
                   if(oGameResult[i].hits[j].shipType == "Carrier"){
                        this.opponentCarrier += oGameResult[i].hits[j].nrOfHits;
                    }else if (oGameResult[i].hits[j].shipType == "Battleship") {
                        this.opponentBattleship += oGameResult[i].hits[j].nrOfHits;
                    }else if (oGameResult[i].hits[j].shipType == "Destroyer") {
                        this.opponentDestroyer += oGameResult[i].hits[j].nrOfHits;
                    }else if (oGameResult[i].hits[j].shipType == "Submarine") {
                        this.opponentSubmarine += oGameResult[i].hits[j].nrOfHits;
                    }else if (oGameResult[i].hits[j].shipType == "Patrol Boat") {
                        this.opponentPatrolboat += oGameResult[i].hits[j].nrOfHits;
                    }
                    for (var hitLocCounter = 0; hitLocCounter < oGameResult[i].hits[j].hitLocations.length; hitLocCounter++) {
                        this.$refs["O" + oGameResult[i].hits[j].hitLocations[hitLocCounter]][0].style.backgroundColor ='#ff5200';
                    }
                }
            }
         },

         getState : async function(){
            this.currentGamePlayerId = this.getURLParameter('gp');
            var result = await fetch('/api/state/'+this.currentGamePlayerId,{
                          methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)
            console.log(result);
            if(result.state == 1 && this.state != "choosingShips"){
                this.state = "choosingShips";
                this.exceptionMessage = "";
            }else if(result.state == 2 && this.state != "gameOver"){
                this.state = "gameOver";
                 this.exceptionMessage = "";
            }else if(result.state == 10 && this.state != "choosingSalvo"){
                this.state = "choosingSalvo";
                this.exceptionMessage = "";
                this.updateGrid;
            }else if (result.state == 20 && this.state != "waiting"){
                this.state = "waiting";
                this.exceptionMessage = "";
                this.updateGrid;
            }
            console.log("state is :" +this.state);
         },
     },
     created: function() {
        this.parameter = this.$route.query
        this.init();
     },
});