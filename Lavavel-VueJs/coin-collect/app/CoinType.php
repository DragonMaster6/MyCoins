<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CoinType extends Model
{
    // Fields that are mass-assignable
    protected $fillable = ['name', 'origin'];
}
