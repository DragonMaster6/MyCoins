<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CoinController extends Controller
{
    /**
     * Displays the homepage of the application.
     */
    public function index() {
      return view('coins.index');
    }
}
