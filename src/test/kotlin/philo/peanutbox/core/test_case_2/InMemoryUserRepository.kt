package philo.peanutbox.core.test_case_2

import philo.peanutbox.core.annotation.ThisIsPeanut
import java.util.concurrent.ConcurrentHashMap

@ThisIsPeanut
class InMemoryUserRepository : UserDao {
    private val database: MutableMap<String, User> = ConcurrentHashMap<String, User>()

    init {
        val gugu = User(1, "gugu", "password", "hkkang@woowahan.com")
        val philz = User(2, "philz", "1234", "philz@hello.com")
        database[gugu.account] = gugu
        database[philz.account] = philz
    }

    override fun save(user: User) {
        database[user.account] = user
    }

    override fun findByAccount(account: String): User? {
        return database[account]
    }
}