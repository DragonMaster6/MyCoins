<?php

namespace App\Http\Resources;

use App\CoinType;
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
    // TODO: Get the coin type and push it to the array.
    return [
      'id' => $this->id,
      'year' => $this->year,
      'type' => CoinType::findOrFail($this->type),
      'mint' => $this->mint,
      'description' => $this->description,
    ];
  }
}
