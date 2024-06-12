package ru.yandex.practicum.sprint10koh17

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yandex.practicum.sprint10koh17.databinding.ActivityMainBinding
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {

//    private var chatItems = (0..200).map {
//        mutableListOf(
//            ChatItem(
//                text = "Hi",
//                date = Date(),
//                origin = ChatItem.Origin.OUTPUT,
//                status = ChatItem.Status.READ,
//            ),
//            ChatItem(
//                text = "How are you?",
//                date = Date(),
//                origin = ChatItem.Origin.OUTPUT,
//                status = ChatItem.Status.DELIVERED,
//            ),
//            ChatItem(
//                text = "Hope u r well?",
//                date = Date(),
//                origin = ChatItem.Origin.OUTPUT,
//                status = ChatItem.Status.SENT,
//            ),
//        )
//    }.flatten()

    private var chatItems = mutableListOf<ChatItem>()

    private val chatItemsAdapter = ChatItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottom = if (ime.bottom > 0) {
                ime.bottom
            } else {
                systemBars.bottom
            }
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottom)
            insets
        }
        /*
        old way
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val ivAction = findViewById<EditText>(R.id.ivAction)
        etMessage.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                ivAction.setImageResource(R.drawable.ic_audio)
            } else {
                ivAction.setImageResource(R.drawable.ic_send)
            }
        }
        */
        binding.etMessage.doOnTextChanged { text, start, before, count ->
            binding.ivAction.setImageResource(
                if (text.isNullOrEmpty()) {
                    R.drawable.ic_audio
                } else {
                    R.drawable.ic_send
                }
            )
        }

        binding.rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.rvItems.adapter = chatItemsAdapter

        binding.ivAction.setOnClickListener {
            val newMessageText = binding.etMessage.text?.toString()
            if (!newMessageText.isNullOrEmpty()) {
                val newMessage = ChatItem(
                    id = UUID.randomUUID().toString(),
                    text = newMessageText,
                    date = Date(),
                    origin = ChatItem.Origin.OUTPUT,
                    status = ChatItem.Status.SENT,
                )
                chatItems.add(0, newMessage)
                chatItemsAdapter.updateItems(chatItems)
                binding.etMessage.setText("")
                binding.rvItems.postDelayed({
                    val newInputMessage = ChatItem(
                        id = UUID.randomUUID().toString(),
                        text = "Ok, $newMessageText",
                        date = Date(),
                        origin = ChatItem.Origin.INPUT,
                        status = ChatItem.Status.SENT,
                    )
                    chatItems.add(0, newInputMessage)
                    chatItemsAdapter.updateItems(chatItems)
                }, 1000L)
            }
        }

    }
}