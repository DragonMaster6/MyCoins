<template>
  <div class="coin-container">
    <div class="coin-list row">
      <div class="coin-item col-md-3 col-sm-4 col-xs-12"
        v-for="coin in coins"
        v-b-modal="'coin-modal'"
        @click="currentCoin=coin.id">
        <div class="coin-item-header"> {{ coin.type.name }} - {{ coin.year}} </div>
        <div class="coin-item-body"> {{ coin.description }} </div>
      </div>
    </div>

    <!-- Actions listing what we can do with the coins -->
    <div class="coin-actions">
      <button id="coin-action-pop"
        class="btn btn-lg"
        @click="newCoinPop=!newCoinPop"> + </button>

      <b-popover :show.sync="newCoinPop"
        placement="top"
        target="coin-action-pop">
        <button class="btn btn-light btn-sm"
          v-b-modal="'coin-modal'"
          @click="currentCoin=-1">
          New Coin
        </button>
        <button class="btn btn-light btn-sm">
          New Coin Type
        </button>
      </b-popover>
    </div>

    <!-- Modal displaying individual coins -->
    <b-modal id="coin-modal" ok-only hide-header>
      <coin :currentCoin="currentCoin"></coin>
    </b-modal>
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
      currentCoin: -1,
      newCoinPop: false,
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
