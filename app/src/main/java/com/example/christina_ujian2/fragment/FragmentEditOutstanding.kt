package com.example.christina_ujian2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.androidstarting.model.DataOutstanding
import com.example.androidstarting.model.ResponseSuccess
import com.example.androidstarting.retrofit.RetrofitClient
import com.example.christina_ujian2.R
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentEditOutstanding : Fragment() {

    companion object {
        fun newInstance(item: DataOutstanding): FragmentEditOutstanding {
            val fragment = FragmentEditOutstanding()
            val bundle = Bundle()
            bundle.putParcelable("item", item)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var tlNama: TextInputLayout
    private lateinit var tlAlamat: TextInputLayout
    private lateinit var tlOutstanding: TextInputLayout
    private var outstandingItem: DataOutstanding? = null
    private lateinit var btnSendData: Button
    private lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        outstandingItem = arguments?.getParcelable<DataOutstanding>("item")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_outstanding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tlNama = view.findViewById(R.id.tlNama)
        tlAlamat = view.findViewById(R.id.tlAlamat)
        tlOutstanding = view.findViewById(R.id.tlOutstanding)
        btnSendData = view.findViewById(R.id.btnSendData)
        spinner = view.findViewById(R.id.spinner)

        tlNama.editText!!.setText(outstandingItem!!.nama)
        tlAlamat.editText!!.setText(outstandingItem!!.alamat)
        tlOutstanding.editText!!.setText(outstandingItem!!.outstanding)

        btnSendData.setOnClickListener {
            updateData(view)
        }
    }

    fun updateData(view: View) {
        val apiService = RetrofitClient.apiService

        spinner.visibility = View.VISIBLE

        // Prepare the data as RequestBody objects
        val idRequestBody = createTextRequestBody(outstandingItem!!.id!!)
        val namaRequestBody = createTextRequestBody(tlNama.editText!!.text.toString())
        val alamatRequestBody = createTextRequestBody(tlAlamat.editText!!.text.toString())
        val outstandingRequestBody = createTextRequestBody(tlOutstanding.editText!!.text.toString())

        // Call the edit function and enqueue the request
        apiService.updateData(idRequestBody, namaRequestBody, alamatRequestBody, outstandingRequestBody)
            .enqueue(object : Callback<ResponseSuccess> {
                override fun onResponse(
                    call: Call<ResponseSuccess>,
                    response: Response<ResponseSuccess>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        // Handle successful response here
                        spinner.visibility = View.GONE

                        Toast.makeText(activity, "Successfully updated", Toast.LENGTH_SHORT).show()
                        Log.i("Hasil edit", responseData.toString())
                        exitFragment(view)
                    } else {
                        // Handle API error here
                        Log.e("Hasil API edit", "Gagal")
                    }
                }

                override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                    // Handle network or other errors here
                    Log.e("Hasil onFailure", t.message.toString())
                }
            })
    }

    fun exitFragment(view: View) {
        val fragmentManager = requireActivity().supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, FragmentAddOutstanding())
                .commit()
        }
    }

    fun createTextRequestBody(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}