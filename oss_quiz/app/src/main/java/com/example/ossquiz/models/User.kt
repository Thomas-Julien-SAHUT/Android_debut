package com.example.ossquiz.models

class User (val _Name : String, var _OSS : Boolean = false) {
    init {
        if (_Name == "Bramard" || _Name == "Flantier" || _Name == "De la batte")
            _OSS =true;
    }
}