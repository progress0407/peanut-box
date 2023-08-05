package philo.peanutbox.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import philo.peanutbox.app.item.ItemService
import philo.peanutbox.core.feature.ManualPeanutScanner
import philo.peanutbox.core.feature.PeanutManager

class PeanutManagerTest {

    @Test
    fun test() {
        PeanutManager.init("philo.peanutbox.app")

//        val itemService = peanutManager.findPeanut(ItemService::class.java)

//        assertThat(itemService).isNotNull;
    }
}