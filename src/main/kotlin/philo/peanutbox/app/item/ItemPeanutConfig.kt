package philo.peanutbox.app.item

import philo.peanutbox.core.annotation.Peanut
import philo.peanutbox.core.annotation.PeanutContainer

@PeanutContainer
class ItemPeanutConfig {

    @Peanut
    fun item(): Item {
        return Item()
    }

    @Peanut
    fun itemService(): ItemService {
        return ItemService()
    }
}