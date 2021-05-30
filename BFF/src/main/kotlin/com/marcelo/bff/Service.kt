package com.marcelo.bff

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.Column
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScreenBuilder
import br.com.zup.beagle.widget.navigation.Touchable
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.Text
import org.springframework.stereotype.Service

@Service
class Service {
    fun getMyScreen(): ScreenBuilder = MyScreen()
}

data class Person (
    val name: String,
    val race: String,
    val planet: String,
    val isMistborn: Boolean,
    val age: Int,
    val sex: String
)

class MyScreen : ScreenBuilder {

    val characters = listOf(
            Person(
                    name = "Kelsier",
                    race = "Half-skaa",
                    planet = "Scadrial",
                    isMistborn = true,
                    age = 38,
                    sex = "male"
            ),
            Person(
                    name = "Vin",
                    race = "Half-skaa",
                    planet = "Scadrial",
                    isMistborn = true,
                    age = 20,
                    sex = "female"
            ),
            Person(
                    name = "TenSoon",
                    race = "Kandra",
                    planet = "Scadrial",
                    isMistborn = false,
                    age = 40,
                    sex = "male"
            ),
    )

    override fun build() = Screen(
            "MAIN",
        child = Styled(self = Column(
            children = listOf(
                Touchable(
                    onPress = listOf(
//                            Alert(
//                                title = "Image",
//                                message = "Clicked on Message"
//                            )
                    ),
                    child = Text(text = "Meu primeiro App com o Beagle", alignment = TextAlignment.CENTER, styleId = "H1")
                ),
                Text(
                    text = "O Beagle é um framework open source de desenvolvimento cross-platform pautado no paradigma de implementação de Server-Driven UI.",
                    alignment = TextAlignment.CENTER,
                    textColor = "#505050"
                ),
                Button(
                        text = "Clique aqui",
                        onPress = listOf(Alert(message="Exemplo de botão"))
                ),
                ListView(
                    context = ContextData(id = "characters", value = characters),
                    dataSource = expressionOf("@{characters}"),
                    template = Container(
                        children = listOf(
                            Text("Name: @{item.name}"),
                            Text("Race: @{item.race}"),
                            Text("Mistborn: @{item.isMistborn}"),
                            Text("Planet: @{item.planet}"),
                            Text("sex: @{item.sex}"),
                            Text("age: @{item.age}"),
                        )
                    ).applyStyle(
                        Style(
                            margin = EdgeValue(bottom = 20.unitReal())
                        )
                    )
                )
            )
        )) {
            padding = EdgeValue.Companion.all(16)
        }
    )
}