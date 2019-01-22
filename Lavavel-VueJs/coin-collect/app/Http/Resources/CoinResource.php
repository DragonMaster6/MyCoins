<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class CoinResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
          'id' => $this->id,
          'coin_type' => $this->coin_type,
          'owner_id' => $this->owner_id,
          'mint' => $this->mint,
          'year' => $this->year,
          'description' => $this->description,
        ];
    }
}
