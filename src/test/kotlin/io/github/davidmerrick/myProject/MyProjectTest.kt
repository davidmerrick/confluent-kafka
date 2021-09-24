package io.github.davidmerrick.myProject

import io.kotlintest.shouldBe
import org.testng.annotations.Test

class MyProjectTest {

    @Test
    fun `Should say hello`(){
        MyProject.hello() shouldBe "Hello, world!"
    }
}
