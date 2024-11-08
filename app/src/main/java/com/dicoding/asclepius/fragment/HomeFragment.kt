package com.dicoding.asclepius.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dicoding.asclepius.viewmodel.HomeViewModel
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.ResultActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun startGallery() {
            // TODO: Mendapatkan gambar dari Gallery.
        }

        fun showImage() {
            // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        }

        fun analyzeImage() {
            // TODO: Menganalisa gambar yang berhasil ditampilkan.
        }

        fun moveToResult() {
            val intent = Intent(requireContext(), ResultActivity::class.java)
            startActivity(intent)
        }

        fun showToast(message: String) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

    }

}