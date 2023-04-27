package com.aviax.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.aviax.game.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater,container,false)
        binding.game.setEndListener(object : GameView.Companion.EndListener {
            override fun end() {
                requireActivity().runOnUiThread {
                    binding.textView13.text = binding.game.score.toString()
                    val set = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())
                    var set1 = HashSet<String>()
                    set1.addAll(set!!.toList())
                    if(!set1.contains(binding.game.score.toString())) set1.add(binding.game.score.toString())
                    requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putStringSet("score",set1).apply()
                    binding.pause1.visibility = View.VISIBLE
                }
            }

            override fun score(score: Int) {
                requireActivity().runOnUiThread {
                    binding.progressBar.progress = score
                }
            }

        })
        binding.button4.setOnClickListener {
            binding.pause.visibility = View.GONE
            binding.game.togglePause()
        }
        binding.imageView3.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            navController.navigate(R.id.gameFragment)
        }
        binding.button5.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.imageView18.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            navController.navigate(R.id.gameFragment)
        }
        binding.imageView14.setOnClickListener {
            binding.game.togglePause()
            binding.pause.visibility = View.VISIBLE
        }
        return binding.root
    }


}