package philo.peanutbox.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import philo.peanutbox.app.item.ItemService
import philo.peanutbox.core.feature.PeanutManager

class PeanutManagerTest {

    @Test
    fun test() {
        PeanutManager.init("philo.peanutbox.app")
        val itemService = PeanutManager.findPeanut(ItemService::class.java)

        assertAll(
                Executable { assertThat(itemService).isNotNull },
                Executable { assertThat(itemService).isInstanceOf(ItemService::class.java) }
        )
    }
}