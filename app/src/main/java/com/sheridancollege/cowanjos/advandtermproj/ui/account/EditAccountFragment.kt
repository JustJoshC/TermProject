package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sheridancollege.cowanjos.advandtermproj.R

class EditAccountFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        inflater.inflate(R.layout.fragment_account_edit, container, false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}