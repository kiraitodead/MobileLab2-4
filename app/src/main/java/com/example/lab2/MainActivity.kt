package com.example.lab2

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import getMealCategories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

class MainActivity : AppCompatActivity(), RecipeAdapter.Listener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories: RecyclerView = findViewById(R.id.rcView)
        categories.layoutManager = LinearLayoutManager(this)

        var datalist: MutableList<DataItem> = mutableListOf()
        val db = MainDb.getDb(this@MainActivity)
        lifecycleScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(this@MainActivity)) {
                val url = URL("https://www.themealdb.com/api/json/v1/1/categories.php")
                val categoriesObject = JSONTokener(url.readText()).nextValue() as JSONObject
                val categoriesArray = categoriesObject.getJSONArray("categories")
                datalist = MutableList(categoriesArray.length()) { i ->
                    val idCategory = categoriesArray.getJSONObject(i).getString("idCategory")
                    val strCategory = categoriesArray.getJSONObject(i).getString("strCategory")
                    val strCategoryThumb =
                        categoriesArray.getJSONObject(i).getString("strCategoryThumb")
                    DataItem(strCategory, strCategoryThumb, "", idCategory)
                }
                val categories : MutableList<Category> = mutableListOf()
                datalist.forEach{
                    categories.add(Category(it.title, it.image))
                }
                db.categoryDao().delete()
                db.categoryDao().insert(categories)
            } else {
                val categories : List<Category> = db.categoryDao().getAllCategories()
                categories.forEach{
                   datalist.add(DataItem(it.title, it.image, "", ""))
                }
            }

            withContext(Dispatchers.Main) {
                val adapter: RecipeAdapter = RecipeAdapter(datalist, this@MainActivity)
                categories.adapter = adapter
            }
        }
    }

    override fun onCLick(dataItem: DataItem) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("name", dataItem.title)
        startActivity(intent)
    }
}