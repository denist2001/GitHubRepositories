package com.codechallenge.githubrepositories.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.githubrepositories.R
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import com.codechallenge.githubrepositories.util.clicks
import com.codechallenge.githubrepositories.util.throttleFirst
import kotlinx.android.synthetic.main.search_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
    PagingDataAdapter<RepoPresentation, SearchAdapter.ItemViewholder>(DiffCallback()) {

    private lateinit var clickFlowCollector: (action: NavDirections) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_item, parent, false),
            clickFlowCollector
        )
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ItemViewholder(itemView: View, val clickFlowCollector: (action: NavDirections) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        @ExperimentalCoroutinesApi
        @InternalCoroutinesApi
        fun bind(item: RepoPresentation) = with(itemView) {
            avatar_iv.load(item.avatarImage) {
                scale(Scale.FIT)
                placeholder(R.drawable.github)
                transformations(CircleCropTransformation())
            }
            repository_tv.text = item.repositoryName
            number_forks_tv.text = item.numberOfForks.toString()
            description_tv.text = item.description

            clicks().throttleFirst(1000L).onEach {
                if (item.repositoryName.isEmpty() || item.ownerName.isEmpty()) return@onEach
                val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                    item.id,
                    item.repositoryName,
                    item.ownerName,
                    item.watchersCount
                )
                clickFlowCollector(action)
            }.launchIn(GlobalScope)
        }
    }

    fun setClickCollector(clickCollector: (id: NavDirections) -> Unit) {
        clickFlowCollector = clickCollector
    }
}

class DiffCallback : DiffUtil.ItemCallback<RepoPresentation>() {
    override fun areItemsTheSame(oldItem: RepoPresentation, newItem: RepoPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RepoPresentation,
        newItem: RepoPresentation
    ): Boolean {
        return oldItem == newItem
    }
}