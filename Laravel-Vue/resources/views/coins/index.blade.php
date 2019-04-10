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
      
  <!-- </div> -->
@endsection
