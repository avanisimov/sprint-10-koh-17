package ru.yandex.practicum.sprint10koh17

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.yandex.practicum.sprint10koh17.databinding.ChatItemInputBinding
import ru.yandex.practicum.sprint10koh17.databinding.ChatItemOutputBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatItemsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items: List<ChatItem> = emptyList()

    fun updateItems(items: List<ChatItem>) {
//        this.items = newItems
//        notifyDataSetChanged()
//        notifyItemInserted(newItems.lastIndex)

        val oldItems = this.items
        val newItems = items.toMutableList()
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        this.items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val holder = when (viewType) {
            VIEW_TYPE_INPUT -> {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_item_input, parent, false)
                ChatItemInputViewHolder(itemView)
            }
            VIEW_TYPE_OUTPUT -> {
                val itemView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.chat_item_output, parent, false)
                ChatItemOutputViewHolder(itemView)
            }
            else -> {
                throw IllegalStateException()
            }
        }

        Log.d("SPRINT_10", "onCreateViewHolder $holder")
        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ChatItemOutputViewHolder -> holder.bind(items[position])
            is ChatItemInputViewHolder -> holder.bind(items[position])
        }

        Log.d("SPRINT_10", "onBindViewHolder $holder $position")
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].origin) {
            ChatItem.Origin.INPUT -> VIEW_TYPE_INPUT
            ChatItem.Origin.OUTPUT -> VIEW_TYPE_OUTPUT
        }
    }

    companion object {
        const val VIEW_TYPE_INPUT = 1
        const val VIEW_TYPE_OUTPUT = 2
    }
}

class ChatItemOutputViewHolder(
    itemView: View
) : ViewHolder(itemView) {

    private val binding: ChatItemOutputBinding = ChatItemOutputBinding.bind(itemView)
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    fun bind(item: ChatItem) {
        binding.text.text = item.text
        binding.date.text = formatter.format(item.date)

        binding.status.setImageResource(
            when (item.status) {
                ChatItem.Status.SENT -> R.drawable.ic_sent
                ChatItem.Status.DELIVERED -> R.drawable.ic_delivered
                ChatItem.Status.READ -> R.drawable.ic_read
            }
        )
    }

}

class ChatItemInputViewHolder(
    itemView: View
) : ViewHolder(itemView) {

    private val binding: ChatItemInputBinding = ChatItemInputBinding.bind(itemView)
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    fun bind(item: ChatItem) {
        binding.text.text = item.text
        binding.date.text = formatter.format(item.date)
    }

}