package philo.peanutbox.app.item

import philo.peanutbox.core.annotation.ManualPeanut
import philo.peanutbox.core.annotation.PeanutBox

@PeanutBox
class ItemPeanutConfig {

    @ManualPeanut
    fun item(): Item {
        return Item()
    }

    @ManualPeanut
    fun itemService(): ItemService {
        return ItemService()
    }
}