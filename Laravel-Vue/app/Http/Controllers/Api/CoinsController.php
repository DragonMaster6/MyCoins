<?php

namespace App\Http\Controllers\Api;

use App\Coin;
use App\Http\Resources\CoinResource;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class CoinsController extends Controller
{
  // Returns a list of paginated coins
  function index() {
    $coins = Coin::latest()
      ->paginate(10);

    return CoinResource::collection($coins);
  }
}
