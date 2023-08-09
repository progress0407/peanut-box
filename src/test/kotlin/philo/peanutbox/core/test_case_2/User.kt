package philo.peanutbox.core.test_case_2

class User(private val id: Long, val account: String, private val password: String, private val email: String) {
    fun checkPassword(password: String): Boolean {
        return this.password == password
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}'
    }
}