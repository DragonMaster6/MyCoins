@extends('master')

@section('content')
  <!-- <div> -->
      <!-- Displays the list of coins -->
      <?php // TODO: Incorporate the paging systems ?>
      <coin-list></coin-list>
      <!-- Display options to create new coins and types -->
      <!-- <div class="coin-actions" @click="newCoinPop=!newCoinPop">
        <span class="glyphicon glyphicon-plus">+</span>
        <div class="coin-actions-list" v-show="newCoinPop">
          <button class="btn btn-light btn-sm"> New Coin </button>
          <button class="btn btn-light btn-sm"> New Coin Type </button>
        </div>
      </div> -->
      <div class="coin-actions">
        <button id="coin-action-pop"
          class="btn btn-lg"
          @click="newCoinPop=!newCoinPop"> + </button>

        <b-popover :show.sync="newCoinPop"
          placement="top"
          target="coin-action-pop">
          <button class="btn btn-light btn-sm"> New Coin </button>
          <button class="btn btn-light btn-sm"> New Coin Type </button>
        </b-popover>
      </div>
  <!-- </div> -->
@endsection
