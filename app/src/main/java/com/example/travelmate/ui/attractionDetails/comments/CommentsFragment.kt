package com.example.travelmate.ui.attractionDetails.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelmate.R
import com.example.travelmate.databinding.FragmentCommentsBinding
import com.example.travelmate.model.Comment
import com.example.travelmate.utils.Resource
import org.koin.android.ext.android.inject

class CommentsFragment : Fragment() {

    private val viewModel by inject<CommentsViewModel>()
    private lateinit var binding: FragmentCommentsBinding
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val actions =
                    CommentsFragmentDirections.fromCommentsToAttractionDetails(arguments?.get("attractionId") as String)
                navController.navigate(actions)
            }
        })

        binding.showEmpty = false
        binding.showComments = false
        binding.viewModel = viewModel
        viewModel.getComments(arguments?.get("attractionId") as String)
        viewModel.getCurrentUsername()
        observeComments()
        observeCurrentUsername()
        sendComment()
        observeAddComment()
    }

    private fun observeComments() {
        viewModel.loadComments.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        binding.showEmpty = true
                        binding.showComments = false
                    } else {
                        setUpCommentsRV(result.data)
                        binding.showComments = true
                        binding.showEmpty = false
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeCurrentUsername() {
        viewModel.username.observe(this, Observer { result ->
            if (result != null) {
                username = result
            }
        })
    }

    private fun sendComment() {
        binding.sendBtn.setOnClickListener {
            val text = binding.editTextField.text.toString()
            viewModel.addComment(username, arguments?.get("attractionId") as String, text)
        }
    }

    private fun observeAddComment() {
        viewModel.addCommentResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (result.data == true) {
                        Toast.makeText(context, "Published!", Toast.LENGTH_LONG).show()
                        binding.showComments = true
                        binding.showEmpty = false
                        viewModel.getComments(arguments?.get("attractionId") as String)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setUpCommentsRV(commentsList: List<Comment>) {
        val list = commentsList.sortedBy { comments ->
            comments.date
        }.asReversed()

        val adapter = context?.let {
            CommentsRVAdapter(list, viewModel)
        }
        binding.commentsRv.layoutManager = LinearLayoutManager(context)
        binding.commentsRv.adapter = adapter
    }
}
