package com.purukajal.kryptofare.fragment

import android.os.Bundle
import android.util.Log
import android.view.ActionProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.purukajal.kryptofare.R
import com.purukajal.kryptofare.adapter.TopLossGainPagerAdapter
import com.purukajal.kryptofare.adapter.TopMarketAdapter
import com.purukajal.kryptofare.apis.ApiInterface
import com.purukajal.kryptofare.apis.ApiUtilities
import com.purukajal.kryptofare.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getTopCurrencyList()

        setTabLayout()

        return binding.root
    }

    private fun setTabLayout() {

        val adapter = TopLossGainPagerAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0){

                    binding.topGainIndicator.visibility = View.VISIBLE
                    binding.topLoseIndicator.visibility = View.GONE

                }
                else{

                    binding.topGainIndicator.visibility = View.GONE
                    binding.topLoseIndicator.visibility = View.VISIBLE



                }
            }


        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager){

            tab, position ->
            var title = if(position == 0){

                "Top Gainers"
            }else{
                "Top Losers"

            }
            tab.text = title

        }.attach()




    }

    private fun getTopCurrencyList() {

        lifecycleScope.launch(Dispatchers.IO){

            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            withContext(Dispatchers.Main){

                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), res.body()!!.data.cryptoCurrencyList)

            }

            Log.d("PURU", "getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")

        }

    }

}