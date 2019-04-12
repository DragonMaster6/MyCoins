<template>
  <div class="container-fluid">
    Edit Mode: {{ editMode }}
    New Coin: {{ newCoin }}
    <div class="row">
      <div class="col">
        <div class="coin-image">
          Image goes here
        </div>
        <div class="coin-desc">
          <label> Description: </label>
          <p v-show="coin.description && !editMode">
            {{ coin.description }}
          </p>
          <textarea v-show="editMode"
            v-model="coin.description" >
          </textarea>
        </div>
      </div>

      <div class="col">
        <!-- Coin Type Field -->
        <div class="form-group row">
          <label class="form-label col-sm-3"> Type: </label>
          <p class="col-sm-9"
            v-show="coin.type.name && !editMode">
            {{ coin.type.name }}
          </p>
          <!-- TODO: Make this a select -->
          <!-- TODO: When this changes, the origin needs to change too -->
          <input type="text" name="coin-type" class="col-sm-9"
            v-show="editMode"
            v-model="coin.type.name">
        </div>
        <!-- Coin Mint Field -->
        <div class="form-group row">
          <label class="form-label col-sm-3"> Mint: </label>
          <p class="col-sm-9"
            v-show="coin.mint && !editMode">
            {{ coin.mint }}
          </p>
          <input type="text" name="coin-mint" class="col-sm-9"
            v-show="editMode"
            v-model="coin.mint">
        </div>
        <!-- Coin Year Field -->
        <div class="form-group row">
          <label class="form-label col-sm-3"> Year: </label>
          <p class="col-sm-9"
            v-show="coin.year && !editMode">
            {{ coin.year }}
          </p>
          <input type="text" name="coin-year" class="col-sm-9"
            v-show="editMode"
            v-model="coin.year">
        </div>
        <!-- Coin Origin Field -->
        <!-- NOTE: Don't edit this field since it depends on the type -->
        <div class="form-group row">
          <label class="form-label col-sm-3"> Type: </label>
          <p class="col-sm-9"
            v-show="coin.type.name && !editMode">
            {{ coin.type.name }}
          </p>
          <p v-show="editMode"> This field depends on the Coin Type </p>
        </div>

        <!-- <label> Type: </label>
        <p> {{coin.type.name}} </p>
        <label> Mint: </label>
        <p> {{coin.mint}} </p>
        <label> Year: </label>
        <p> {{coin.year}} </p>
        <label> origin: </label>
        <p> {{coin.type.origin}} </p> -->

        <!-- Actions: Edit, save, delete, etc -->
        <button class="btn btn-secondary"
          v-show="!editMode"
          @click="editMode=true"> Edit </button>
        <button class="btn btn-primary"
          v-show="editMode"
          @click="saveCoin()"> Save </button>
        <!-- TODO: Prevent the first delete by adding a confirm -->
        <button class="btn btn-danger"
          v-show="coin"
          @click="deleteCoin()"> Delete </button>
        <button class="btn btn-secondary"
          v-show="editMode"
          @click="editMode=false"> Cancel </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      endpoint: 'api/coins',
      coin: {type:{}},
      editMode: false,
      newCoin: false,
    };
  },
  props: ['currentCoin'],
  mounted: function() {
    this.getCoin(this.currentCoin);
    console.log("Coin Template Created");
  },
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
          this.editMode = false;

          // Don't want to create a new coin if one is found.
          if (this.newCoin) {
            this.newCoin = false;
          }
        })
        .catch((error) => {
          console.log("Unable to retrieve the coin");
          // Set the coin to nothing and setup for a new coin.
          this.coin = {type:{}};
          this.editMode = true;
          this.newCoin = true;
        });
    },
  }
}
</script>
