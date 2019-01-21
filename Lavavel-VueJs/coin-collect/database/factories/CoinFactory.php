<?php

use Faker\Generator as Faker;

$factory->define(App\Coin::class, function (Faker $faker) {
    return [
        'coin_type' => factory(App\CoinType::class),
        'mint' => $faker->word,
        'year' => $faker->year,
        'description' => $faker->paragraph(4),
    ];
});
