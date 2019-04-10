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

  // Retrieves a single coin from the database
  function show($id) {
    return new CoinResource(Coin::find($id));
  }

  // Handles new coin requests and adds them to the database
  function create(Request $request) {
    // First and foremost, validate the input
    $validatedData = $request->validate([
      'type' => 'required',
      'mint' => 'required',
      'year' => 'required',
      'origin' => 'required',
      'description' => 'nullable'
    ]);

    // If all goes smoothly, insert it into the database
    $new_coin = Coin::create($validatedData);

    return new CoinResource($new_coin);
  }
}
