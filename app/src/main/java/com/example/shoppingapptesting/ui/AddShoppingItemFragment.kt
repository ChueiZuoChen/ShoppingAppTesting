package com.example.shoppingapptesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shoppingapptesting.R
import com.example.shoppingapptesting.databinding.FragmentAddShoppingItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment() {

    lateinit var binding: FragmentAddShoppingItemBinding

    private val viewModel: ShoppingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        /**當add shoppingItem 按返回的時候再回來image會預設成上次選取的，所以必須重新設定ViewModel的url為預設空字串*/
        val callback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                /**重設當前的imageurl，並且pop NaviController的fragment堆疊*/
                viewModel.setCurrentImageUrl("")
                findNavController().popBackStack()
            }
        }
        /**最後在activity的onBackPressDispatcher加入callback的動作*/
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}