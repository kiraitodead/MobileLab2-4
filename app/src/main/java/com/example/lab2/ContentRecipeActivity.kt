package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class ContentRecipeActivity : AppCompatActivity() {

    val dishes: Map<String, DataItem>
        get() = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_recipe)

        val id = intent.getStringExtra("name")
        val db = MainDb.getDb(this@ContentRecipeActivity)
        var datalist: MutableList<DataItem> = mutableListOf()
        lifecycleScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(this@ContentRecipeActivity)) {
                val url = URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id")
                val mealsObject = JSONTokener(url.readText()).nextValue() as JSONObject
                val mealsArray = mealsObject.getJSONArray("meals")
                datalist = MutableList(mealsArray.length()) { i ->
                    val idMeal = mealsArray.getJSONObject(i).getString("idMeal")
                    val strMeal = mealsArray.getJSONObject(i).getString("strMeal")
                    val strMealThumb =
                        mealsArray.getJSONObject(i).getString("strMealThumb")
                    val strInstructions =
                        mealsArray.getJSONObject(i).getString("strInstructions")
                    DataItem(strMeal, strMealThumb, strInstructions, idMeal)
                }
                val recipes : MutableList<Recipe> = mutableListOf()
                datalist.forEach{
                    recipes.add(Recipe(it.title, it.image, it.content))
                }
                db.recipeDao().delete()
                db.recipeDao().insert(recipes)
            } else {
                    val recipes : List<Recipe> = db.recipeDao().getAllRecipes()
                    recipes.forEach{
                        datalist.add(DataItem(it.title, it.image, it.content, ""))
                    }
                }
            withContext(Dispatchers.Main) {

                val textName: TextView = findViewById(R.id.textName)
                val textContent: TextView = findViewById(R.id.textRecipies)
                val imagePhoto: ImageView = findViewById(R.id.photoDishes)
                Picasso.get()
                    .load(datalist[0].image)
                    .into(imagePhoto)

                textName.text = datalist[0].title
                textContent.text = datalist[0].content
            }
        }
    }
}