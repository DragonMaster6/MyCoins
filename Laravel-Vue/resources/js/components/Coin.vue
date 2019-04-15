<template>
  <div class="container-fluid">
    Edit Mode: {{ editMode }}
    New Coin: {{ newCoin }}
    <div class="row">
      {{ coin }}
    </div>
    <div class="row" v-show="message || errMsg">
      <div class="alert alert-success" v-show="message">
        {{ message }}
      </div>
      <div class="alert alert-danger" v-show="errMsg">
        {{ errMsg }}
      </div>
    </div>
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

          <!-- TODO: Small bug where it doesn't show the currently selected type. -->
          <b-form-select @input="coinTypeChange($event)"
            :options="coinTypeOptions"
            v-show="editMode">
          </b-form-select>
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
          <label class="form-label col-sm-3"> Origin: </label>
          <p class="col-sm-9"
            v-show="coin.type.name && !editMode">
            {{ coin.type.name }}
          </p>
          <p v-show="editMode"> This field depends on the Coin Type </p>
        </div>

        <!-- Actions: Edit, save, delete, etc -->
        <button class="btn btn-secondary"
          v-show="!editMode"
          @click="editMode=true"> Edit </button>
        <button class="btn btn-primary"
          v-show="editMode"
          @click="saveCoin()"> Save </button>
        <!-- TODO: Prevent the first delete by adding a confirm -->
        <button class="btn btn-danger"
          id="deleteBtn"
          v-show="coin"> Delete </button>
        <b-popover target="deleteBtn" triggers="focus"
          :show.sync="confirmDelete">
          <template slot="title"> Are you sure? </template>
          <button class="btn btn-danger"
            @click="deleteCoin()"> Delete Me! </button>
          <button class="btn btn-primary"
            @click="confirmDelete=false"> Panic! </button>
        </b-popover>
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
      coinTypes: [],
      coinTypeOptions: [],
      editMode: false,
      newCoin: false,
      confirmDelete: false,
      message: '',
      errMsg: '',
    };
  },
  props: ['currentCoin'],
  mounted: function() {
    // Retrieve a fake coin.. might change this to init the states.
    this.getCoin(this.currentCoin);
    console.log("Coin Template Created");
    // Retrieve the different coin types.
    this.getCoinTypes();
  },
  watch: {
    currentCoin: function(coinId, oldCoinId) {
      console.log("Getting a coin...");
      this.getCoin(coinId);
    }
  },
  methods: {
    getCoinTypes: function() {
      axios.get('api/cointypes')
      .then(({data}) => {
        this.coinTypes = data.data;
        this.coinTypeOptions = data.data.map((value) => {
          return {value: value.id, text: value.name};
        });
        console.log(this.coinTypes);
      })
      .catch((error) => {
        console.log("There was an issue getting the coin types");
      })
    },
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
    // When the user selects a different coin type, repopulate the
    // coin model.
    coinTypeChange: function(data) {
      this.coin.type = this.coinTypes[data];
    },
    // Save the state of the current coin being viewed
    saveCoin: function() {
      // First and foremost, convert the coin type back to an id
      this.coin.type = this.coin.type.id;

      // Determine to save a new coin or update an existing one.
      // TODO: Need to emit an event to tell the coin list to refresh its list;
      if (this.newCoin) {
        axios.post('api/coins', this.coin)
        .then(({data}) => {
          // The response returned okay, refresh the coin and
          // display a success message.
          console.log("Coin Saved", data);
          this.message = "Successfully created a new coin.";

        })
        .catch((error) => {
          // There was an error. Capture the message and display it.
          console.error("Coin Error", error);
          this.errMsg = "Unable to create a new coin.";
        });
      }
      else {
        axios.put('api/coins/'+this.coin.id, this.coin)
        .then(({data}) => {
          // The response returned okay, refresh the coin and
          // display a success message.
          console.log("Coin Saved", data);
          this.message = "Coin Successfully saved."

        })
        .catch((error) => {
          // There was an error. Capture the message and display it.
          console.error("Coin Error", error);
          this.errMsg = "Unable to save the coin."
        });
      }
    },
    // Deletes the currently selected coin.
    // TODO: Emit a list refresh to reflect the coin deletion.
    deleteCoin: function() {
      axios.delete('api/coins/' + this.coin.id)
        .then(({data}) => {
          // Successfully deleted the coin
          this.message = "Deleted the coin succesfully";
          this.confirmDelete = false;
        })
        .catch (error => {
          // There was an error deleting the coin.
          console.log("Coin Deletion", error);
          this.errMsg = "Unable to delete the coin.";
        })
    }
  }
}
</script>
