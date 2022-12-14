package com.example.bahadir_eray_retrofitproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bahadir_eray_retrofitproject.R
import com.example.bahadir_eray_retrofitproject.adapter.RecyclerViewAdapter
import com.example.bahadir_eray_retrofitproject.databinding.FragmentMenuBinding
import com.example.bahadir_eray_retrofitproject.model.MarsModel
import com.example.bahadir_eray_retrofitproject.service.MarsAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment(), RecyclerViewAdapter.Listener {

    //Classes are defined.
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val marsAPIService = MarsAPIService()
    private var marsModel: ArrayList<MarsModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    //The RecycleView will list the bind operation done.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromAPI()
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(
            requireContext().applicationContext,
            2,
            GridLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.layoutManager = layoutManager

    }

    //Adapter onItemClick->The purpose of the created intents will convey to the detail page
    override fun onItemClick(marsModel: MarsModel) {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_menuFragment_to_detailsFragment, Bundle().apply {
                    putString("marsIdDetail", marsModel.id.toString())
                    putString("marsTypeDetail", marsModel.type)
                    putString("marsPriceDetail", marsModel.price.toString())
                    putString("marsImgDetail", marsModel.img_src)
                })
        }
    }

    /*
    We are requesting the 'API' service made.
    If the answer is successful, we assign it to the 'MarsModel'.
     */
    private fun getDataFromAPI() {
        val call = marsAPIService.getMars()
        call.enqueue(object : Callback<List<MarsModel>> {
            override fun onResponse(
                call: Call<List<MarsModel>>,
                response: Response<List<MarsModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        marsModel = ArrayList(it)
                        marsModel?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it!!, this@MenuFragment)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<MarsModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}