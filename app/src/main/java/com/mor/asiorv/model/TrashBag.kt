package com.mor.asiorv.model

import java.util.*

data class TrashBag(
    val title:String = "",
    val weight: Double = 0.0,
    val id: UUID = UUID.randomUUID()
        )

data class PairTrashBag(
    val trash1: TrashBag = TrashBag(),
    val trash2: TrashBag = TrashBag()
){
    fun check():Boolean{
// TODO: be sure that u have both of the trash
        return ((trash1.weight + trash2.weight)<= 3)
    }
}