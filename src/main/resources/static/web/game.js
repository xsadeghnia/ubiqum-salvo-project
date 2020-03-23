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
                        this.$refs[locs[j]][0].style.backgroundColor = '#66bb6a';
                    }
              }
         },
     },
     created: function() {
        this.parameter = this.$route.query
        this.init();
     },
});