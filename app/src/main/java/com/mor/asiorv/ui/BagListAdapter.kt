package com.mor.asiorv.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.mor.asiorv.R
import com.mor.asiorv.model.PairTrashBag
import com.mor.asiorv.model.TrashBag
import com.mor.asiorv.util.visiBool

class BagListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PairTrashBag>() {


        override fun areItemsTheSame(oldItem: PairTrashBag, newItem: PairTrashBag): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PairTrashBag, newItem: PairTrashBag): Boolean {
            return (oldItem.trash1.id == newItem.trash1.id && oldItem.trash2.id == newItem.trash2.id)
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return BagVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.bag_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BagVH -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: MutableList<PairTrashBag>) {
        differ.submitList(list)
    }

    class BagVH
    constructor(
        itemView: View,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PairTrashBag) = with(itemView) {
            itemView.findViewById<ImageButton>(R.id.btn_delete_item).setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.findViewById<TextView>(R.id.item_id_content_first_bag).text = String.format("%.3s",item.trash1.id.toString())
            itemView.findViewById<TextView>(R.id.item_weight_content_first_bag).text = item.trash1.weight.toString()

            if(item.trash2.weight != 0.0){
                secondItemViewVisible(true)
                itemView.findViewById<TextView>(R.id.item_id_content_second_bag).text = String.format("%.3s",item.trash2.id.toString())
                itemView.findViewById<TextView>(R.id.item_weight_content_second_bag).text = item.trash2.weight.toString()
            }else{
                secondItemViewVisible(false)

            }
        }

        private fun secondItemViewVisible(visible: Boolean){
            itemView.findViewById<ConstraintLayout>(R.id.second_bag_item_layout).visiBool(visible)
            itemView.findViewById<TextView>(R.id.second_bag_title).visiBool(visible)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: PairTrashBag)
    }
}