package com.example.test.dagger

/**
 * author: beitingsu
 * created on: 2021/3/16 4:41 PM
 */
class ClassB {
    var classA: ClassA? = null

    constructor()

    constructor(classA: ClassA) {
        this.classA = classA
    }
}