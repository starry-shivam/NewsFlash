package com.starry.newsflash

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.starry.newsflash.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainRecyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this, this)
        binding.mainRecyclerView.adapter = mAdapter
    }

    private fun fetchData() {
       val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=e609e6b5b0f742d9a94019eac580d527"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                Log.e("TAG", "fetchData: $it")
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val title =  newsJsonObject.getString("title")
                    // make sure author is not null.
                    var author =  newsJsonObject.getString("title")
                    if (author == "null") {
                        author = "Unknown Author"
                    }
                    val newsUrl = newsJsonObject.getString("url")
                    var imageUrl =  newsJsonObject.getString("urlToImage")
                    if (imageUrl == "null") {
                        imageUrl = "https://telegra.ph//file/b11c1794cacad10abbd4d.png"
                    }
                    val news = News(title, author, newsUrl, imageUrl)
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },

            {
                Log.d("Error occur", "Try again..." + it.networkResponse.statusCode)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}