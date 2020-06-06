package com.example.travelmate.ui.attractionDetails.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmate.R
import com.example.travelmate.databinding.LayoutCommentCellBinding
import com.example.travelmate.model.Comment
import java.text.DateFormat


class CommentsRVAdapter(
    var commentsList: List<Comment>,
    var viewModel: CommentsViewModel
) :
    RecyclerView.Adapter<CommentsRVAdapter.CommentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = DataBindingUtil.inflate<LayoutCommentCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_comment_cell,
            parent,
            false
        )
        return CommentsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }


    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.commentInfo.comment = commentsList[position]
        holder.commentInfo.date = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
            .format(commentsList[position].date)
    }

    class CommentsViewHolder(itemBinding: LayoutCommentCellBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var commentInfo = itemBinding
    }
}
