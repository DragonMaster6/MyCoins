<template>
  <div class="coin-list">
    <div class="coin-item col-3" v-for="coin in coins">
      <div class="coin-item-header"> {{ coin.type }} </div>
      <div class="coin-item-body"> {{ coin.description }} </div>
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      endpoint: 'api/coins?page=',
      page: 1,
      test: 'Hello world',
      coins: {},
    };
  },

  mounted: function() {
    this.fetchCoins();
  },

  methods: {
    fetchCoins: function (newPage = 1) {
      axios.get(this.endpoint + newPage)
        .then(({data}) => {
          console.log(data);
          this.coins = data.data;
        });
    }
  }
}
</script>
