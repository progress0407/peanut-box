package philo.peanutbox.core.test_case_2

import philo.peanutbox.core.annotation.AutoPeanut
import philo.peanutbox.core.annotation.GiveMePeanut

@AutoPeanut
class UserService private constructor() {

    @GiveMePeanut
    private val userDao: UserDao? = null

    fun join(user: User): User? {
        userDao!!.save(user)
        return userDao.findByAccount(user.account)
    }
}