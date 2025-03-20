package com.example.linguadailyapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainWordDisplayContainer(){
    Box(){
        //Contains a Coloumn with everything
        Column() {
            //Contains the Main word
            //Defenition
            //"How to use + ${MainWord}"
            //How to use the main word

        }
    }
}

@Preview
@Composable
fun PreviewMainWordDisplayContainer(){
MaterialTheme {
    MainWordDisplayContainer()
}
}