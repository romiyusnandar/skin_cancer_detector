package com.dicoding.asclepius.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.local.LocalResponse
import com.dicoding.asclepius.utils.ViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeLiveData()
    }

    private fun setupRecyclerView() {
        with(binding.rvHistory) {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLiveData() {
        viewModel.history.observe(viewLifecycleOwner) { results ->
            setResults(results)
        }
    }

    private fun setResults(results: List<LocalResponse>) {
        val adapter = HistoryAdapter().apply { submitList(results) }
        binding.rvHistory.adapter = adapter
        isHistoryEmpty(results)
    }

    private fun isHistoryEmpty(results: List<LocalResponse>) {
        binding.textNotFound.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        binding.rvHistory.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}