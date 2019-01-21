<?php

use Faker\Generator as Faker;

$factory->define(App\CoinType::class, function (Faker $faker) {
    return [
        'name' => $faker->word,
        'origin' => $faker->word,
    ];
});
