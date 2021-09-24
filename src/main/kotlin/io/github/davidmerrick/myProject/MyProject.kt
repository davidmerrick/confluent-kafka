package io.github.davidmerrick.myProject

import io.github.davidmerrick.myProject.MyProject.Companion.hello

class MyProject {

    companion object {
        fun hello() = "Hello, world!"
    }
}

fun main(args: Array<String>){
    println(hello())
}

