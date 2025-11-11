package com.example.lab_week_10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10.viewmodels.TotalViewModel

class FirstFragment : Fragment() {

    private lateinit var viewModel: TotalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViewModel(view)
    }

    private fun updateText(total: Int) {
        view?.findViewById<TextView>(R.id.text_total)?.text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel(view: View) {
        // Use shared ViewModel between Activity and Fragment
        viewModel = ViewModelProvider(requireActivity())[TotalViewModel::class.java]

        // Observe LiveData safely with viewLifecycleOwner
        viewModel.total.observe(viewLifecycleOwner) { total ->
            updateText(total)
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                // Optionally store params in arguments bundle if needed
            }
    }
}
