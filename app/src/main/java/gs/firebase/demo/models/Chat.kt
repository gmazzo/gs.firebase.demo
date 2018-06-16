package gs.firebase.demo.models

data class Chat(var id: String? = null,
                var type: Type = Type.MESSAGE,
                var userId: String? = null,
                var user: User? = null,
                var timestamp: Long? = null,
                var message: String? = null) {

    enum class Type { MESSAGE, NUDGE }

}
