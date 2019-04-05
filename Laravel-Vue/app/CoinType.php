<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CoinType extends Model
{
  // Assign model properties to be mass-fillable
  protected $fillable = ['name', 'value', 'origin'];
}
