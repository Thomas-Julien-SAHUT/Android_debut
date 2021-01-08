package com.example.ossquiz.models

class BoiteAQuestion (var _LesQuestions : List<Question>, var index : Int = 0){
    fun GetQuestion(): Question {
        if (index == _LesQuestions.size)
            index = 0;
        index++
        return _LesQuestions.get(index-1)

    }
}