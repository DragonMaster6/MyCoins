<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CoinsController extends Controller
{
  // Fetches the home page and returns a view
  function index() {
    return view('coins.index');
  }
}
