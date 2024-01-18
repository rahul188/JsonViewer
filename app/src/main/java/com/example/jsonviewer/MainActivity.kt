package com.example.jsonviewer

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        val spinner: Spinner = findViewById(R.id.spinner)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val jsonUrl = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/littleLemonMenu.json"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, jsonUrl, null,
            Response.Listener { response ->
                val gson = Gson()
                val menuType = object : TypeToken<Map<String, MenuCategory>>() {}.type
                val menuMap: Map<String, MenuCategory> =
                    gson.fromJson(response.toString(), menuType)

                setupSpinner(spinner, menuMap)
                menuAdapter.submitList(emptyList()) // Clear existing data
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        requestQueue.add(jsonObjectRequest)

        menuAdapter = MenuAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = menuAdapter
    }

    private fun setupSpinner(spinner: Spinner, menuMap: Map<String, MenuCategory>) {
        val categories = menuMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = categories[position]
                val selectedMenu = menuMap[selectedCategory]?.menu ?: emptyList()

                menuAdapter.submitList(selectedMenu)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requestQueue.cancelAll(TAG)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
data class MenuCategory(val menu: List<String>)
