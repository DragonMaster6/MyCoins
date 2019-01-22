<?php

namespace App\Http\Controllers\Api;

use App\CoinType;
use App\Http\Controllers\Controller;
use App\Http\Resources\CoinTypeResource;

use Illuminate\Http\Request;

class CoinTypeController extends Controller
{
  /**
  * Returns a list of 20 Coins per page.
  * @return CoinTypeResource
  */
  public function index() {
    $coinTypes = CoinType::all();
    return CoinTypeResource::collection($coinTypes);
  }

  /**
   * Retrieve a single coin.
   * @param Coin $coin
   * @return CoinTypeResource
   */
  public function show(CoinType $coinType) {
    return new CoinTypeResource($coinType);
  }

  /**
   * Creates a new Coin
   * @param Request $request
   * @return CoinTypeResource
   */
  public function store(Request $request) {
    $coinType = $this->validate($request, [
      'coin_type' => 'required',
      'owner_id' => 'nullable|integer',
      'mint' => 'required|min:1',
      'year' => 'required|integer',
      'description' => 'nullable',
    ]);

    $coinType = CoinType::create($coin);

    return new CoinTypeResource($coinType);
  }

  /**
   * Deletes a specified coin
   * @param Coin $coin
   * @return CoinTypeResource
   */
  public function destroy(CoinType $coinType) {
    CoinType::delete($coinType);

    return new CoinTypeResource($coinType);
  }
}
