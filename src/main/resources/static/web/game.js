  var router = new VueRouter({
    mode: 'history',
    routes: [],
  });
  new Vue({
      router,
     el:"#myVue",
     data:{
     game : [],
     rows :['','1','2','3','4','5','6','7','8','9','10'],
     columns :['A','B','C','D','E','F','G','H','I','J'],
     parameter :'',
     },
     methods:{
         init : async  function(){
            this.game = await  fetch('http://localhost:8080/api/game_view/'+this.parameter.gp,{
                                        methods: "GET",
                                    })
                                    .then(res => res.json())
                                    .then(data => data)
                                    .catch(err => err)
              for( var i = 0 ; i <  this.game[0].ships.length ; i++){
                    var locs = this.game[0].ships[i].shipLocations
                    for(var j = 0 ; j< locs.length ; j++){
                        this.$refs["P" + locs[j]][0].style.backgroundColor = '#66bb6a';
                    }
              }
              for(var k = 0 ; k < this.game[0].salvos.length ; k++){
                var fireLocations = this.game[0].salvos[k].salvoLocations
                for(var l = 0; l < fireLocations.length ; l++){
                    if(this.game[0].salvos[k].playerId == this.game[0].gamePlayers[0].player.id){
                        this.$refs["O" + fireLocations[l]][0].style.backgroundColor ='red';
                        this.$refs["O" + fireLocations[l]][0].innerHTML = this.game[0].salvos[k].turn ;
                    }else{
                        this.$refs["P" + fireLocations[l]][0].style.backgroundColor ='red';
                        this.$refs["P" + fireLocations[l]][0].innerHTML = this.game[0].salvos[k].turn ;
                    }

                }
              }
         },
     },
     created: function() {
        this.parameter = this.$route.query
        this.init();
     },
});