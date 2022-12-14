package com.ilsamil.readingdiarycleanarchitecture.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Books (
    var title : String,
    var contents : String,
    var url : String,
    var isbn : String,
    var datetime : String,
    var authors : List<String>,
    var publisher : String,
    var translators : List<String>,
    var price : String,
    var sale_price : String,
    var thumbnail : String,
    var status : String,
) : Parcelable