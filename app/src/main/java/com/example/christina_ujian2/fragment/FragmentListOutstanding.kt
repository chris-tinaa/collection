package com.example.christina_ujian2.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidstarting.model.DataOutstanding
import com.example.androidstarting.model.ResponseGetAllOutstanding
import com.example.androidstarting.model.ResponseSuccess
import com.example.androidstarting.retrofit.RetrofitClient
import com.example.christina_ujian2.R
import com.example.christina_ujian2.adapter.OutstandingAdapter
import com.example.christina_ujian2.callback.OnItemClick
import com.example.christina_ujian2.callback.OnItemLongClick
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FragmentListOutstanding : Fragment() {

    private lateinit var rvOutstanding: RecyclerView
    private lateinit var adapter: OutstandingAdapter
    private lateinit var ivAdd: ImageView
    private lateinit var tlSearch: TextInputLayout
    private lateinit var tvSearch: TextView
    private lateinit var spinner: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var outstandingList: List<DataOutstanding?>? = null
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_outstanding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ivAdd = view.findViewById(R.id.ivAdd)
        tlSearch = view.findViewById(R.id.tlSearch)
        tvSearch = view.findViewById(R.id.tvSearch)
        spinner = view.findViewById(R.id.spinner)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        rvOutstanding = view.findViewById(R.id.rvOutstanding)
        rvOutstanding.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getDataAndSetupRecyclerView()

        swipeRefreshLayout.setOnRefreshListener {
            refreshLayout()
        }

        ivAdd.setOnClickListener {
            loadFragment(FragmentAddOutstanding())
        }

        tvSearch.setOnClickListener {
            searchItem(tlSearch.editText!!.text.toString())
        }

    }

    fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null) // Optional: add to back stack
        transaction.commit()
    }

    fun createTextRequestBody(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun getDataAndSetupRecyclerView() {
        val apiService = RetrofitClient.apiService

        spinner.visibility = View.VISIBLE

        apiService.getAll().enqueue(object : Callback<ResponseGetAllOutstanding> {
            override fun onResponse(
                call: Call<ResponseGetAllOutstanding>,
                response: Response<ResponseGetAllOutstanding>
            ) {
                if (response.isSuccessful) {
                    spinner.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false

                    val responseData = response.body()

                    Log.i("hasil masuk", responseData.toString())
                    responseData?.let {
                        outstandingList = it.data!!.collection
                        adapter = OutstandingAdapter(outstandingList,
                            object : OnItemClick<DataOutstanding> {
                                override fun onItemClick(item: DataOutstanding) {
                                    loadFragment(FragmentEditOutstanding.newInstance(item))
                                }
                            },
                            object : OnItemLongClick<DataOutstanding> {
                                override fun onItemLongClick(item: DataOutstanding) {
                                    showConfirmationDialog(item)
                                }

                            },
                            object : OnItemClick<DataOutstanding> {
                                override fun onItemClick(item: DataOutstanding) {
                                    showConfirmationDialog(item)
                                }
                            }
                        )
                        rvOutstanding.adapter = adapter
                    }
                } else {
                    // Handle API error here
                    Log.e("Hasil API get all error", response.toString())
                }
            }

            override fun onFailure(
                call: Call<ResponseGetAllOutstanding>,
                t: Throwable
            ) {
                // Handle network or other errors here
                Log.e("Hasil onFailure", t.message.toString())
            }
        })

    }

    fun refreshLayout() {
        val apiService = RetrofitClient.apiService

        apiService.getAll().enqueue(object : Callback<ResponseGetAllOutstanding> {
            override fun onResponse(
                call: Call<ResponseGetAllOutstanding>,
                response: Response<ResponseGetAllOutstanding>
            ) {
                if (response.isSuccessful) {
                    swipeRefreshLayout.isRefreshing = false

                    val responseData = response.body()

                    Log.i("hasil masuk", responseData.toString())
                    responseData?.let {
                        outstandingList = it.data!!.collection
                        adapter = OutstandingAdapter(outstandingList,
                            object : OnItemClick<DataOutstanding> {
                                override fun onItemClick(item: DataOutstanding) {
                                    loadFragment(FragmentEditOutstanding.newInstance(item))
                                }
                            },
                            object : OnItemLongClick<DataOutstanding> {
                                override fun onItemLongClick(item: DataOutstanding) {
                                    showConfirmationDialog(item)
                                }

                            },
                            object : OnItemClick<DataOutstanding> {
                                override fun onItemClick(item: DataOutstanding) {
                                    showConfirmationDialog(item)
                                }
                            }
                        )
                        rvOutstanding.adapter = adapter
                    }
                } else {
                    // Handle API error here
                    Log.e("Hasil API get all error", response.toString())
                }
            }

            override fun onFailure(
                call: Call<ResponseGetAllOutstanding>,
                t: Throwable
            ) {
                // Handle network or other errors here
                Log.e("Hasil onFailure", t.message.toString())
            }
        })

    }

    private fun showConfirmationDialog(item: DataOutstanding) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null)
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setView(dialogView)


        dialog = dialogBuilder.create()
        dialog.show()

        val deleteButton = dialogView.findViewById<Button>(R.id.delete_button)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)

        deleteButton.setOnClickListener {
            // Handle the confirmation and delete the item
            deleteItem(item)
        }

        cancelButton.setOnClickListener {
            // Cancel the deletion
            dialog.dismiss()
        }
    }

    fun deleteItem(item: DataOutstanding) {
        val apiService = RetrofitClient.apiService
        spinner.visibility = View.VISIBLE

        // Call the deleteData function and enqueue the request
        apiService.deleteData(DataOutstanding(id = item!!.id.toString()))
            .enqueue(object : Callback<ResponseSuccess> {
                override fun onResponse(
                    call: Call<ResponseSuccess>,
                    response: Response<ResponseSuccess>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        // Handle successful response here
                        spinner.visibility = View.GONE
                        Log.i("Hasil delete", responseData.toString())
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        getDataAndSetupRecyclerView()
                    } else {
                        // Handle API error here
                        Log.e("Hasil API delete", "Gagal")
                    }
                }

                override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                    // Handle network or other errors here
                    Log.e("Hasil onFailure", t.message.toString())
                }
            })
    }

    fun searchItem(keyword: String) {
        spinner.visibility = View.VISIBLE
        rvOutstanding.visibility = View.GONE

        val apiService = RetrofitClient.apiService

        apiService.getAllDataByFilter(filterValue = keyword)
            .enqueue(object : Callback<ResponseGetAllOutstanding> {
                override fun onResponse(
                    call: Call<ResponseGetAllOutstanding>,
                    response: Response<ResponseGetAllOutstanding>
                ) {
                    if (response.isSuccessful) {

                        rvOutstanding.visibility = View.VISIBLE
                        spinner.visibility = View.GONE

                        val responseData = response.body()
                        responseData?.let {
                            outstandingList = it.data!!.collection
                            adapter = OutstandingAdapter(outstandingList,
                                object : OnItemClick<DataOutstanding> {
                                    override fun onItemClick(item: DataOutstanding) {
                                        loadFragment(FragmentEditOutstanding.newInstance(item))
                                    }
                                },
                                object : OnItemLongClick<DataOutstanding> {
                                    override fun onItemLongClick(item: DataOutstanding) {
                                        showConfirmationDialog(item)
                                    }

                                },
                                object : OnItemClick<DataOutstanding> {
                                    override fun onItemClick(item: DataOutstanding) {
                                        showConfirmationDialog(item)
                                    }
                                }
                            )
                            rvOutstanding.adapter = adapter

                        }
                    } else {
                        // Handle API error here
                        Log.e("Hasil API search", response.message())
                    }
                }

                override fun onFailure(
                    call: Call<ResponseGetAllOutstanding>,
                    t: Throwable
                ) {
                    // Handle network or other errors here
                    Log.e("Hasil onFailure", t.message.toString())
                }
            })
    }

}
