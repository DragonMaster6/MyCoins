<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Coin extends Model
{
  // Assign mass-fillable fields for the Model
  protected $fillable = ['type', 'mint', 'year', 'description'];
}
