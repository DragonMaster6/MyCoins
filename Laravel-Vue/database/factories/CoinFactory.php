<?php

use App\User;
use Illuminate\Support\Str;
use Faker\Generator as Faker;

/*
|--------------------------------------------------------------------------
| Model Factories
|--------------------------------------------------------------------------
|
| This directory should contain each of the model factory definitions for
| your application. Factories provide a convenient way to generate new
| model instances for testing / seeding your application's database.
|
*/

$factory->define(App\Coin::class, function (Faker $faker) {
    return [
        'type' => function() {
          return factory(App\CoinType::class)->create()->id;
        },
        'mint' => $faker->cityPrefix,
        'year' => $faker->numberBetween(1950,2019),
        'description' => $faker->paragraph,
    ];
});
