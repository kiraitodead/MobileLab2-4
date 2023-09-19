package com.example.lab2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class CategoryActivity : AppCompatActivity(), RecipeAdapter.Listener {

    var recipes: Map<String, List<DataItem>>
        get() = mapOf()
        set(value) = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val name = intent.getStringExtra("name")

        val recipes: RecyclerView = findViewById(R.id.rcView)
        recipes.layoutManager = LinearLayoutManager(this)

        var datalist: MutableList<DataItem> = mutableListOf()
        val db = MainDb.getDb(this@CategoryActivity)
        lifecycleScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(this@CategoryActivity)) {
                val url = URL("https://www.themealdb.com/api/json/v1/1/filter.php?c=$name")
                val mealsObject = JSONTokener(url.readText()).nextValue() as JSONObject
                val mealsArray = mealsObject.getJSONArray("meals")
                datalist = MutableList(mealsArray.length()) { i ->
                    val idMeal = mealsArray.getJSONObject(i).getString("idMeal")
                    val strMeal = mealsArray.getJSONObject(i).getString("strMeal")
                    val strMealThumb =
                        mealsArray.getJSONObject(i).getString("strMealThumb")
                    DataItem(strMeal, strMealThumb, "", idMeal)
                }
                val meals : MutableList<Meal> = mutableListOf()
                datalist.forEach{
                    meals.add(Meal(it.title, it.image, it.id))
                }
                db.mealDao().delete()
                db.mealDao().insert(meals)
            } else {
                val meals : List<Meal> = db.mealDao().getAllMeals()
                meals.forEach{
                    datalist.add(DataItem(it.title, it.image, "", it.recipeID))
                }
            }
            withContext(Dispatchers.Main) {
                val adapter: RecipeAdapter = RecipeAdapter(datalist, this@CategoryActivity)
                recipes.adapter = adapter
            }
        }
    }

    override fun onCLick(dataItem: DataItem) {
        val intent = Intent(this, ContentRecipeActivity::class.java)
        intent.putExtra("name", dataItem.id)
        startActivity(intent)
    }
}