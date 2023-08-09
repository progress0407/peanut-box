package philo.peanutbox.core.test_case_2

interface UserDao {
    fun save(user: User)
    fun findByAccount(account: String): User?
}