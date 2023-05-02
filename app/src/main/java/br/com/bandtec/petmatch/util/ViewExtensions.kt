package br.com.bandtec.petmatch.util

import android.view.View

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.beInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.beGone() {
    this.visibility = View.GONE
}