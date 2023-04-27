package com.aviax.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.aviax.game.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScoreBinding.inflate(inflater,container,false)
        binding.textView.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        val set = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())
        var list = mutableListOf<String>()
        list.addAll(set!!.toList())
        list.sortBy { -it.toInt() }
        val adapter = ScoreAdapter(list)
        binding.list.adapter = adapter
        return binding.root
    }


}