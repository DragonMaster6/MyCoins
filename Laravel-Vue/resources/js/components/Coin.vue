<template>
  <div class="container-fluid">
    <div class="row" v-if="coin">
      <div class="col">
        <div class="coin-image">
          Image goes here
        </div>
        <div class="coin-desc">
          {{ coin.description }}
        </div>
      </div>
      <div class="col">
        <label> Type: </label>
        <p> {{coin.type.name}} </p>
        <label> Mint: </label>
        <p> {{coin.mint}} </p>
        <label> Year: </label>
        <p> {{coin.year}} </p>
        <label> origin: </label>
        <p> {{coin.type.origin}} </p>

        <!-- Actions: Edit, save, delete, etc -->
        <button class="btn btn-secondary"> Edit </button>
        <button class="btn btn-primary"> Save </button>
        <button class="btn btn-danger"> Delete </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      endpoint: 'api/coins',
      coin: null,
    };
  },
  props: ['currentCoin'],
  watch: {
    currentCoin: function(coinId, oldCoinId) {
      console.log("Getting a coin...");
      this.getCoin(coinId);
    }
  },
  methods: {
    getCoin: function(coinId) {
      axios.get(this.endpoint + '/' + coinId)
        .then(({data}) => {
          console.log("Coin retrievied",data);
          this.coin = data.data;
        })
        .catch((error) => {
          console.log("Unable to retrieve the coin");
          this.coin = null;
        });
    },
  }
}
</script>
