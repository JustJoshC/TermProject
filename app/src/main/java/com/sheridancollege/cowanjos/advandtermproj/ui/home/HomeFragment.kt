package com.sheridancollege.cowanjos.advandtermproj.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup WebView
        setupWebView()

        homeViewModel.text.observe(viewLifecycleOwner) {
            binding.textHome.text = it
        }
        return root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.homeWebView.apply {
            webViewClient = WebViewClient() // Ensures that links open within the WebView
            settings.javaScriptEnabled = true // Enable JavaScript if needed
            loadUrl("https://www.planetfitness.com/community/articles/7-motivating-gym-tips-beginners")
            // Replace with your desired URL
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
