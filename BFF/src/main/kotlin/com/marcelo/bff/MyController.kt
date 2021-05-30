package com.marcelo.bff

import br.com.zup.beagle.widget.layout.ScreenBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController(private val myService: Service) {

    @GetMapping("/screen")
    fun getScreen() : ScreenBuilder = myService.getMyScreen()
}