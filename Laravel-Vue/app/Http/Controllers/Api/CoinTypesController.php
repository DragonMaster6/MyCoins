<?php

namespace App\Http\Controllers\Api;

use App\CoinType;
use App\Http\Resources\CoinTypeResource;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class CoinTypesController extends Controller
{
  // List all the coin types that we know
  function index() {
    $types = CoinType::latest()->get();

    return CoinTypeResource::collection($types);
  }
}
