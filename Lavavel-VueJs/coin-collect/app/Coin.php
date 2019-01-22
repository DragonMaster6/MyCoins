<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class Coin extends Model
{
    // Fields to be mass-assigned
    protected $fillable = ['coin_type','mint','year','description'];

}
