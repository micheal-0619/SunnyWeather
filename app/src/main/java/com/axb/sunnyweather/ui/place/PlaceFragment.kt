package com.axb.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.axb.sunnyweather.MainActivity
import com.axb.sunnyweather.R
import com.axb.sunnyweather.WeatherActivity
import com.axb.sunnyweather.databinding.FragmentPlaceBinding

private const val TAG = "PlaceFragment"

class PlaceFragment : Fragment() {
    private lateinit var bindingInflate : FragmentPlaceBinding

    val viewModel by lazy { ViewModelProviders.of(this)[PlaceViewModel::class.java] }

    private lateinit var adapter: PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingInflate  = FragmentPlaceBinding.inflate(inflater, container, false);
        return bindingInflate.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        bindingInflate.recyclerView.layoutManager = layoutManager
        Log.d(TAG, "onActivityCreated: 11111111")
        adapter = PlaceAdapter(this, viewModel.placeList)
        bindingInflate.recyclerView.adapter = adapter
        bindingInflate.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            Log.d(TAG, "onActivityCreated: content== $content")
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                bindingInflate.recyclerView.visibility = View.GONE
                bindingInflate.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                bindingInflate.recyclerView.visibility = View.VISIBLE
                bindingInflate.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}