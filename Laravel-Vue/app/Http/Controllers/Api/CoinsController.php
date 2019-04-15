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
    return new CoinResource(Coin::findOrFail($id));
  }

  // Creates a new coin to be added to the collection of treasure.
  function store(Request $request) {
    // First and foremost, validate the input
    $validatedData = $request->validate([
      'type' => 'required',
      'mint' => 'required',
      'year' => 'required',
      'description' => 'nullable'
    ]);

    // If all goes smoothly, insert it into the database
    $new_coin = Coin::create($validatedData);

    return new CoinResource($new_coin);
  }

  // Updates a coins information
  function update(Request $request, $id) {
    // First and foremost, validate the input
    $validatedData = $request->validate([
      'type' => 'required',
      'mint' => 'required',
      'year' => 'required',
      'description' => 'nullable'
    ]);

    // Once validated, update the database entry.
    $coin = Coin::findOrFail($id);

    $coin->update($validatedData);

    return json_encode("Coin Saved");
  }

  // Deletes a coin from the database
  function destroy(Request $request, $id) {
    // Find the coin to be deleted.
    $coin = Coin::findOrFail($id);

    $coin->delete();

    return json_encode("Coin Deleted");
  }
}
