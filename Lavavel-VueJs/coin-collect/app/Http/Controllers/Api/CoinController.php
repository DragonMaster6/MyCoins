<?php

namespace App\Http\Controllers\Api;

use App\Coin;
use App\Http\Controllers\Controller;
use App\Http\Resources\CoinResource;

use Illuminate\Http\Request;

class CoinController extends Controller
{
  /**
  * Returns a list of 20 Coins per page.
  * @return CoinResource
  */
  public function index() {
    $coins = Coin::listCoins()
      ->paginate(20);
    return CoinResource::collection($coins);
  }

  /**
   * Retrieve a single coin.
   * @param Coin $coin
   * @return CoinResource
   */
  public function show(Coin $coin) {
    return new CoinResource($coin);
  }

  /**
   * Creates a new Coin
   * @param Request $request
   * @return CoinResource
   */
  public function store(Request $request) {
    $coin = $this->validate($request, [
      'coin_type' => 'required',
      'owner_id' => 'nullable|integer',
      'mint' => 'required|min:1',
      'year' => 'required|integer',
      'description' => 'nullable',
    ]);

    $coin = Coin::create($coin);

    return new CoinResource($coin);
  }

  /**
   * Deletes a specified coin
   * @param Coin $coin
   * @return CoinResource
   */
  public function destroy(Coin $coin) {
    Coin::delete($coin);

    return new CoinResource($coin);
  }
}
