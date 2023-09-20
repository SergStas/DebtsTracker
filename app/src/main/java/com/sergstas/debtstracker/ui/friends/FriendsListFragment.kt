package com.sergstas.debtstracker.ui.friends

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentFriendsBinding

class FriendsListFragment: Fragment(R.layout.fragment_friends) {
    private val binding by viewBinding(FragmentFriendsBinding::bind)
}