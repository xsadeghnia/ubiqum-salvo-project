new Vue({
    el:'#myVue',
    data:{
    games : [],
    },
    methods:{
        init : async function(){
           this.games =  await  fetch('http://localhost:8080/api/games',{
                            methods: "GET",
                        })
                        .then(res => res.json())
                        .then(data => data)
                        .catch(err => err)
           },
          convertTimestamp:function(t) {
              var date = new Date(t * 1000);
              var formattedDate = ('0' + date.getDate()).slice(-2) + '/' + ('0' + (date.getMonth() + 1)).slice(-2) + '/' + date.getFullYear() + ' ' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
              return formattedDate
          }
        },
    created: function() {
      this.init();
    }
});