package gs.firebase.demo.models

data class Chat(var id: String? = null,
                var userId: String? = null,
                @Transient var user: User? = null,
                var timestamp: Long? = null,
                var message: String? = null)