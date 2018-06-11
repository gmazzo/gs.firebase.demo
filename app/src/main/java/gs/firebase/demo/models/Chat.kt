package gs.firebase.demo.models

data class Chat(var id: String? = null,
                var userId: String? = null,
                var user: User? = null,
                var timestamp: Long? = null,
                var message: String? = null,
                var nudge: Boolean = false)